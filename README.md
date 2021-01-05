DroidREST is a Java library which provides an interface for client-side apps to exchange POJO objects with a web server.

This library can be used to write different type of client-side programs (Java applets, Android applications, Kotlin programs, etc). DroidREST uses JSON to exchange data between clients and server, and the server can be implemented in any language which can receive and interpret JSON data. An example with PHP is provided [here](demo-server-side.php).

## Features
1. Make HTTP GET requests to a remote server.
2. Make HTTP POST requests to a remote server.
3. Send POJO objects in HTTP requests.
4. Receive POJO objects in HTTP response.

## Release Notes
| Version | Release Notes | 
|---------|---------------|
| 2.1.0 | Converted formerly Android library to pure Java. |
| 2.0.1 | Deprecated classes removed. All network requests should be managed using the [HttpTask](droid-rest/src/main/java/co/aspirasoft/apis/rest/HttpTask.java) class.|
| Older versions | Support for older versions has been officially dropped. They are no longer available for use in Gradle.|

## Installation
1. Open the project level `build.gradle` file and add the following code:
```
buildscript {
    repositories {
        // ...
        maven() {
            url 'https://dl.bintray.com/saifkhichi96/maven/'
        }
    }
    // ...
}

allprojects {
    repositories {
        // ...
        maven() {
            url 'https://dl.bintray.com/saifkhichi96/maven/'
        }
    }
}
```
2. Open the module level `build.gradle` file and add dependency to this library using:
```
dependencies {
  // other dependencies ...
  implementation 'co.aspirasoft.apis:droid-rest:$latest_version'
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
    exclude 'META-INF/spring.handlers'
    exclude 'META-INF/spring.schemas'
    exclude 'META-INF/spring.tooling'
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
import co.aspirasoft.apis.rest.HttpServer;

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
### Creating an HTTP task
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
