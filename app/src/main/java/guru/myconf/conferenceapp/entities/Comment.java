package guru.myconf.conferenceapp.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
    private final String mText;
    private final Date mDate;
    private final User mAuthor;

    private final SimpleDateFormat mDateFormatter = new SimpleDateFormat("d MMMM HH:mm");

    public Comment(String text, Date date, User user){
        mText = text;
        mDate = date;
        mAuthor = user;
    }

    public String getText(){
        return mText;
    }

    public String getDateString(){
        return mDateFormatter.format(mDate);
    }

    public User getAuthor()
    {
        return mAuthor;
    }
}
