package guru.myconf.conferenceapp.events;

public class ApiErrorEvent {
    private Exception mException;

    public ApiErrorEvent(Exception exception){
        mException = exception;
    }

    public Exception getError(){
        return mException;
    }
}
