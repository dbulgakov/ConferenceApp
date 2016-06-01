package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConferenceInfoResponse extends BasicResponse{
    @SerializedName("conference")
    @Expose
    private ConferenceInfo _conference;

    public ConferenceInfoResponse() {
        super();
    }

    public ConferenceInfo getConferenceInfo(){
        return _conference;
    };
}
