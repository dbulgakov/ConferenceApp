package guru.myconf.conferenceapp.events;

public class ApiResultEvent {
    private Object _object;

    public ApiResultEvent(Object _object) {
        this._object = _object;
    }

    public Object getResponse() {
        return _object;
    }
}
