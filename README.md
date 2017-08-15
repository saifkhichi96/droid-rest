# Introduction
DroidREST is an Android library, which provides a RESTful interface for Android applications
to exchange POJO objects with a remote server.

## Features
1. Make HTTP GET requests to a remote web server.
2. Send POJO class objects in HTTP requests.
3. Receive POJO class objects in HTTP response.

# How to Use
## Installation
1. Open  `build.gradle` file of your Android project's module
2. Add dependency to this library using:
```
dependencies {
  ...
  compile 'sfllhkhan95.android.rest:api:1.0.0'
}
```
3. Add the following packaging options:
```
android {
  ...
  packagingOptions {
    exclude 'META-INF/ASL2.0'
    exclude 'META-INF/LICENSE'
    exclude 'META-INF/license.txt'
    exclude 'META-INF/NOTICE'
    exclude 'META-INF/notice.txt'
  }
}
```
4. Sync gradle
