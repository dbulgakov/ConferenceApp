package guru.myconf.conferenceapp.pojos.Request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("username")
    private String mUsername;

    @SerializedName("password")
    private String mPassword;

    public LoginRequest(String username, String password) {
        mUsername = username;
        mPassword = password;
    }
}
