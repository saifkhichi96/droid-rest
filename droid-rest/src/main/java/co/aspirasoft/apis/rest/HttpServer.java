package co.aspirasoft.apis.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HttpServer {

    private final String address;

    /**
     * Default public constructor.
     *
     * @param address HTTP address of the server
     * @throws MalformedURLException exception thrown if given address is invalid
     */
    public HttpServer(@NotNull String address) throws MalformedURLException {
        // Append backslash to the URL if not already added
        this.address = !address.endsWith("/") ? address + "/" : address;

        // Verify URL correctness
        try {
            new URL(address);
            if (address.equals("/")) {
                throw new MalformedURLException();
            }
        } catch (MalformedURLException ex) {
            String message = "Invalid HTTP address";
            throw new MalformedURLException(message);
        }
    }

    private String getQuery(List<AbstractMap.SimpleEntry<String, String>> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (AbstractMap.SimpleEntry<String, String> pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    /**
     * Returns content of the web document.
     *
     * @return content of the web document
     */

    synchronized <T> T getObject(String file, Class<? extends T> dataType, HttpRequest<T> httpRequest) throws IOException {
        // Establish a new HTTP connection with the remote web server
        HttpURLConnection connection = connect(this.address + file);

        if (httpRequest.getMethod() == HttpMethod.POST) {
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            List<AbstractMap.SimpleEntry<String, String>> params = new ArrayList<>();
            if (httpRequest.getPayload() != null) {
                params.add(new AbstractMap.SimpleEntry<>("payload", httpRequest.getPayload()));
            }

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();
        }

        // Read the remote file and build an input stream from the received data
        InputStream inputStream = new BufferedInputStream(connection.getInputStream());

        // Read the whole content of the remote file and save it locally
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String content = s.hasNext() ? s.next() : "";

        // Terminate the HTTP connection with the remote web server
        connection.disconnect();

        ObjectMapper mapper = new MappingJackson2HttpMessageConverter().getObjectMapper();
        return mapper.readValue(content, dataType);
    }

    /**
     * Establishes an HTTP connection with the remote web server and requests the a remote web
     * document at the given path.
     *
     * @param address http address of the web document to connect to
     * @return an instance of HttpURLConnection established with the requested web document
     * @throws IOException thrown if there is an error communicating with the web server
     */
    private HttpURLConnection connect(String address) throws IOException {
        URL url = new URL(address);
        return (HttpURLConnection) url.openConnection();
    }

}