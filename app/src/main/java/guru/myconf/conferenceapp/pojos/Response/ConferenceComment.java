package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import guru.myconf.conferenceapp.entities.Comment;

public class ConferenceComment {
    @SerializedName("comment_id")
    @Expose
    private int mCommentId;

    @SerializedName("post_time")
    @Expose
    private Date mPostTime;

    @SerializedName("comment_text")
    @Expose
    private String mText;


    @SerializedName("author")
    @Expose
    private User mAuthor;

    public int getCommentId() {
        return mCommentId;
    }

    public Date getPostTime() {
        return mPostTime;
    }

    public String getText() {
        return mText;
    }

    public Comment convertToEntityComment() {
        guru.myconf.conferenceapp.entities.User tmpUser = new guru.myconf.conferenceapp.entities.User(mAuthor.convertToEntityUser());
        return new Comment(mText, mPostTime, tmpUser);
    }
}
