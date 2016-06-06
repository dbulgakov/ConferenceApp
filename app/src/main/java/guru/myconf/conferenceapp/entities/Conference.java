package guru.myconf.conferenceapp.entities;

public class Conference {
    private final int mId;
    private final String mTitle;
    private final String mDate;
    private final String mImageLink;

    public Conference(int id, String title, String date, String imageLink) {
        mId = id;
        mTitle = title;
        mDate = date;
        mImageLink = imageLink;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getImageLink() {
        return mImageLink;
    }
}
