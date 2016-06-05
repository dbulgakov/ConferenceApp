package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConferenceResponse {
    @SerializedName("conference_id")
    @Expose
    private int mId;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("start_date")
    @Expose
    private Date mStartDate;

    @SerializedName("end_date")
    @Expose
    private Date mEndDate;

    @SerializedName("main_image")
    @Expose
    private int mImageId;

    private SimpleDateFormat mDateFormatter = new SimpleDateFormat("dd.MM");



    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDateFormatter.format(mStartDate) + " - " + mDateFormatter.format(mEndDate);
    }

    public int getImageId() {
        return mImageId;
    }
}
