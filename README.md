## Scala CLI native-image example

This is a small example how to build a CLI application which is compiled to a binary (no JVM startup delay!) via 
[GraalVM (Substrate VM)](https://www.graalvm.org/docs/reference-manual/aot-compilation/) while using useful libraries like [decline](http://ben.kirw.in/decline/), [monix](https://monix.io/) and [better.files](https://github.com/pathikrit/better-files).

### Prerequisites

You need to have the GraalVM `native-image` binary in your `PATH`. 

There are several ways to do this. I recommend to use [sdkman.io](https://sdkman.io/) and install `1.0-rc13` or newer

```bash
sdk install java 1.0.0-rc-13-grl
```   

### Running and building the program

For normal development you can use `sbt run` and related commands as usual.

Once you want to build your program into a binary, you run `sbt show graalvm-native-image:packageBin`, which will 
take some time and eventually show you the absolute path of your newly built executable binary.

I recommend building the binary once in a while (especially if you add new libaries), as some things are not supported 
or need special `native-image` flags. Saves time if you notice it early :)
