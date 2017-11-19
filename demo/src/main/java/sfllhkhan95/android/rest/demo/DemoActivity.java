package sfllhkhan95.android.rest.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.MalformedURLException;

import sfllhkhan95.android.rest.HttpMethod;
import sfllhkhan95.android.rest.HttpRequest;
import sfllhkhan95.android.rest.HttpServer;
import sfllhkhan95.android.rest.ResponseHandler;

public class DemoActivity extends AppCompatActivity implements ResponseHandler<Greeting> {

    private HttpServer httpServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        try {
            httpServer = new HttpServer("http://192.168.43.223/libs/droid-rest/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_get_request:
                sendRequest("get");
                return true;

            case R.id.action_post_request:
                sendRequest("post");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponseReceived(@Nullable Greeting entity) {
        TextView greetingIdText = (TextView) findViewById(R.id.id_value);
        TextView greetingContentText = (TextView) findViewById(R.id.content_value);

        if (entity != null) {
            greetingIdText.setText(entity.getSender());
            greetingContentText.setText(entity.getMessage());
        }
    }

    private void sendRequest(String method) {
        HttpRequest<Greeting> httpRequest = new HttpRequest<>(httpServer, "demo-server-side.php", Greeting.class);
        httpRequest.showStatus((ViewGroup) findViewById(R.id.activity_demo));

        Greeting greeting = new Greeting();
        greeting.setSender(getString(R.string.app_name));
        greeting.setMessage("To be, or to not be?!");
        httpRequest.setPayload(greeting);

        if (method.equals("get")) {
            httpRequest.setMethod(HttpMethod.GET);
        } else if (method.equals("post")) {
            httpRequest.setMethod(HttpMethod.POST);
        }

        httpRequest.sendRequest(this);
    }
}
