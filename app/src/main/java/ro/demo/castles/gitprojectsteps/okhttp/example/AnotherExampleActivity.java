package ro.demo.castles.gitprojectsteps.okhttp.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ro.demo.castles.gitprojectsteps.R;

public class AnotherExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.another_exemple_activity);

        final TextView jsonTextView = findViewById(R.id.json_text_view_id);
        String jsonURL = getResources().getString(R.string.jsonURL);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(jsonURL)
                .build();
        //call to get the json
        Call call = client.newCall(request);
        //enqueue the call to a background thread
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(AnotherExampleActivity.this,
                        getResources().getString(R.string.network_failed),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final  String json = response.body().string();
                 AnotherExampleActivity.this.runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         jsonTextView.setText(json);
                     }
                 });
            }
        });

    }
}
