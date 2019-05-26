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
import com.madhubasavanna.knowme.userdata.UserPreferences;
import com.madhubasavanna.knowme.wikipediadata.WikiUserData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserPreferenceAdapter extends RecyclerView.Adapter<UserPreferenceAdapter.MyViewHolder> {
    private Context context;
    private List<WikiUserData> preferenceList;
    private UserPreferenceListener userPreferenceListener;

    public UserPreferenceAdapter(Context context,List<WikiUserData> data, UserPreferenceListener listener){
       this.context = context;
       this.preferenceList = data;
       this.userPreferenceListener = listener;
    }

    public void setData(List<WikiUserData> data){
        this.preferenceList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_preference_item_view, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(preferenceList.get(position).getName());
        holder.thumbnail.setImageBitmap(preferenceList.get(position).getThumbnail());
    }

    @Override
    public int getItemCount() {
        return preferenceList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        Button unfollowBtn;
        public MyViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.title);
            unfollowBtn = view.findViewById(R.id.unfollow_btn);
            thumbnail = view.findViewById(R.id.thumbnail);

            unfollowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WikiUserData data = preferenceList.get(getAdapterPosition());
                    userPreferenceListener.OnUserPreferenceChange(new UserPreferences(data.getPageId()
                            ,data.getName(),data.convertTobyte(),data.getImageUrl()));
                }
            });
        }
    }

    public interface UserPreferenceListener{
        public void OnUserPreferenceChange(UserPreferences data);
    }
}
