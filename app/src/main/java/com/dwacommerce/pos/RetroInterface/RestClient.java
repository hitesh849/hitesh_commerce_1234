package com.dwacommerce.pos.RetroInterface;

import com.dwacommerce.pos.sharedPreferences.Config;
import com.dwacommerce.pos.utility.Constants;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Hitesh on 28-01-2017.
 */
public class RestClient {
    public static RestInterface restInterface;

    public static RestInterface getRestInterface() {
        if (restInterface == null) {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(120000, TimeUnit.MILLISECONDS);
            client.setReadTimeout(120000, TimeUnit.MILLISECONDS);
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(Config.getServerUrl())
                    .setClient(new OkClient(client))
                    .build();
            restInterface = adapter.create(RestInterface.class);
        }
        return restInterface;
    }

    public static RestInterface getRestInterfaceObject(String baseUrl) {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(120000, TimeUnit.MILLISECONDS);
        client.setReadTimeout(120000, TimeUnit.MILLISECONDS);
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setClient(new OkClient(client))
                .build();
        RestInterface   restInterface = adapter.create(RestInterface.class);

        return restInterface;
    }
}
