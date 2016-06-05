package guru.myconf.conferenceapp.events;

public class ApiPostCommentError extends ApiErrorEvent{
    public ApiPostCommentError(Exception exception){
        super(exception);
    }
}
