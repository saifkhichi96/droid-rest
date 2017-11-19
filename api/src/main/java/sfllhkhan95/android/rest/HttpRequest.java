package sfllhkhan95.android.rest;


import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class HttpRequest<Entity> extends AsyncTask<Void, Void, Entity> {
    private final HttpServer httpServer;
    private final String targetUrl;
    private final Class<Entity> dataType;

    private ResponseHandler<Entity> responseHandler;
    private String payload;
    private ViewGroup statusContainer;

    private HttpMethod method = HttpMethod.GET;

    public HttpRequest(@NotNull HttpServer httpServer, String targetUrl, @NotNull Class<Entity> type) {
        this.httpServer = httpServer;
        this.targetUrl = targetUrl;
        this.dataType = type;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setPayload(@NotNull Object payload) {
        try {
            ObjectMapper mapper = new MappingJackson2HttpMessageConverter().getObjectMapper();
            this.payload = mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public String getPayload() {
        return payload;
    }

    public void showStatus(@NotNull ViewGroup parent) {
        this.statusContainer = parent;
        ViewGroup.inflate(parent.getContext(), R.layout.status, parent);
    }

    public void sendRequest(ResponseHandler<Entity> responseHandler) {
        this.responseHandler = responseHandler;
        this.execute();
    }

    @Nullable
    @Override
    protected final Entity doInBackground(Void... params) {
        Entity entity = null;
        try {
            entity = (Entity) httpServer.getObject(buildUrl(), this.dataType, this);
        } catch (Exception ex) {
            onExecuteFailed(ex);
        }

        return entity;
    }

    protected void onExecuteFailed(Exception ex) {

    }

    @Override
    protected final void onPostExecute(@Nullable Entity entity) {
        try {
            statusContainer.removeView(statusContainer.getChildAt(statusContainer.getChildCount() - 1));
        } catch (NullPointerException ignored) {

        } finally {
            if (responseHandler != null) {
                responseHandler.onResponseReceived(entity);
            }
        }
    }

    @Contract(pure = true)
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