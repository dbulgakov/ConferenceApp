package guru.myconf.conferenceapp.entities;

public class User {
    private String _firstName;
    private String _lastName;
    private int _id;

    public User(String firstName, String lastName, int id){
        _firstName = firstName;
        _lastName = lastName;
        _id = id;
    }

    public String getFirstName(){
        return _firstName;
    }

    public String getLastName(){
        return _lastName;
    }

    public int getId(){
        return _id;
    }

    public String getFullName() {
        return _firstName + " " + _lastName;
    }
}
