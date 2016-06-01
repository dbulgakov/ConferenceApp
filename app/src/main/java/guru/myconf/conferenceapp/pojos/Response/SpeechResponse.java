package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @SerializedName("start_date")
    @Expose
    private Date _startDate;

    @SerializedName("end_date")
    @Expose
    private Date _endDate;

    @SerializedName("author")
    @Expose
    private SpeechAuthor _author;

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

    public String getAddress() {
        return _address;
    }

    public String getDate() {
        return _dateFormatter.format(_startDate) + " - " + _dateFormatter.format(_endDate);
    }

    public SpeechAuthor getAuthor() {
        return _author;
    }
}

class SpeechAuthor{

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

}
