# Hill Cipher Simulator

## Description

Hill Cipher Simulator is part of the final project at School of Electrical Engineering, University of Belgrade. It is supposed to help students understand and learn how Hill Cipher works.

## Requirements

- Java 1.8
- Ant

## Building The Simulator

To build the simulator clone this repository, open repository directory and execute:
```bash
ant -f build/build.xml
```
After the build finishes a bunch of files will appear inside of `build` directory containing build artifacts and deployable code.

## Running The Simulator

To run previously built simulator execute:
```bash
java -jar build/dist/Hill\ Cipher\ Simulator.jar
```

## FAQ

### Checking Java Version

Open the terminal and execute:
```bash
java -version
```

It will result in something like this:

```bash
java version "1.8.0_131"
Java(TM) SE Runtime Environment (build 1.8.0_131-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.131-b11, mixed mode)
```

### Checking Ant Version

Open the terminal and execute:
```bash
ant -version
```

It will result in something like this:

```bash
Apache Ant(TM) version 1.10.1 compiled on February 2 2017
```

