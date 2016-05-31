package guru.myconf.conferenceapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import guru.myconf.conferenceapp.R;
import guru.myconf.conferenceapp.entities.Conference;

public class ConferenceAdapter extends RecyclerView.Adapter<ConferenceAdapter.ViewHolder> {

    private ArrayList<Conference> _conferences;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class ConferenceViewHolder extends ViewHolder {
        TextView conferenceName;
        TextView conferenceDate;

        public ConferenceViewHolder(View v) {
            super(v);
            this.conferenceName = (TextView) v.findViewById(R.id.conference_title);
            this.conferenceDate = (TextView) v.findViewById(R.id.conference_date);
        }
    }


    public ConferenceAdapter(ArrayList<Conference> conferences) {
        _conferences = conferences;
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
    }

    @Override
    public int getItemCount() {
        return _conferences.size();
    }
}