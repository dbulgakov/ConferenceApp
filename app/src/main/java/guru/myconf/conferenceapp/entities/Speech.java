package guru.myconf.conferenceapp.entities;

public class Speech {
    private int _id;
    private String _title;
    private String _address;
    private String _date;

    public Speech(int id, String title, String date, String address) {
        _id = id;
        _title = title;
        _address = address;
        _date = date;
    }

    public int getId() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

    public String getAddress() {
        return _address;
    }

    public String getDate() {
        return _date;
    }
}
