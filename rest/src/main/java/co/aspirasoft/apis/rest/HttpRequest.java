package co.aspirasoft.apis.rest;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


final class HttpRequest<Response> extends AsyncTask<Void, Void, Response> {

    private final HttpServer httpServer;
    private final String targetUrl;
    private final Class<Response> responseType;

    @Nullable
    private ResponseListener<Response> responseListener;

    private String payload;

    private HttpMethod method = HttpMethod.GET;

    HttpRequest(@NonNull HttpServer httpServer, String targetUrl, @NonNull Class<Response> type) {
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

    void setPayload(@NonNull Object payload) {
        try {
            ObjectMapper mapper = new MappingJackson2HttpMessageConverter().getObjectMapper();
            this.payload = mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    void sendRequest(@Nullable ResponseListener<Response> responseListener) {
        this.responseListener = responseListener;
        this.execute();
    }

    @Nullable
    @Override
    protected final Response doInBackground(Void... params) {
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

    @Override
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
        if (method == HttpMethod.GET) {
            if (this.payload == null) {
                return this.targetUrl;
            }

            this.payload = this.payload
                    .replace("%", "%25")
                    .replace("=", "3D")
                    .replace(" ", "%20")
                    .replace("\n", "%0A")
                    .replace("+", "%2B")
                    .replace("-", "%2D")
                    .replace("#", "%23")
                    .replace("&", "%26");

            if (this.targetUrl.contains("?")) {
                return this.targetUrl + "&payload=" + this.payload;
            } else {
                return this.targetUrl + "?payload=" + this.payload;
            }
        } else {
            return this.targetUrl;
        }
    }

}