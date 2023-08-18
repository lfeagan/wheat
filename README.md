# wheat
A staple set of libraries for Java.

## How to Get Wheat
You can add this library into your Maven/Gradle/SBT/Leiningen project thanks to JitPack.io. Follow the instructions [here](https://jitpack.io/#lfeagan/distributed-task-manager).

### Example Gradle Instructions

Add this into your build.gradle file:

```groovy
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
  implementation 'com.github.lfeagan:wheat:0.0.2'
}
```

## Dependencies
1. [ThreeTen Extra](https://www.threeten.org/threeten-extra/) -- For class PeriodDuration
2. [SLF4J](https://www.slf4j.org/) -- For logging
3. [TestNG](https://testng.org/) -- For testing

## Potential Dependencies
1. [Guava](https://github.com/google/guava) -- For ImmutableSet
2. [Lombok](https://projectlombok.org/) -- For saving my sanity
3. [Apache Commons Lang3]() -- For Pair
