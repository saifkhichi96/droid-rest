package co.aspirasoft.apis.rest;

import org.jetbrains.annotations.NotNull;

/**
 * ResponseListener defines an interface for handling success and failure messages received from
 * the {@link HttpServer} in response to an {@link HttpRequest}.
 *
 * @author MuhammadSaifullah
 * @version 1.0
 */
public interface ResponseListener<@NotNull Entity> {

    void onRequestSuccessful(@NotNull Entity response);

    void onRequestFailed(@NotNull Exception ex);
}