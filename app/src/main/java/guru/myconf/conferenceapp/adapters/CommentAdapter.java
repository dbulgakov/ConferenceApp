package guru.myconf.conferenceapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import guru.myconf.conferenceapp.R;
import guru.myconf.conferenceapp.entities.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> mComments;
    private Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class ConferenceViewHolder extends ViewHolder {
        TextView commentText, commentAuthor, commentTime;

        public ConferenceViewHolder(View v) {
            super(v);
            commentText = (TextView) v.findViewById(R.id.comment_text);
            commentAuthor = (TextView) v.findViewById(R.id.comment_author);
            commentTime = (TextView) v.findViewById(R.id.comment_time);
        }
    }

    public CommentAdapter(Context context, ArrayList<Comment> speeches) {
        mComments = speeches;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_row, viewGroup, false);
        return new ConferenceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        ConferenceViewHolder holder = (ConferenceViewHolder) viewHolder;

        holder.commentText.setText(mComments.get(position).getText());
        holder.commentAuthor.setText(mContext.getString(R.string.comment_author_separator) + mComments.get(position).getAuthor().getFullName());
        holder.commentTime.setText(mComments.get(position).getDateString());
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public void addItems(ArrayList<Comment> comments) {
        mComments.addAll(comments);
        notifyDataSetChanged();
    }

    public void removeItems() {
        mComments = new ArrayList<>();
        notifyDataSetChanged();
    }
}