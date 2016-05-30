package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasicResponse {
    @SerializedName("success")
    @Expose
    private boolean _successStatus;

    @SerializedName("message")
    @Expose
    private String _message;

    public boolean getSuccessStatus() {
        return _successStatus;
    }

    public String getResponseMessage() {
        return _message;
    }
}
