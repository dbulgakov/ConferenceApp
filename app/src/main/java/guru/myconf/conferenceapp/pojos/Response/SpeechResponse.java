package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SpeechResponse {
    @SerializedName("speech_id")
    @Expose
    private int _id;

    @SerializedName("title")
    @Expose
    private String _title;

    @SerializedName("description")
    @Expose
    private String _description;

    @SerializedName("address")
    @Expose
    private String _address;

    @SerializedName("start_time")
    @Expose
    private Date _startDate;

    @SerializedName("end_time")
    @Expose
    private Date _endDate;

    @SerializedName("author")
    @Expose
    private User _author;

    private SimpleDateFormat _dateFormatter = new SimpleDateFormat("HH:mm");

    public int getId() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

    public String getDescription() {
        return _description;
    }

    public String getAddress() {
        return _address;
    }

    public String getDate() {
        return _dateFormatter.format(_startDate) + " - " + _dateFormatter.format(_endDate);
    }

    public User getAuthor() {
        return _author;
    }
}
