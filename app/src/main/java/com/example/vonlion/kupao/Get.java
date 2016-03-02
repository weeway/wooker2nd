package com.example.vonlion.kupao;

/**
 * Created by hbs on 2016/1/7.
 */

import java.io.IOException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class Get {

    OkHttpClient client = new OkHttpClient();
    String loginb(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
           return response.body().string();

        }
        else {
            throw new IOException("Unexpected code " + response);
        }

    }
}
