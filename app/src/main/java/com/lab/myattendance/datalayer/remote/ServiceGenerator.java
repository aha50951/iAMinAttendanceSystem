package com.lab.myattendance.datalayer.remote;

import com.google.gson.Gson;
import com.lab.myattendance.BuildConfig;
import com.lab.myattendance.datalayer.ServiceApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * This class provide the BaseUrl for all the APIs, Initialize all the objects needed to create an Instance of Retrofit
 */

public class ServiceGenerator {
    public static final String Base_Url = "http://iamingu-001-site1.etempurl.com";
    private static ServiceGenerator serviceGenerator;
    private static Gson gson;
    private static OkHttpClient.Builder okHttpClient;
    private static Retrofit.Builder retrofit;

    /**
     * Initialize the Class Fields
     * */
    private ServiceGenerator() {
        gson = new Gson();
        okHttpClient = new OkHttpClient.Builder();
        retrofit = new Retrofit.Builder();
    }

    /**
     * Get a single object from this class in all the application
     * */
    public static ServiceGenerator getInstance() {
        if (serviceGenerator == null) {
            serviceGenerator = new ServiceGenerator();
        }
        return serviceGenerator;
    }

    /**
     * Configure the Retrofit and Return an object from the Implemented ServiceApi Object, and is used by all the ViewModels to call the APIs
     * */
    public static ServiceApi create() {
        okHttpClient.connectTimeout(120L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
//                .cache(new Cache(new File(app.getCacheDir(), "OKcache"), 25L))
                .networkInterceptors();

        if (BuildConfig.DEBUG) {
            final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            okHttpClient.addInterceptor(logging);
        }

        retrofit.baseUrl(Base_Url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        final Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build();

                return chain.proceed(request);
            }
        };

        okHttpClient.addInterceptor(interceptor);
        return retrofit.client(okHttpClient.build()).build().create(ServiceApi.class);
    }
}
