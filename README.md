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
  // other dependencies ...
  compile 'sfllhkhan95.android.rest:api:1.0.0'
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
### Creating a HTTP request
Use the generic `HttpRequest` class to instantiate a new request. You have to define the type of object which you are requesting from the server, as demonstrated in the code sample below.
```
HttpRequest<Greeting> request = new HttpRequest<>(
  WebServer.getInstance(),    // instance of receiving server
  "file/on/server.php",       // path of server file which will handle the request
  Greeting.class              // type of object requested
  );
```

Each request may also optionally include a payload. A payload here is a POJO object which you want to send to the webserver.
```
Greeting iSaid =  new Greeting();
iSaid.setGreeting("Hello, Server!");
request.setPayload(iSaid);
```
Note that requested object and payload do not need to be objects of same class.
### Receiving the requested object
Implement the `ResponseHandler` interface to receive the requested object.
```
public class GreetingReceiver implements ResponseHandler<Greeting> {
  @Override
  public void onResponseReceived(@Nullable Greeting greeting) {
    // App code here
  }
}
```
And finally ...
### Sending the request
```
GreetingReceiver receiver = new GreetingReceiver();
request.sendRequest(receiver);
```
