package co.aspirasoft.apis.rest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 *
 */
public class HttpTask<P, R> {

    private HttpServer httpServer;
    private String requestUrl;
    private Class<R> responseType;

    @Nullable
    private P payload;
    private HttpMethod method;

    private HttpTask() {
        requestUrl = "";
        payload = null;
        method = HttpMethod.GET;
    }

    private HttpTask(String requestUrl, Class<R> responseType) {
        this.requestUrl = requestUrl;
        this.responseType = responseType;
        this.method = HttpMethod.GET;
    }

    private void execute(@Nullable ResponseListener<R> responseListener) {
        HttpRequest<R> request = new HttpRequest<>(httpServer, requestUrl, responseType);
        request.setMethod(method);
        if (payload != null) {
            request.setPayload(payload);
        }

        request.sendRequest(responseListener);
    }

    public final void startAsync(@NonNull ResponseListener<R> responseListener) {
        execute(responseListener);
    }

    public final void startAsync() {
        execute(null);
    }

    public static class Builder<Payload, Response> {

        private HttpTask<Payload, Response> task = new HttpTask<>();

        public Builder(@NonNull Class<Response> responseType) {
            task.responseType = responseType;
        }

        public Builder<Payload, Response> setPayload(@NonNull Payload payload) {
            task.payload = payload;
            return this;
        }

        public Builder<Payload, Response> setRequestUrl(String requestUrl) {
            task.requestUrl = requestUrl;
            return this;
        }

        public Builder<Payload, Response> setMethod(@NonNull HttpMethod method) {
            task.method = method;
            return this;
        }

        public HttpTask<Payload, Response> create(@NonNull HttpServer server) {
            task.httpServer = server;
            return task;
        }

    }

}