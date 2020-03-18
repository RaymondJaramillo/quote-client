package edu.cnm.deepdive.quoteclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.quoteclient.BuildConfig;
import edu.cnm.deepdive.quoteclient.model.Quote;
import edu.cnm.deepdive.quoteclient.model.Source;
import io.reactivex.Single;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface QodService {

  @GET("quotes/random")
  Single<Quote> getRandom(@Header("Authorization") String oauthHeader);

  @GET("quotes")
  Single<List<Quote>> getAll(@Header("Authorization") String oauthHeader);

  @GET("sources")
  Single<List<Source>> gerAllSources(
      @Header("Authorization") String oauthHeader, @Query("includeNull") boolean includeNull);

  @POST("quotes")
  Single<Quote> post(@Header("Authorization") String oauthHeader, @Body Quote quote);

  static QodService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  class InstanceHolder {

    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final QodService INSTANCE;

    static {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .setDateFormat(TIMESTAMP_FORMAT)
          .create();
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(Level.BODY);
      OkHttpClient client = new OkHttpClient.Builder()
          .readTimeout(60, TimeUnit.SECONDS)
          .addInterceptor(interceptor)
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create(gson))
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .client(client)
          .baseUrl(BuildConfig.BASE_URL)
          .build();
      INSTANCE = retrofit.create(QodService.class);
    }
  }

}
