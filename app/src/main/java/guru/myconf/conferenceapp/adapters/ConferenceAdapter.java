package guru.myconf.conferenceapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import guru.myconf.conferenceapp.R;
import guru.myconf.conferenceapp.entities.Conference;

public class ConferenceAdapter extends RecyclerView.Adapter<ConferenceAdapter.ViewHolder> {

    private ArrayList<Conference> _conferences;
    private Context _context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class ConferenceViewHolder extends ViewHolder {
        TextView conferenceName, conferenceDate;
        ImageView conferenceImage;

        public ConferenceViewHolder(View v) {
            super(v);
            conferenceName = (TextView) v.findViewById(R.id.conference_title);
            conferenceDate = (TextView) v.findViewById(R.id.conference_date);
            conferenceImage = (ImageView) v.findViewById(R.id.conference_image);
        }
    }

    public ConferenceAdapter(Context context, ArrayList<Conference> conferences) {
        _conferences = conferences;
        _context = context;

        Picasso.Builder builder = new Picasso.Builder(_context);
        builder.downloader(new OkHttpDownloader(_context,Integer.MAX_VALUE));
        Picasso picasso = builder.build();
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);
        Picasso.setSingletonInstance(picasso);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.conference_row, viewGroup, false);
        return new ConferenceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        ConferenceViewHolder holder = (ConferenceViewHolder) viewHolder;
        holder.conferenceName.setText(_conferences.get(position).getName());
        holder.conferenceDate.setText(_conferences.get(position).getDate());
        Picasso.with(_context).load(_conferences.get(position).getImageLink()).into(holder.conferenceImage);
    }

    @Override
    public int getItemCount() {
        return _conferences.size();
    }
}