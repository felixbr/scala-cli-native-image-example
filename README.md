## Scala CLI native-image example

This is a small example how to build a CLI application which is compiled to a binary with no JVM startup delay via 
GraalVM (more precisely SubstrateVM) while using useful libraries like `decline`, `monix` and `better.files`.

### Prerequisites

You need to have the GraalVM `native-image` binary in your `PATH`. 

There are several ways to do this. I recommend to use `sdkman.io` and install `1.0-rc13` or newer

```bash
sdk install java 1.0.0-rc-13-grl
```   

### Running and building the program

For normal development you can use `sbt run` and related commands as usual.

Once you want to build your program into a binary, you run `sbt show graalvm-native-image:packageBin`, which will 
take some time and in the end provide you with the absolute path of your executable binary.

I recommend building the binary once in a while (especially if you add new libaries), as some things are not supported 
or need special `native-image` flags. Saves time if you notice it early :)
