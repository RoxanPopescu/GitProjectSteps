package ro.demo.castles.gitprojectsteps;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/* We will build an app that send HTTP GET request and display the response.
*
* */
public class ConnectHttpUrlConnectionActivity extends AppCompatActivity {
    TextView tvIsConnected;
    TextView tvResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_http_url_connection_activity);

        tvIsConnected = findViewById(R.id.txvIsConnected);
        tvResult      = findViewById(R.id.txvResult);

        if(checkNetworkConnection()){
            new HTTPAsyncTask().execute("http://hmkcode.appspot.com/rest/controller/get.json");
                    //"http://hmkcode.appspot.com/rest/controller/get.json");
                    //("http://hmkcode.com/examples/index.php");
        }
    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager =
                              (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = false;
        if(networkInfo != null && (isConnected = networkInfo.isConnected())){
            tvIsConnected.setText("Connected "+networkInfo.getTypeName());
            tvIsConnected.setBackgroundColor(0xFF7CCC26);
        }else{
            tvIsConnected.setText(" Not Connected");
            tvIsConnected.setBackgroundColor(0xFFFF0000);
        }
      return isConnected;
    }

    private String HttpGet(String myUrl) {//throws IOException

        InputStream inputStream = null;
        String result ="";


        try{
            //create a new url object by passing url string to the constructor
            URL url = new URL(myUrl);

            //create HttpURLConnection
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();

            //make GET request to the given URL
            conn.connect();

            //receive response as inputStream
            inputStream = conn.getInputStream();

            //convert inputStream to string
            if(inputStream!=null){
                result = convertInputStreamToString(inputStream);
            }else{
                result = "Did not work!";
            }

        }catch(IOException e){
            Log.d("InputStream",e.getLocalizedMessage());
        }
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws  IOException{

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine())!= null){
            result+=line;
        }
        inputStream.close();
        return result;
    }

    private class HTTPAsyncTask extends AsyncTask<String,Void, String>{


        // displays the result of the AsyncTask
        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getBaseContext(),"Received",Toast.LENGTH_LONG).show();
            try {
                JSONObject json = new JSONObject(result);
                String str = "";
                JSONArray articles = json.getJSONArray("articleList");
                str+= "articles length ="+articles.length();
                str+="\n----------------\n";
                //str+="names : "+articles.getJSONObject(0).names();
                str+="title : "+articles.getJSONObject(0).get("title");
                str+="\n----------------\n";
                str+="url : "+articles.getJSONObject(0).getString("url");
                str+="\n----------------\n";
                str+="categories : "+articles.getJSONObject(0).getString("categories");
                str+="\n----------------\n";
                str+="tags : "+articles.getJSONObject(0).getString("tags");


                tvResult.setText(str);
            }catch (JSONException e){
                e.printStackTrace();
            }
//            tvResult.setText(result);
        }

        @Override
        protected String doInBackground(String... urls) {
            return HttpGet(urls[0]);
        }
    }


}
