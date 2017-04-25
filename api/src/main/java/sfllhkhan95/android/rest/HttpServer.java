package sfllhkhan95.android.rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import sfllhkhan95.android.rest.exception.HttpServerException;

public class HttpServer {

    private final String address;

    public HttpServer(String address) {
        if (!address.endsWith("/")) {
            this.address = address + "/";
        } else {
            this.address = address;
        }
    }

    public static boolean hasNetworkAccess(Context ct) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    synchronized Object getObject(String path, Class<?> dataType) throws HttpServerException {
        if (this.address == null || this.address.equals("/")) {
            throw new HttpServerException();
        }

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate.getForObject(this.address + path, dataType);
    }

}