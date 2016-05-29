package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("success")
    @Expose
    private boolean _successStatus;

    @SerializedName("message")
    @Expose
    private String _message;

    @SerializedName("token")
    @Expose
    private String _authToken;

    public boolean getSuccessStatus() {
        return _successStatus;
    }

    public String getResponseMessage() {
        return _message;
    }

    public String getResponseToken() {
        return _authToken;
    }
}
