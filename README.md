# BUX
Demo Android app in [Kotlin](https://kotlinlang.org/) for viewing live trading product feed

## Flavors

### Development
* Start mock service by running ```java -jar bux-server.jar``` on your local host
* Run the app in the emulator
### Beta
* Service available only during weekdays

## Architecture
* MVVM + Data Binding + LiveData

## Network
* [Square/Retrofit](https://github.com/square/retrofit) for HTTP RESTful connections
* [Tinder/Scarlet](https://github.com/Tinder/Scarlet) for WebSocket connections
```
* Copyright (c) 2018, Match Group, LLC 
* All rights reserved.
```
* [Square/Moshi](https://github.com/square/moshi) JSON adapter
* [Chuck](https://github.com/jgilfelt/chuck) network interceptor

## Code quality
* [Ktlint](https://ktlint.github.io/) for static analysis of Kotlin code
* [Detekt](https://github.com/arturbosch/detekt)

## Unit tests
* In progress

## Roadmap:
* [Espresso](https://developer.android.com/training/testing/espresso/) for UI tests
* [ProGuard](https://www.guardsquare.com/en/products/proguard) rules for code obfuscation
* [CircleCI](https://circleci.com/) for continuous integration
* [Firebase](https://firebase.google.com/) for analytics

## Wunderlist:
* [Android Jetpack/Navigation](https://developer.android.com/topic/libraries/architecture/navigation/)

## Disclaimer
```NOT FOR COMMERCIAL USE```
