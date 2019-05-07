package ro.demo.castles.gitprojectsteps.okhttp.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ro.demo.castles.gitprojectsteps.R;

public class OkHttpActivity extends AppCompatActivity {

    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.okhttp_activity);

        mTextViewResult = findViewById(R.id.text_view_result);
        OkHttpClient client = new OkHttpClient();
        String url =  "https://reqres.in/api/users?page=2";

        Request request = new Request.Builder()
                .url(url)
                .build();

        //with enqueue we run our request on a background thread
         client.newCall(request).enqueue(new Callback() {

             @Override
             public void onFailure(Call call, IOException e) {
                 e.printStackTrace();
             }

             @Override
             public void onResponse(Call call, Response response) throws IOException {
                 if(response.isSuccessful()){
                     final String myResponse = response.body().string();

                     OkHttpActivity.this.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             mTextViewResult.setText(myResponse);
                         }
                     });
                 }
             }

         });
    }
}
