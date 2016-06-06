package guru.myconf.conferenceapp.entities;

public class Speech {
    private final int mId;
    private final String mTitle;
    private final String mAddress;
    private final String mDate;

    public Speech(int id, String title, String date, String address) {
        mId = id;
        mTitle = title;
        mAddress = address;
        mDate = date;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getDate() {
        return mDate;
    }
}
