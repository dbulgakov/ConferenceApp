package guru.myconf.conferenceapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import guru.myconf.conferenceapp.R;
import guru.myconf.conferenceapp.entities.Speech;

public class SpeechAdapter extends RecyclerView.Adapter<SpeechAdapter.ViewHolder> {

    private ArrayList<Speech> _speeches;
    private Context _context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class ConferenceViewHolder extends ViewHolder {
        TextView speechTitle, speechTime;

        public ConferenceViewHolder(View v) {
            super(v);
            speechTitle = (TextView) v.findViewById(R.id.speech_title);
            speechTime = (TextView) v.findViewById(R.id.speech_time);
        }
    }

    public SpeechAdapter(Context context, ArrayList<Speech> speeches) {
        _speeches = speeches;
        _context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.speech_row, viewGroup, false);
        return new ConferenceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        ConferenceViewHolder holder = (ConferenceViewHolder) viewHolder;

        holder.speechTitle.setText(_speeches.get(position).getTitle());
        holder.speechTime.setText("\nВремя проведения: " + _speeches.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return _speeches.size();
    }

    public void addItems(ArrayList<Speech> speeches) {
        _speeches.addAll(speeches);
        notifyDataSetChanged();
    }

    public void removeItems() {
        _speeches = new ArrayList<>();
        notifyDataSetChanged();
    }
}