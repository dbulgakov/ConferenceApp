package guru.myconf.conferenceapp.entities;

import android.graphics.Bitmap;

public class Conference {
    private int _id;
    private String _name;
    private String _date;
    private Bitmap _image;

    public Conference(int id, String name, String date, Bitmap image) {
        _id = id;
        _name = name;
        _date = date;
        _image = image;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _date;
    }

    public String getDate() {
        return _date;
    }

    public Bitmap getImage() {
        return _image;
    }
}
