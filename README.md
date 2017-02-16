# kodeinject

A small library allowing constructor dependency injection for applications using [Kodein](https://github.com/SalomonBrys/Kodein).

### Usage
#### Maven
```xml
<dependency>
    <groupId>pw.kmp</groupId>
    <artifactId>kodeinject</artifactId>
    <version>1.0.0</version>
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