package com.aayush.journeyjournal.interfaces;

import android.view.View;

import com.aayush.journeyjournal.recyclerview.RecyclerDataModel;

public interface RecyclerViewClickEvent {
    void onRecyclerItemClicked(View view, int pos, RecyclerDataModel recyclerDataModel);
}
