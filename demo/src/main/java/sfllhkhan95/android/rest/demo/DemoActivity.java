package sfllhkhan95.android.rest.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import sfllhkhan95.android.rest.HttpRequest;
import sfllhkhan95.android.rest.HttpServer;
import sfllhkhan95.android.rest.ResponseHandler;

public class DemoActivity extends AppCompatActivity {

    private HttpServer httpServer = new HttpServer("http://rest-service.guides.spring.io/");
    private ResponseHandler responseHandler = new ResponseHandler() {
        @Override
        public void execute(Object response) {
            Greeting greeting = (Greeting) response;

            TextView greetingIdText = (TextView) findViewById(R.id.id_value);
            TextView greetingContentText = (TextView) findViewById(R.id.content_value);
            greetingIdText.setText(greeting.getId());
            greetingContentText.setText(greeting.getContent());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
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
            HttpRequest httpRequest = new HttpRequest(httpServer, "greeting");
            httpRequest.setResponseHandler(responseHandler);
            httpRequest.requestObject(
                    getLayoutInflater(),
                    (ViewGroup) findViewById(R.id.activity_demo),
                    Greeting.class
            );
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
