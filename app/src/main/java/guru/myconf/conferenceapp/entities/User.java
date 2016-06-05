package guru.myconf.conferenceapp.entities;

public class User {
    private String mFirstName;
    private String mLastName;
    private int mId;

    public User(User user) {
        mFirstName = user.getFirstName();
        mLastName = user.getLastName();
        user.mId = user.getId();
    }


    public User(String firstName, String lastName, int id){
        mFirstName = firstName;
        mLastName = lastName;
        mId = id;
    }

    private String getFirstName(){
        return mFirstName;
    }

    private String getLastName(){
        return mLastName;
    }

    private int getId(){
        return mId;
    }

    public String getFullName() {
        return mFirstName + " " + mLastName;
    }
}
