package guru.myconf.conferenceapp.entities;

public class Conference {
    private int _id;
    private String _title;
    private String _date;
    private String _imageLink;

    public Conference(int id, String title, String date, String imageLink) {
        _id = id;
        _title = title;
        _date = date;
        _imageLink = imageLink;
    }

    public int getId() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

    public String getDate() {
        return _date;
    }

    public String getImageLink() {
        return _imageLink;
    }
}
