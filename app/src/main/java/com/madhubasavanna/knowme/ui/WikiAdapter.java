package com.madhubasavanna.knowme.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madhubasavanna.knowme.R;
import com.madhubasavanna.knowme.wikipediadata.WikiUserData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WikiAdapter extends RecyclerView.Adapter<WikiAdapter.WikiViewHolder> {
    private Context context;
    private List<WikiUserData> wikiDataList;
    private WikiAdapterListener wikiAdapterListener;

    public WikiAdapter(Context context, List<WikiUserData> data, WikiAdapterListener listener){
        this.context = context;
        this.wikiDataList = data;
        this.wikiAdapterListener = listener;
    }

    interface WikiAdapterListener{
        void onWikiDataSelected(WikiUserData wikiUserData);
        void onFollowBtnClicked(WikiUserData wikiUserData);
    }

    @NonNull
    @Override
    public WikiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_suggestion_item_view, parent, false);

        return new WikiViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WikiViewHolder holder, int position) {
        final WikiUserData wikiUserData = wikiDataList.get(position);

        holder.title.setText(wikiUserData.getName());
        Picasso.with(context)
                .load(wikiUserData.getImageUrl()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return wikiDataList.size();
    }

    class WikiViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        TextView title;
        Button button;
        public WikiViewHolder(View view){
            super(view);
            thumbnail = view.findViewById(R.id.profile_photo);
            title = view.findViewById(R.id.title);
            button = view.findViewById(R.id.add_btn);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wikiAdapterListener.onWikiDataSelected(wikiDataList.get(getAdapterPosition()));
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wikiAdapterListener.onFollowBtnClicked(wikiDataList.get(getAdapterPosition()));
                }
            });
        }
    }
}
