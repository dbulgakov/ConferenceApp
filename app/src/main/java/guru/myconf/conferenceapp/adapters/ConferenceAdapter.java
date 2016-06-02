package guru.myconf.conferenceapp.adapters;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import guru.myconf.conferenceapp.R;
import guru.myconf.conferenceapp.entities.Conference;

public class ConferenceAdapter extends RecyclerView.Adapter<ConferenceAdapter.ViewHolder> {

    private ArrayList<Conference> _conferences;
    private Context _context;
    private Picasso _picasso;
    private SwipeRefreshLayout _swipeRefreshLayout;
    private ConferenceAdapter.OnConferenceSelected _clickListener;


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

            // Initializing Picasso
            _picasso = Picasso.with(_context);
            _picasso.setIndicatorsEnabled(false);
        }
    }

    public ConferenceAdapter(Context context, ArrayList<Conference> conferences,
                             OnConferenceSelected clickListener, SwipeRefreshLayout swipeRefreshLayout) {
        _conferences = conferences;
        _context = context;
        _clickListener = clickListener;
        _swipeRefreshLayout = swipeRefreshLayout;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _clickListener.OnConferenceSelected(_conferences.get(position).getId());
            }
        });
        _swipeRefreshLayout.setRefreshing(true);
        holder.conferenceName.setText(_conferences.get(position).getTitle());
        holder.conferenceDate.setText(new StringBuilder().append(_context.getString(R.string.conferencerow_date_string)).append(_conferences.get(position).getDate()).toString());
        _picasso.load(_conferences.get(position).getImageLink())
                .fit()
                .into(holder.conferenceImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        _swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError() {
                        Log.v("Picasso Error","Error while fetching image");
                        Log.v("Picasso Error",_conferences.get(position).getImageLink());
                        _swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return _conferences.size();
    }

    public void addItems(ArrayList<Conference> conferences) {
        _conferences.addAll(conferences);
        notifyDataSetChanged();
    }

    public void removeItems() {
        _conferences = new ArrayList<>();
        notifyDataSetChanged();
    }

    public interface OnConferenceSelected {
        void OnConferenceSelected(int conferenceId);
    }
}