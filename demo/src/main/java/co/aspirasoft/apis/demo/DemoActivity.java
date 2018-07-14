package co.aspirasoft.apis.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.net.MalformedURLException;

import co.aspirasoft.apis.rest.HttpMethod;
import co.aspirasoft.apis.rest.HttpServer;
import co.aspirasoft.apis.rest.HttpTask;
import co.aspirasoft.apis.rest.ResponseListener;

public class DemoActivity extends AppCompatActivity implements ResponseListener<Greeting> {

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

    private void sendRequest(String method) {
        Greeting greeting = new Greeting();
        greeting.setSender(getString(R.string.app_name));
        greeting.setMessage("To be, or to not be?!");

        HttpTask<Greeting, Greeting> task = new HttpTask.Builder<Greeting, Greeting>(Greeting.class)
                .setMethod(method.equals("post") ? HttpMethod.POST : HttpMethod.GET)
                .setRequestUrl("demo-server-side.php")
                .setPayload(greeting)
                .create(httpServer);

        task.startAsync(this);
    }

    @Override
    public void onRequestSuccessful(@NonNull Greeting greeting) {
        TextView greetingIdText = findViewById(R.id.id_value);
        TextView greetingContentText = findViewById(R.id.content_value);

        greetingIdText.setText(greeting.getSender());
        greetingContentText.setText(greeting.getMessage());
    }

    @Override
    public void onRequestFailed(@NonNull Exception ex) {
        TextView greetingIdText = findViewById(R.id.id_value);
        TextView greetingContentText = findViewById(R.id.content_value);

        greetingIdText.setText(ex.getClass().getSimpleName());
        greetingContentText.setText(ex.getMessage());
    }
}
