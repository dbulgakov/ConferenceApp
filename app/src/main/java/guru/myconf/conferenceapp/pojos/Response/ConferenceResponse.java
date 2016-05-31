package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConferenceResponse {
    @SerializedName("conference_id")
    @Expose
    private int _id;

    @SerializedName("title")
    @Expose
    private String _title;

    @SerializedName("start_date")
    @Expose
    private Date _startDate;

    @SerializedName("end_date")
    @Expose
    private Date _endDate;

    @SerializedName("photos")
    @Expose
    private int _imageId;

    private SimpleDateFormat _dateFormatter = new SimpleDateFormat("dd.MM");



    public int getId() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

    public String getDate() {
        return _dateFormatter.format(_startDate) + " - " + _dateFormatter.format(_endDate);
    }

    public int getImageId() {
        return _imageId;
    }
}
