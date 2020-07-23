package com.kipchulovidovi.lovidovi.networking;

import android.content.Context;

import com.kipchulovidovi.lovidovi.utils.Constants;
import com.kipchulovidovi.lovidovi.utils.SharedPreferencesConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient mInstance;
    private static final String BaseUrl = Constants.BASE_URL;
    private Retrofit retrofit;
    private RetrofitClient(Context context) {
        final String accessToken;
        if(new SharedPreferencesConfig(context).isloggedIn()){
            accessToken=new SharedPreferencesConfig(context).readClientsAccessToken();

        }else{
            accessToken="";
        }
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder new_request = request.newBuilder()
                                .addHeader("Authorization","Bearer "+accessToken);
                        return chain.proceed(new_request.build());
                    }
                });
        retrofit=new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static  synchronized RetrofitClient getInstance(Context context){
        if(mInstance==null){
            mInstance=new RetrofitClient(context);
        }
        return mInstance;
    }

    public JsonPlaceHolderInterface getApiConnector(){
        return retrofit.create(JsonPlaceHolderInterface.class);
    }

}
