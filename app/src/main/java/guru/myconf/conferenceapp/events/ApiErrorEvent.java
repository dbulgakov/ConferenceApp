package guru.myconf.conferenceapp.events;

public class ApiErrorEvent {
    private Exception _exception;

    public ApiErrorEvent(Exception exception){
        _exception = exception;
    }

    public Exception getError(){
        return _exception;
    }
}
