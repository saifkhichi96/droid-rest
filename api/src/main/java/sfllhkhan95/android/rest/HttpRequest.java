package sfllhkhan95.android.rest;


import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import sfllhkhan95.android.rest.exception.HttpServerException;

public class HttpRequest extends AsyncTask<Object, Void, Object> {
    private final HttpServer httpServer;
    private ResponseHandler responseHandler;
    private HashMap<String, String> args;
    private String targetUrl;
    private Class dataType;
    private ViewGroup parent;

    public HttpRequest(HttpServer httpServer, String targetUrl) {
        this.httpServer = httpServer;
        this.targetUrl = targetUrl;

        args = new HashMap<>();
    }

    public void setResponseHandler(ResponseHandler handler) {
        this.responseHandler = handler;
    }

    public void addArgument(String key, String value) {
        value = value
                .replace("%", "%25")
                .replace("=", "3D")
                .replace(" ", "%20")
                .replace("\n", "%0A")
                .replace("+", "%2B")
                .replace("-", "%2D")
                .replace("#", "%23")
                .replace("&", "%26");

        key = key
                .replace("%", "%25")
                .replace("=", "3D")
                .replace(" ", "%20")
                .replace("\n", "%0A")
                .replace("+", "%2B")
                .replace("-", "%2D")
                .replace("#", "%23")
                .replace("&", "%26");

        args.put(key, value);
    }

    private String buildUrl() {
        String url = targetUrl;
        if (args.size() > 0) {
            url += "?";
            for (HashMap.Entry<String, String> entry : args.entrySet()) {
                url += entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        return url;
    }

    public void requestObject(Class<?> type, Object... params) {
        try {
            this.dataType = type;
            this.execute(params);
        } catch (IllegalStateException ignored) {

        }
    }

    public void requestObject(LayoutInflater inflater, ViewGroup parent, Class<?> type, Object... params) {
        this.parent = parent;
        inflater.inflate(R.layout.status, parent, true);

        try {
            this.dataType = type;
            this.execute(params);
        } catch (IllegalStateException ignored) {

        }
    }

    @Override
    protected Object doInBackground(Object... params) {
        try {
            return httpServer.getObject(buildUrl(), this.dataType);
        } catch (HttpServerException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        try {
            parent.removeView(parent.getChildAt(parent.getChildCount() - 1));
        } catch (NullPointerException ignored) {

        } finally {
            if (responseHandler != null) {
                responseHandler.execute(result);
            }
        }
    }
}