import org.scalatest._

class MainSpec extends WordSpec with MustMatchers {

  "1 + 1" must {
    "equal 2" in {
      (1 + 1) mustEqual 2
    }
  }
}
