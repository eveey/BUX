# BUX
![App icon](https://github.com/eveey/BUX/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

Android app in [Kotlin](https://kotlinlang.org/) for viewing live trading product feed.

## Flavors

### Development
* Start mock service by running *```java -jar bux-server.jar```* on your local host (not included in the project)
* Run the app in the emulator
### Beta

## Architecture
* MVVM (Model-View-ViewModel) + [Data Binding](https://developer.android.com/topic/libraries/data-binding/) + [LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData) + [RxKotlin](https://github.com/ReactiveX/RxKotlin)

## Dependency management
* [Google/Dagger](https://github.com/google/dagger) - Dependency injection

## Network
* [Square/Retrofit](https://github.com/square/retrofit) - HTTP RESTful connections
* [Tinder/Scarlet](https://github.com/Tinder/Scarlet) - WebSocket connections
```
* Copyright (c) 2018, Match Group, LLC 
* All rights reserved.
```
* [Square/Moshi](https://github.com/square/moshi) - JSON adapter
* [Chuck](https://github.com/jgilfelt/chuck) - Network interceptor

## Other
### Date/time
* [ThreeTenABP](https://github.com/JakeWharton/ThreeTenABP) - 
An adaptation of the JSR-310 backport for Android
### Logging
* [Timber](https://github.com/JakeWharton/timber)

## Code quality
* [Ktlint](https://ktlint.github.io/) - An anti-bikeshedding Kotlin linter with built-in formatter
* [Detekt](https://github.com/arturbosch/detekt) - Static code analysis for Kotlin

## Unit tests
* In progress

## Roadmap:
* [Espresso](https://developer.android.com/training/testing/espresso/) - UI testing framework
* [ProGuard](https://www.guardsquare.com/en/products/proguard) - Code obfuscation
* [CircleCI](https://circleci.com/) - Continuous integration
* [Firebase](https://firebase.google.com/) - Analytics

## Wunderlist:
* [Android Jetpack/Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) - Android navigation framework

## Disclaimer:
```NOT FOR COMMERCIAL USE```
