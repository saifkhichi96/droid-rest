package co.aspirasoft.apis.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


final class HttpRequest<Response> {

    private final HttpServer httpServer;
    private final String targetUrl;
    private final Class<Response> responseType;

    @Nullable
    private ResponseListener<Response> responseListener;

    private String payload;

    private HttpMethod method = HttpMethod.GET;

    HttpRequest(@NotNull HttpServer httpServer, String targetUrl, @NotNull Class<Response> type) {
        this.httpServer = httpServer;
        this.targetUrl = targetUrl;
        this.responseType = type;
    }

    HttpMethod getMethod() {
        return method;
    }

    void setMethod(HttpMethod method) {
        this.method = method;
    }

    String getPayload() {
        return payload;
    }

    void setPayload(@NotNull Object payload) {
        try {
            ObjectMapper mapper = new MappingJackson2HttpMessageConverter().getObjectMapper();
            this.payload = mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    void sendRequest(@Nullable ResponseListener<Response> responseListener) {
        this.responseListener = responseListener;
        try {
            this.execute();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    protected final Response doInBackground() {
        Response response = null;
        try {
            response = httpServer.getObject(buildUrl(), this.responseType, this);
        } catch (Exception ex) {
            if (responseListener != null) {
                responseListener.onRequestFailed(ex);
            }
        }
        return response;
    }

    protected final void onPostExecute(@Nullable Response response) {
        if (responseListener != null) {
            try {
                if (response == null) {
                    throw new NullPointerException("Requested object not found.");
                }

                responseListener.onRequestSuccessful(response);
            } catch (Exception ex) {
                responseListener.onRequestFailed(ex);
            }
        }
    }

    private String buildUrl() {
        if (method == HttpMethod.GET && payload != null) {
            // Encode payload
            this.payload = URLEncoder.encode(payload);

            // Attach payload to URL
            return this.targetUrl + (this.targetUrl.contains("?")
                    ? "&payload=" + this.payload
                    : "?payload=" + this.payload);
        } else {
            return this.targetUrl;
        }
    }

    private void execute() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Response> future = executor.submit(this::doInBackground);

        Response response = future.get();
        onPostExecute(response);

        executor.shutdown();
    }

}