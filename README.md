# Introduction
DroidREST is an Android library, which provides a RESTful interface for Android applications
to exchange POJO objects with a remote server.

## Features
1. Make HTTP GET requests to a remote web server.
1. Make HTTP POST requests to a remote web server.
2. Send POJO class objects in HTTP requests.
3. Receive POJO class objects in HTTP response.

## Release Notes
### v2.0.1
Deprecated classes removed. All communication to be done through the HttpTask class.

### Older Versions
Support for older versions has been officially dropped. They are no longer available for use in Gradle.

# How to Use
## Installation
1. Open  `build.gradle` file of your Android project's module
2. Add dependency to this library using:
```
dependencies {
  // other dependencies ...
  compile 'sfllhkhan95.android.rest:api:2.0.1'
}
```
3. Add the following packaging options:
```
android {
  // existing configurations ...
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
## Defining a webserver
`HttpServer` class defines connections to a remote server. You will need to pass an instance of this class to each `HttpRequest` you make. We recommend subclassing `HttpServer` in a singleton class which can then be used to access a shared instance of the webserver anywhere in the project.

Following sample code may be used as a starting point for your project.
```
// package declaration

import java.net.MalformedURLException;
import sfllhkhan95.android.rest.HttpServer;

public class WebServer extends HttpServer {
    private static WebServer ourInstance;

    public static WebServer getInstance() {
        if (ourInstance == null) {
            try {
                ourInstance = new WebServer();
            } catch (MalformedURLException e) {
                throw new InstantiationError("Check server URL.");
            }
        }
        return ourInstance;
    }

    private WebServer() throws MalformedURLException {
        super("http://path/to/server/");
    }
}
```
## Exchanging POJO objects with remote server
Let us define a simple POJO class whose objects we want to send to and receive from the server.
```
public class Greeting {
  private String greeting;

  public String getGreeting() {
    return greeting;
  }

  public void setGreeting(String greeting) {
    this.greeting = greeting;
  }
}
```
### Creating a HTTP task
Use the generic `HttpTask` class to instantiate a new asynchronous request. You have to define the type of object which you are requesting from the server and the type of payload (if any) as demonstrated in the code sample below.
```
HttpTask<Greeting, Greeting> httpTask =  
  new HttpTask.Builder<Greeting, Greeting>(Greeting.class) // type of object requested
  .setRequestUrl("file/on/server.php")                     // path of server file which will handle the request
  .setMethod(HttpMethod.POST)                              // type of request (default: GET)
  .create(WebServer.getInstance());                        // instance of receiving server
```

Each request may also optionally include a payload. A payload here is a POJO object which you want to send to the remote server.
```
Greeting iSaid =  new Greeting();
iSaid.setGreeting("Hello, Server!");
```

Call `HttpTask.Builder#setPayload(iSaid)` while creating the `HttpTask` object.

Note that requested object and payload do not need to be objects of same class.

### Receiving the requested object
Implement the `ResponseListener` interface to receive the requested object.
```
public class GreetingReceiver implements ResponseListener<Greeting> {
  @Override
  public void onRequestSuccessful(@NonNull Greeting greeting) {
    // Process response here
  }
  
  @Override
  public void onRequestFailed(@NonNull Exception ex) {
    // Handle errors here
  }
}
```
And finally ...
### Sending the request
```
GreetingReceiver receiver = new GreetingReceiver();
task.startAsync(receiver);
```
