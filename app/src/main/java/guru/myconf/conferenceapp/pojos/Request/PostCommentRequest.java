package guru.myconf.conferenceapp.pojos.Request;

import com.google.gson.annotations.SerializedName;

public class PostCommentRequest {
    @SerializedName("token")
    private final String mToken;

    @SerializedName("text")
    private final String mText;

    public PostCommentRequest(String token, String text) {
        mToken = token;
        mText = text;
    }
}
