package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasicResponse {
    @SerializedName("success")
    @Expose
    private boolean mSuccessStatus;

    @SerializedName("message")
    @Expose
    private String mMessage;

    public boolean getSuccessStatus() {
        return mSuccessStatus;
    }

    public String getResponseMessage() {
        return mMessage;
    }
}
