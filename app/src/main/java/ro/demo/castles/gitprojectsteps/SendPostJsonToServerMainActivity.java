package ro.demo.castles.gitprojectsteps;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/*
* Android | Send “POST” JSON Data to Server
* HttpURLConnection client.
* REST service http://hmkcode.appspot.com/jsonservlet
*
* In SendPostJsonToServerMainActivity we need to :
* 1. check network connectivity
* 2. create a separate thread to perform HTTP request using AsyncTask
* 3. Build HTTP POST request
*
* Check link for results of post and log Info : http://hmkcode.appspot.com/post-json/index.html
* */
public class SendPostJsonToServerMainActivity extends AppCompatActivity {

    TextView tvIsConnected;
    EditText etName;
    EditText etCountry;
    EditText etTwitter;
    TextView tvResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvIsConnected = findViewById(R.id.tvIsConnected);
        etName   = findViewById(R.id.etName);
        etCountry= findViewById(R.id.etCountry);
        etTwitter= findViewById(R.id.etTwitter);
        tvResult = findViewById(R.id.tvResult);

        checkNetworkConnection();

    }
    public void send(View view){
        Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show();

        //perform HTTP POST request
        if(checkNetworkConnection()){
            new HTTPAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");
        }else{
            Toast.makeText(this,"Not Connected",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkNetworkConnection(){

        ConnectivityManager connectivityManager =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =  connectivityManager.getActiveNetworkInfo();
        boolean isConnected = false;

        if(networkInfo != null && (isConnected = networkInfo.isConnected())){
            // show "Connected" & type of networks "Wifi or Mobile"
            tvIsConnected.setText("Connected"+networkInfo.getTypeName());

            //change background color to red
            tvIsConnected.setBackgroundColor(0xFF7CCC26);
        }else{
            // show Not Connected
            tvIsConnected.setText("Not Connected");

            //change background color to green
            tvIsConnected.setBackgroundColor(0xFFFF0000);
        }

        return isConnected;
    }

    private String httpPost(String myUrl) throws IOException,JSONException{

        String result = "";
        URL url = new URL(myUrl);

        // 1. create the HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type","application/json; charset=utf-8");

        //2. build JSON object
        JSONObject jsonObject = buildJsonObject();

        //3. add JSON content to POST request body
        setPostRequestContent(conn,jsonObject);

        //4. make POST request to the given URL
        conn.connect();

        //5. return response message

        return  conn.getResponseMessage()+"";

    }
    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException{
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(SendPostJsonToServerMainActivity.class.toString(),jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();

    }

    private JSONObject buildJsonObject() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("name",etName.getText().toString());
        jsonObject.accumulate("country",etCountry.getText().toString());
        jsonObject.accumulate("twitter",etTwitter.getText().toString());

        return jsonObject;
    }
    //create a separate thread to perform HTTP request using AsyncTask
    private class HTTPAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            // params come from the execute() call: params[0] is the url

            try {

                try {
                    return httpPost(urls[0]);
                }catch (JSONException e) {
                    e.printStackTrace();
                    return "Error!";
                }
            }catch (IOException e) {
                    return "Unable to retrieve web page. URL may be invalid.";
            }
        }



        @Override
        protected void onPostExecute(String result) {
            tvResult.setText(result);

        }
    }



}
