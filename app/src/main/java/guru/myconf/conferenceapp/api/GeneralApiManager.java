package guru.myconf.conferenceapp.api;

import android.content.Context;

import guru.myconf.conferenceapp.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeneralApiManager {

    private Retrofit _retrofit;
    private Context _context;

    public GeneralApiManager(Context context) {
        _context = context;
        _retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.conferenceguru_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ApiUrlManager getApiService() {
        return _retrofit.create(ApiUrlManager.class);
    }
}
