package ro.demo.castles.gitprojectsteps;

import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;


public class netTest {

    @Test
    public void net() throws IOException {

        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("Hey there you found me!"));
        server.start();

        HttpUrl baseUrl = server.url("/api/hey");
        String bodyOfRequest = sendGetRequest(new OkHttpClient(),baseUrl);
        Assert.assertEquals("Hey there you found me!",bodyOfRequest);

    }

    private String sendGetRequest(OkHttpClient okHttpClient, HttpUrl base) throws IOException{

        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),"hy there");

        okhttp3.Request request = new okhttp3.Request.Builder()
                .post(body)
                .url(base)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();

    }
}
