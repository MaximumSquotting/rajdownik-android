package com.example.barte_000.rajdownik;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class API {

    private static APIInterface apiInterface;
    private static String url = "http://192.168.0.8:3000/";
    public static String uid = "";
    public static String token = "";
    public static String client = "";

    public static APIInterface getClient() {
        if (apiInterface == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(
                            new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request().newBuilder()
                                            .addHeader("Accept", "application/json")
                                            .addHeader("uid", uid)
                                            .addHeader("client", client)
                                            .addHeader("access-token",token).build();
                                    return chain.proceed(request);
                                }
                            }).build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
            apiInterface = client.create(APIInterface.class);
        }
        return apiInterface;
    }


    public interface APIInterface {

        @FormUrlEncoded
        @POST("/confirm")
        Call<Registrations> sendIndexNumber(@Field("student_id") String indexNumber);

    }
}

