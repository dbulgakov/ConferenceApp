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

    private ArrayList<Speech> mSpeeches;
    private Context mContext;


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
        mSpeeches = speeches;
        mContext = context;
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

        holder.speechTitle.setText(mSpeeches.get(position).getTitle());
        holder.speechTime.setText("\nВремя проведения: " + mSpeeches.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mSpeeches.size();
    }

    public void addItems(ArrayList<Speech> speeches) {
        mSpeeches.addAll(speeches);
        notifyDataSetChanged();
    }

    public void removeItems() {
        mSpeeches = new ArrayList<>();
        notifyDataSetChanged();
    }
}