package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConferenceInfo {
    @SerializedName("conference_id")
    @Expose
    private int mId;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("description")
    @Expose
    private String mDescription;

    @SerializedName("city_name")
    @Expose
    private String mCity;

    @SerializedName("address")
    @Expose
    private String mAddress;

    @SerializedName("start_date")
    @Expose
    private Date mStartDate;

    @SerializedName("end_date")
    @Expose
    private Date mEndDate;

    @SerializedName("main_image")
    @Expose
    private int mMainImageId;

    @SerializedName("main_image_bigger")
    @Expose
    private int mMainImageBiggerId;


    @SerializedName("scheduled_speeches")
    @Expose
    private ArrayList<SpeechResponse> mSpeeches;


    private final SimpleDateFormat _dateFormatter = new SimpleDateFormat("dd.MM");

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getCity() {
        return mCity;
    }

    public String getDate() {
        return _dateFormatter.format(mStartDate) + " - " + _dateFormatter.format(mEndDate);
    }

    public String getBiggerImageUrl() {
        return "https://myconf.guru/api/images/" + mMainImageBiggerId;
    }

    public String getAddress() {
        return mAddress;
    }

    public ArrayList<SpeechResponse> getSpeeches() {
        return mSpeeches;
    }
}
