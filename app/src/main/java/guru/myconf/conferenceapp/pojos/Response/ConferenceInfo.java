package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConferenceInfo {
    @SerializedName("conference_id")
    @Expose
    private int _id;

    @SerializedName("title")
    @Expose
    private String _title;

    @SerializedName("description")
    @Expose
    private String _description;

    @SerializedName("city_name")
    @Expose
    private String _city;

    @SerializedName("start_time")
    @Expose
    private Date _startDate;

    @SerializedName("end_time")
    @Expose
    private Date _endDate;

    @SerializedName("photos")
    @Expose
    private ArrayList<Integer> _photos;

    @SerializedName("scheduled_speeches")
    @Expose
    private ArrayList<SpeechResponse> _speeches;


    private SimpleDateFormat _dateFormatter = new SimpleDateFormat("dd.MM");

    public int getId() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

    public String getDescription() {
        return _description;
    }

    public String getCity() {
        return _city;
    }

    public String getDate() {
        return _dateFormatter.format(_startDate) + " - " + _dateFormatter.format(_endDate);
    }

    public ArrayList<Integer> getImageId() {
        return _photos;
    }

    public ArrayList<SpeechResponse> getSpeeches() {
        return _speeches;
    }
}
