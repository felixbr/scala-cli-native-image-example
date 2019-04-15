import java.io.InputStream
import java.nio.charset.StandardCharsets

import better.files._
import cats.implicits._
import com.monovore.decline._
import monix.eval.Task
import monix.execution.Scheduler
import monix.reactive.Observable

import scala.concurrent.duration._

/**
  * Once the binary is built (`sbt graalvm-native-image:packageBin`), try running it like this:
  *
  * {{{
  *   ./target/graalvm-native-image/scala-cli-native-image-example
  *
  *   # OR
  *
  *   ./target/graalvm-native-image/scala-cli-native-image-example --resource-file other_names.txt
  * }}}
  */
object Main
    extends CommandApp(
      name = "main",
      header = "A cli application which is built as native-image via Graal",
      main = {
        implicit val scheduler: Scheduler = Scheduler.global

        (
          MainParams.verbose,
          MainParams.resourceName,
        ).mapN(MainImpl.run).map(_.runSyncUnsafe(Duration.Inf))
      }
    )

object MainParams {
  val availableResourceNames = List("names.txt", "other_names.txt")

  val verbose: Opts[Boolean] =
    Opts.flag(long = "verbose", short = "v", help = "Outputs more verbose logging").orFalse

  val resourceName: Opts[String] =
    Opts
      .option[String](
        long = "resource-file",
        short = "f",
        help = "The resource-file to use",
        metavar = s"${availableResourceNames.mkString("|")}"
      )
      .validate(s"Error: resource-file needs to be one of the following: ${availableResourceNames.mkString(" ")}")(
        name => availableResourceNames.contains(name)
      )
      .withDefault("names.txt")
}

object MainImpl {
  private val ioScheduler = monix.execution.Scheduler.io()

  def run(verbose: Boolean, resourceName: String): Task[Unit] =
    for {
      resourceContentOpt <- readResourceLines(resourceName)

      _ <- resourceContentOpt match {
        case None                  => tprintln(s"Error: No resource file found for file name: $resourceName")
        case Some(resourceContent) => tprintln(s"Resource content:\n$resourceContent")
      }
    } yield ()

  private def tprintln(str: String): Task[Unit] = Task(println(str))

  // The code below is intentionally convoluted to see if the various parts of monix and better.files work
  // in conjunction with GraalVM native-image. This is not an exhaustive test, of course...

  private def readResourceLines(fileName: String): Task[Option[String]] =
    openResourceStream(fileName).use {
      case None =>
        Task.pure(None)

      case Some(stream) =>
        Observable
          .fromInputStream(Task.pure(stream))
          .map(bytes => new String(bytes, StandardCharsets.UTF_8))
          .foldL
          .map(_.some)
    }

  private def openResourceStream(fileName: String): cats.effect.Resource[Task, Option[InputStream]] =
    cats.effect.Resource.make {
      Task {
        Resource.my.asStream(fileName) // native-image cli options are needed for static resources; see build.sbt
      }.executeOn(ioScheduler)
    } { stream =>
      Task(stream.foreach(_.close()))
    }
}
