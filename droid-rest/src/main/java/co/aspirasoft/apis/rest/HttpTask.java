package co.aspirasoft.apis.rest;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public final void startAsync(@NotNull ResponseListener<R> responseListener) {
        execute(responseListener);
    }

    public final void startAsync() {
        execute(null);
    }

    public static class Builder<Payload, Response> {

        private final HttpTask<Payload, Response> task = new HttpTask<>();

        public Builder(@NotNull Class<Response> responseType) {
            task.responseType = responseType;
        }

        public Builder<Payload, Response> setPayload(@NotNull Payload payload) {
            task.payload = payload;
            return this;
        }

        public Builder<Payload, Response> setRequestUrl(String requestUrl) {
            task.requestUrl = requestUrl;
            return this;
        }

        public Builder<Payload, Response> setMethod(@NotNull HttpMethod method) {
            task.method = method;
            return this;
        }

        public HttpTask<Payload, Response> build(@NotNull HttpServer server) {
            task.httpServer = server;
            return task;
        }

    }

}