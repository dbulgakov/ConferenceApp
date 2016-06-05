package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    @Expose
    private int _id;

    @SerializedName("first_name")
    @Expose
    private String _first_name;

    @SerializedName("last_name")
    @Expose
    private String _last_name;

    @SerializedName("photo_id")
    @Expose
    private int _photo_id;

    public int getId() {
        return _id;
    }

    public String getFirstName() {
        return _first_name;
    }

    public String getLastName() {
        return _last_name;
    }

    public int getPhotoId() {
        return _id;
    }

    public guru.myconf.conferenceapp.entities.User convertToEntityUser() {
        guru.myconf.conferenceapp.entities.User tmpUser = new guru.myconf.conferenceapp.entities.User(_first_name, _first_name, _id);
        return tmpUser;
    }
}
