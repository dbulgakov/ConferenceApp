package guru.myconf.conferenceapp.pojos.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import guru.myconf.conferenceapp.entities.Comment;

public class ConferenceComment {
    @SerializedName("comment_id")
    @Expose
    private int _commentId;

    @SerializedName("post_time")
    @Expose
    private Date _postTime;

    @SerializedName("comment_text")
    @Expose
    private String _text;


    @SerializedName("author")
    @Expose
    private User _author;

    public int getCommentId() {
        return _commentId;
    }

    public Date getPostTime() {
        return _postTime;
    }

    public String getText() {
        return _text;
    }

    public Comment convertToEntityComment() {
        guru.myconf.conferenceapp.entities.User tmpUser = new guru.myconf.conferenceapp.entities.User(_author.convertToEntityUser());
        guru.myconf.conferenceapp.entities.Comment tmpComment = new guru.myconf.conferenceapp.entities.Comment(_text, _postTime, tmpUser);
        return tmpComment;
    }
}
