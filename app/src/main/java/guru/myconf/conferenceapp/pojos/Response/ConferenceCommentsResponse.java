package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ConferenceCommentsResponse extends BasicResponse{
    public ConferenceCommentsResponse() {
        super();
    }

    @SerializedName("comments")
    @Expose
    private ArrayList<ConferenceComment> mComments;

    public ArrayList<ConferenceComment> getResponseComments() {
        return mComments;
    }
}
