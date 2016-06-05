package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse extends BasicResponse {

    public LoginResponse(){
        super();
    }

    @SerializedName("token")
    @Expose
    private String mAuthToken;

    public String getResponseToken() {
        return mAuthToken;
    }
}
