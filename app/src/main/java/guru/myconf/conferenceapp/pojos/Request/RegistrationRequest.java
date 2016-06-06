package guru.myconf.conferenceapp.pojos.Request;

import com.google.gson.annotations.SerializedName;

public class RegistrationRequest {
    @SerializedName("username")
    private final String mUsername;

    @SerializedName("password")
    private final String mPassword;

    @SerializedName("firstname")
    private final String mFirstname;

    @SerializedName("lastname")
    private final String mLastname;

    @SerializedName("email")
    private final String mEmail;

    public RegistrationRequest(String userLogin, String userPassword,
                                String userFirstName, String userLastName, String userEmail) {
        mUsername = userLogin;
        mPassword = userPassword;
        mFirstname = userFirstName;
        mLastname = userLastName;
        mEmail = userEmail;
    }
}
