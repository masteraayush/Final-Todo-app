package com.johns.recyclerfragment;

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

import java.util.ArrayList;

public class RecyclerFragAdapter extends RecyclerView.Adapter<RecyclerFragAdapter.CustomViewHolder> {

    ArrayList<RecyclerDataModel> dataList;
    Context ctx;
    public RecyclerFragAdapter(Context context, ArrayList<RecyclerDataModel> recyclerDataModels) {
        this.ctx = context;
        this.dataList = recyclerDataModels;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_rec, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        RecyclerDataModel rdm = dataList.get(position);
        holder.itemTitleView.setText(rdm.getItemTitle());
        holder.itemPriceView.setText(rdm.getItemPrice());
        Log.d("ImageURL",rdm.getImgUrl());
        Glide.with(ctx).load(rdm.getImgUrl()).into(holder.itemImageView);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        TextView itemTitleView;
        TextView itemPriceView;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.img_view);
            itemTitleView = itemView.findViewById(R.id.tv_title);
            itemPriceView = itemView.findViewById(R.id.tv_price);
        }
    }

}
