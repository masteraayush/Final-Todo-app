package com.aayush.journeyjournal.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.aayush.journeyjournal.R;
import com.aayush.journeyjournal.interfaces.RecyclerViewClickEvent;

import java.util.ArrayList;

public class RecyclerFragAdapter extends RecyclerView.Adapter<RecyclerFragAdapter.CustomViewHolder> {

    ArrayList<RecyclerDataModel> dataList;
    private RecyclerViewClickEvent recyclerViewClickEvent;
    Context ctx;

    public RecyclerFragAdapter(RecyclerViewClickEvent clickEvent,Context context, ArrayList<RecyclerDataModel> recyclerDataModels) {
        this.recyclerViewClickEvent = clickEvent;
        this.ctx = context;
        this.dataList = recyclerDataModels;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_cardview, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        //provides the position of the data present in the list
        RecyclerDataModel rdm = dataList.get(position);

        //setting data inside the holder to be displayed in the cardview
        holder.itemJournalTitle.setText(rdm.getJournalTitle());
//        holder.itemJournalLocation.setText(rdm.getJournalLocation());
        holder.itemJournalDate.setText(rdm.getJournalDate());
//        Log.d("ImageResource", rdm.getImgResource());
        Glide.with(ctx).load(rdm.getImgResource()).into(holder.itemImageView);


        //when a single cardview is clicked, a new activity loads up
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                recyclerViewClickEvent.onRecyclerItemClicked(view, holder.getAdapterPosition(), rdm);
//
            }

        });

    }

    public void UpdateAdapter(ArrayList<RecyclerDataModel> dataModel, int pos) {
        this.dataList = dataModel;
        notifyItemChanged(pos);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void removeItemAt(int pos) {
        dataList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, dataList.size());
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        TextView itemJournalTitle;
//        TextView itemJournalLocation;
        TextView itemJournalDate;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.journalImage);
            itemJournalTitle = itemView.findViewById(R.id.journalTitle);
//            itemJournalLocation = itemView.findViewById(R.id.journalLocation);
            itemJournalDate = itemView.findViewById(R.id.journalDate);
        }
    }
}
