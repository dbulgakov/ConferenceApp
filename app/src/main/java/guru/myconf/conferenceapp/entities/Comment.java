package guru.myconf.conferenceapp.entities;

import java.util.Date;

public class Comment {
    private String _text;
    private Date _date;
    private User _author;

    public Comment(String text, Date date, User user){
        _text = text;
        _date = date;
        _author = user;
    }

    public String getText(){
        return _text;
    }

    public Date getDate(){
        return _date;
    }

    public User getAuthor()
    {
        return _author;
    }
}
