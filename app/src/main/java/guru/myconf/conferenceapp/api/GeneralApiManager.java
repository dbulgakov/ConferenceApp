package guru.myconf.conferenceapp.api;

import android.content.Context;

import guru.myconf.conferenceapp.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeneralApiManager {

    private final Retrofit mRetrofit;

    public GeneralApiManager(Context context) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.conference_guru_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ApiUrlManager getApiService() {
        return mRetrofit.create(ApiUrlManager.class);
    }
}
