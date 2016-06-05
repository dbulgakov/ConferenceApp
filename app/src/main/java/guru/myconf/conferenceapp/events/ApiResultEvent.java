package guru.myconf.conferenceapp.events;

public class ApiResultEvent {
    private Object mObject;

    public ApiResultEvent(Object object) {
        this.mObject = object;
    }

    public Object getResponse() {
        return mObject;
    }
}
