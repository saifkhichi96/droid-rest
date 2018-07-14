package co.aspirasoft.apis.rest;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 *
 */
public abstract class HttpTask<Payload, Response> {

    private final String requestUrl;
    private final Class<Response> responseType;

    @Nullable
    private Payload payload;
    private HttpMethod method;

    protected HttpTask(String requestUrl, Class<Response> responseType) {
        this.requestUrl = requestUrl;
        this.responseType = responseType;
        this.method = HttpMethod.GET;
    }

    @CallSuper
    protected void setPayload(@NonNull Payload payload) {
        this.payload = payload;
    }

    protected void setMethod(@NonNull HttpMethod method) {
        this.method = method;
    }

    public final void startAsync(HttpServer httpServer, ResponseListener<Response> responseListener) {
        HttpRequest<Response> request = new HttpRequest<>(httpServer, requestUrl, responseType);
        request.setMethod(method);
        if (payload != null) {
            request.setPayload(payload);
        }

        request.sendRequest(responseListener);
    }

}