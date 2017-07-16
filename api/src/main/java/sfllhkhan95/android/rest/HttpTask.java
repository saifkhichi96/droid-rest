package sfllhkhan95.android.rest;

import android.widget.RelativeLayout;

/**
 *
 */
public abstract class HttpTask<Payload, Response> {

    private final String requestUrl;
    private final Class<Response> responseType;
    private Payload payload;

    protected HttpTask(String requestUrl, Class<Response> responseType) {
        this.requestUrl = requestUrl;
        this.responseType = responseType;
    }

    public void startAsync(HttpServer httpServer, ResponseHandler<Response> responseHandler, RelativeLayout parent) {
        HttpRequest<Response> request = new HttpRequest<>(httpServer, requestUrl, responseType);
        request.setPayload(payload);
        if (parent != null) {
            request.showStatus(parent);
        }
        request.sendRequest(responseHandler);
    }

    public void startAsync(HttpServer httpServer, ResponseHandler<Response> responseHandler) {
        HttpRequest<Response> request = new HttpRequest<>(httpServer, requestUrl, responseType);
        request.setPayload(payload);
        request.sendRequest(responseHandler);
    }

    protected void setPayload(Payload payload) {
        this.payload = payload;
    }
}
