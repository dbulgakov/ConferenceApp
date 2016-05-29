package guru.myconf.conferenceapp.pojos.Request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("username")
    private String _username;

    @SerializedName("password")
    private String _password;

    public LoginRequest(String username, String password) {
        _username = username;
        _password = password;
    }
}
