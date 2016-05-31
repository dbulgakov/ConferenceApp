package guru.myconf.conferenceapp.entities;

import android.graphics.Bitmap;

public class Conference {
    private int _id;
    private String _name;
    private String _date;
    private String _imageLink;

    public Conference(int id, String name, String date, String imageLink) {
        _id = id;
        _name = name;
        _date = date;
        _imageLink = imageLink;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public String getDate() {
        return _date;
    }

    public String getImageLink() {
        return _imageLink;
    }
}
