package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SpeechResponse {
    @SerializedName("speech_id")
    @Expose
    private int mId;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("description")
    @Expose
    private String mDescription;

    @SerializedName("address")
    @Expose
    private String mAddress;

    @SerializedName("start_time")
    @Expose
    private Date mStartDate;

    @SerializedName("end_time")
    @Expose
    private Date mEndDate;

    @SerializedName("author")
    @Expose
    private User mAuthor;

    private final SimpleDateFormat mDateFormatter = new SimpleDateFormat("HH:mm");

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getDate() {
        return mDateFormatter.format(mStartDate) + " - " + mDateFormatter.format(mEndDate);
    }

    public User getAuthor() {
        return mAuthor;
    }
}
