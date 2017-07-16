# Introduction
DroidREST is an Android library, which provides a RESTful interface for Android applications
to exchange POJO objects with a remote server.

## Features
1. Make HTTP GET requests to a remote web server.
2. Send POJO class objects in HTTP requests.
3. Receive POJO class objects in HTTP response.

# How to Use
1. In your project's `build.gradle` file, add the following code:
```
repositories {
  jcenter()
  maven {
    url  "http://dl.bintray.com/bytexcite/maven/" // add this maven block inside repositories block
  }
}
```
2. Compile library by adding `compile 'sfllhkhan95.android.rest:api:1.0.0'` to `build.gradle` of your module.


