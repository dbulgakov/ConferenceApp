package guru.myconf.conferenceapp.pojos.Request;

import com.google.gson.annotations.SerializedName;

public class RegistrationRequest {
    @SerializedName("username")
    private String mUsername;

    @SerializedName("password")
    private String mPassword;

    @SerializedName("firstname")
    private String mFirstname;

    @SerializedName("lastname")
    private String mLastname;

    @SerializedName("email")
    private String mEmail;

    public RegistrationRequest(String userLogin, String userPassword,
                                String userFirstName, String userLastName, String userEmail) {
        mUsername = userLogin;
        mPassword = userPassword;
        mFirstname = userFirstName;
        mLastname = userLastName;
        mEmail = userEmail;
    }
}
