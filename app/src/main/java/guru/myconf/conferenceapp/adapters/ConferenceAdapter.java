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

    private ArrayList<Conference> mConferences;
    private Context mContext;
    private Picasso mPicasso;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ConferenceAdapter.OnConferenceSelected mClickListener;


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
            mPicasso = Picasso.with(mContext);
            mPicasso.setIndicatorsEnabled(false);
        }
    }

    public ConferenceAdapter(Context context, ArrayList<Conference> conferences,
                             OnConferenceSelected clickListener, SwipeRefreshLayout swipeRefreshLayout) {
        mConferences = conferences;
        mContext = context;
        mClickListener = clickListener;
        mSwipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.conference_row, viewGroup, false);
        return new ConferenceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final ConferenceViewHolder holder = (ConferenceViewHolder) viewHolder;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.OnConferenceSelected(mConferences.get(position).getId());
            }
        });
        mSwipeRefreshLayout.setRefreshing(true);
        holder.conferenceName.setText(mConferences.get(position).getTitle());
        holder.conferenceDate.setText(new StringBuilder().append(mContext.getString(R.string.conferencerow_date_string)).append(mConferences.get(position).getDate()).toString());
        mPicasso.load(mConferences.get(position).getImageLink())
                .fit()
                .into(holder.conferenceImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError() {
                        Log.v("Picasso Error","Error while fetching image");
                        Log.v("Picasso Error", mConferences.get(position).getImageLink());
                        mPicasso.load(R.drawable.error_image).into(holder.conferenceImage);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mConferences.size();
    }

    public void addItems(ArrayList<Conference> conferences) {
        mConferences.addAll(conferences);
        notifyDataSetChanged();
    }

    public void removeItems() {
        mConferences = new ArrayList<>();
        notifyDataSetChanged();
    }

    public interface OnConferenceSelected {
        void OnConferenceSelected(int conferenceId);
    }
}