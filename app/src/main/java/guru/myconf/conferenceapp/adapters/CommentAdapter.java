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

    private ArrayList<Comment> _comments;
    private Context _context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class ConferenceViewHolder extends ViewHolder {
        TextView commentText, commentAuthor;

        public ConferenceViewHolder(View v) {
            super(v);
            commentText = (TextView) v.findViewById(R.id.comment_text);
            commentAuthor = (TextView) v.findViewById(R.id.comment_author);
        }
    }

    public CommentAdapter(Context context, ArrayList<Comment> speeches) {
        _comments = speeches;
        _context = context;
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

        holder.commentText.setText(_comments.get(position).getText());
        holder.commentAuthor.setText(_comments.get(position).getAuthor().getFullName());
    }

    @Override
    public int getItemCount() {
        return _comments.size();
    }

    public void addItems(ArrayList<Comment> comments) {
        _comments.addAll(comments);
        notifyDataSetChanged();
    }

    public void removeItems() {
        _comments = new ArrayList<>();
        notifyDataSetChanged();
    }
}