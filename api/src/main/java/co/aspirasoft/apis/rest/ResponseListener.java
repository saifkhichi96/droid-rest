package co.aspirasoft.apis.rest;

import android.support.annotation.NonNull;

/**
 * ResponseListener defines an interface for handling success and failure messages received from
 * the {@link HttpServer} in response to an {@link HttpRequest}.
 *
 * @author MuhammadSaifullah
 * @version 1.0
 */
public interface ResponseListener<Entity> {

    void onRequestSuccessful(@NonNull Entity response);

    void onRequestFailed(@NonNull Exception ex);
}