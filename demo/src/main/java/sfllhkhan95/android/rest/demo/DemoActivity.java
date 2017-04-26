package sfllhkhan95.android.rest.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.MalformedURLException;

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
            httpServer = new HttpServer("http://10.0.3.2/libs/android");
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
        if (id == R.id.action_refresh) {
            sendRequest();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponseReceived(@Nullable Greeting entity) {
        TextView greetingIdText = (TextView) findViewById(R.id.id_value);
        TextView greetingContentText = (TextView) findViewById(R.id.content_value);
        greetingIdText.setText(entity.getSender());
        greetingContentText.setText(entity.getMessage());
    }

    private void sendRequest() {
        HttpRequest<Greeting> httpRequest = new HttpRequest<>(httpServer, "ping.php", Greeting.class);
        httpRequest.showStatus(getLayoutInflater(), (ViewGroup) findViewById(R.id.activity_demo));
        httpRequest.sendRequest(this);
    }
}
