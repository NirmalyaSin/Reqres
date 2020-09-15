package com.reqres.retrofit;

import com.reqres.ReqresApplication;
import com.reqres.screens.utils.AppConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit retrofit;



    public static RetrofitService getAPIClient() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(AppConstants.BASE_URL)
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RetrofitService.class);
    }

    private static OkHttpClient getHttpClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .cache(new Cache(ReqresApplication.getInstance().getCacheDir(),
                        10 * 1024 * 1024)) // 10 MB
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .connectTimeout(40, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor);

        return builder.build();
    }

    /*private static class AddHeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {

            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("Accept", "application/json");
            return chain.proceed(builder.build());
        }
    }*/
}
