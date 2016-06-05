package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    @Expose
    private int mId;

    @SerializedName("first_name")
    @Expose
    private String mFirstName;

    @SerializedName("last_name")
    @Expose
    private String mLastName;

    @SerializedName("photo_id")
    @Expose
    private int mPhotoId;

    public int getId() {
        return mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public int getPhotoId() {
        return mId;
    }

    public guru.myconf.conferenceapp.entities.User convertToEntityUser() {
        return new guru.myconf.conferenceapp.entities.User(mFirstName, mLastName, mId);
    }
}
