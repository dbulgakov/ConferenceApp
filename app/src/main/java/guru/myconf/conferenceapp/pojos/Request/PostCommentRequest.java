package guru.myconf.conferenceapp.pojos.Request;

import com.google.gson.annotations.SerializedName;

public class PostCommentRequest {
    @SerializedName("token")
    private String _token;

    @SerializedName("text")
    private String _text;

    public PostCommentRequest(String token, String text) {
        _token = token;
        _text = text;
    }
}
