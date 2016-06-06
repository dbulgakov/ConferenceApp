package guru.myconf.conferenceapp.pojos.Request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("username")
    private final String mUsername;

    @SerializedName("password")
    private final String mPassword;

    public LoginRequest(String username, String password) {
        mUsername = username;
        mPassword = password;
    }
}
