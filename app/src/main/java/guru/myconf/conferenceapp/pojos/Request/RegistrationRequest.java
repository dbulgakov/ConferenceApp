package guru.myconf.conferenceapp.pojos.Request;

import com.google.gson.annotations.SerializedName;

public class RegistrationRequest {
    @SerializedName("username")
    private String _username;

    @SerializedName("password")
    private String _password;

    @SerializedName("firstname")
    private String _firstname;

    @SerializedName("lastname")
    private String _lastname;

    @SerializedName("email")
    private String _email;

    public RegistrationRequest(String userLogin, String userPassword,
                                String userFirstName, String userLastName, String userEmail) {
        _username = userLogin;
        _password = userPassword;
        _firstname = userFirstName;
        _lastname = userLastName;
        _email = userEmail;
    }
}
