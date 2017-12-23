# kodeinject [![Build Status](https://travis-ci.org/kailan/kodeinject.svg?branch=master)](https://travis-ci.org/kailan/kodeinject) [![Maven Central](https://img.shields.io/maven-central/v/pw.kmp/kodeinject.svg)](https://mvnrepository.com/artifact/pw.kmp/kodeinject/latest)

A small library allowing constructor dependency injection for applications using [Kodein](https://github.com/SalomonBrys/Kodein).

### Usage
#### Maven
```xml
<dependency>
    <groupId>pw.kmp</groupId>
    <artifactId>kodeinject</artifactId>
    <version>1.2.2</version>
</dependency>
```
#### Injected
```kotlin
class Application(db: Database) {
    // stuff goes here
}

val kodein = Kodein {
    bind<Database>() with singleton { MongoDB("127.0.0.1", 27017) }
    bind() from injected<Application>()
}
```
`kodeinject` will automatically instantiate your `injected()` classes using
bindings from the Kodein container.

#### Singleton
```kotlin
val kodein = Kodein {
    bind() from injectedSingleton<Application>()
}
```
Use `injectedSingleton()` to cache the dependency instead of creating a new
instance for each access.

#### Nullable
```kotlin
class Application(db: Database, cache: Cache?) {
    // stuff goes here
}
```
Paramaters that are nullable will be set to null if they are unavailable upon
injection.

#### Automatic Kodein injection
```kotlin
class Application(kodein: Kodein) {
    // stuff goes here
}
```
Constructors requiring the `Kodein` object will be injected with it by default, without the need to create a binding.
