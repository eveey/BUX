# BUX
![com.evastos.bux.screenshot.icon](https://github.com/eveey/BUX/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

Sample Android app in [Kotlin](https://kotlinlang.org/) for viewing live trading product feed.

`Important note: to use this app, add your auth token to local.properties file in the project like this:
authToken="{auth token}"`

## Flavors

### Development
* Start mock service by running *```java -jar bux-server.jar```* on your local host (not included in the project)
* Run the app in the emulator

### Beta

![com.evastos.bux.screenshot](https://github.com/eveey/BUX/blob/master/app/src/main/assets/tradingProductScreenshot.png)

## Architecture
* MVVM (Model-View-ViewModel) + [Data Binding](https://developer.android.com/topic/libraries/data-binding/) + [LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData) + [RxKotlin](https://github.com/ReactiveX/RxKotlin)

## Dependency management
* [Google/Dagger](https://github.com/google/dagger) - Dependency injection

## Network
* [Square/Retrofit](https://github.com/square/retrofit) - HTTP RESTful connections
* [Tinder/Scarlet](https://github.com/Tinder/Scarlet) - WebSocket connections
```
Copyright (c) 2018, Match Group, LLC 
All rights reserved.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL MATCH GROUP, LLC BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
```
* [Square/Moshi](https://github.com/square/moshi) - JSON adapter
* [Chuck](https://github.com/jgilfelt/chuck) - Network interceptor

## Code quality
* [Ktlint](https://ktlint.github.io/) - An anti-bikeshedding Kotlin linter with built-in formatter
* [Detekt](https://github.com/arturbosch/detekt) - Static code analysis for Kotlin

## Unit tests
* In progress

## Other
### Date/time
* [ThreeTenABP](https://github.com/JakeWharton/ThreeTenABP) - 
An adaptation of the JSR-310 backport for Android
### Logging
* [Timber](https://github.com/JakeWharton/timber)

## Roadmap:
* [Espresso](https://developer.android.com/training/testing/espresso/) - UI testing framework
* [ProGuard](https://www.guardsquare.com/en/products/proguard) - Code obfuscation
* [CircleCI](https://circleci.com/) - Continuous integration
* [Firebase](https://firebase.google.com/) - Analytics

## Wunderlist:
* [Android Jetpack/Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) - Android navigation framework

## Disclaimer:
```NOT INTENDED FOR COMMERCIAL USE```
