package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ConferencesResponse extends BasicResponse{
    ConferencesResponse() {
        super();
    }

    @SerializedName("conferences")
    @Expose
    private ArrayList<ConferenceResponse> mConferences;

    public ArrayList<ConferenceResponse> getResponseConferences() {
        return mConferences;
    }
}
