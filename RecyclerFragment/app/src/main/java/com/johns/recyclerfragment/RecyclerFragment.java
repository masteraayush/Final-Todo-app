package com.johns.recyclerfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerFragment extends Fragment {
    RecyclerView itemRecycler;
    ArrayList<RecyclerDataModel> recyclerDataModels;
    RecyclerFragAdapter fragAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        itemRecycler = view.findViewById(R.id.recycler_frag);
        recyclerDataModels = new ArrayList<>();

        //data
        recyclerDataModels.add(new RecyclerDataModel("MacBook M1 chip","$1249","https://m.media-amazon.com/images/I/81AkdxBCVpL._AC_SL1500_.jpg"));
        recyclerDataModels.add(new RecyclerDataModel("Samsung Neo Qled","$4497","https://m.media-amazon.com/images/I/913+l9CB6cL._AC_SL1500_.jpg"));
        recyclerDataModels.add(new RecyclerDataModel("Razer Cynosa Chroma Gaming Keyboard","$60","https://m.media-amazon.com/images/I/61WgmLgwuQL._AC_SL1023_.jpg"));
        recyclerDataModels.add(new RecyclerDataModel("Logitech C922x Pro Stream Webcam","$90","https://m.media-amazon.com/images/I/712xpaJPT6L._AC_SL1500_.jpg"));
        recyclerDataModels.add(new RecyclerDataModel("TOZO G1 Wireless Earbuds Bluetooth Gaming Headphones with Microphone High Sensitivity in-Ear Headset","$30","https://m.media-amazon.com/images/I/61L4ZajO81L._AC_SL1500_.jpg"));
        recyclerDataModels.add(new RecyclerDataModel("Flexispot Quick Install Standing Desk EC9 Electric Height Adjustable Desk for Home Office 48 x 24 Inches Sit Stand Desk Whole-Piece Desk Board VICI","$254","https://m.media-amazon.com/images/I/61CXDbm+c0S._AC_SL1500_.jpg"));
        recyclerDataModels.add(new RecyclerDataModel("CleverMade Collapsible Cooler Bag: Insulated Leakproof 50 Can Soft Sided Portable Cooler Bag for Lunch, Grocery Shopping, Camping and Road Trips, Heather Grey/Black","$50","https://m.media-amazon.com/images/I/A1iCconZOEL._AC_SL1500_.jpg"));
        recyclerDataModels.add(new RecyclerDataModel("DR.VIVA Smart Watches for Men, Outdoor GPS Smartwatch Fitness Activity Tracker with Heart Rate Monitor Blood Oxygen Sleep Monitor","$60","https://m.media-amazon.com/images/I/71R6HyRatyL._AC_SL1500_.jpg"));
        recyclerDataModels.add(new RecyclerDataModel("NETGEAR WiFi Router (R6230) - AC1200 Dual Band Wireless Speed (up to 1200 Mbps) | Up to 1200 sq ft Coverage & 20 Devices | 4 x 1G Ethernet and 1 x 2.0 USB ports","$100","https://m.media-amazon.com/images/I/51N7ymmoTiL._AC_SL1350_.jpg"));
        recyclerDataModels.add(new RecyclerDataModel("Toshiba Canvio Gaming 1TB Portable External Hard Drive USB 3.0, Black for PlayStation, Xbox, PC, & Mac - HDTX110XK3AA","$90","https://m.media-amazon.com/images/I/71uX++X8HvL._AC_SL1500_.jpg"));
        fragAdapter = new RecyclerFragAdapter(getContext(),recyclerDataModels );
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        itemRecycler.setLayoutManager(llm);
        itemRecycler.setAdapter(fragAdapter);
        return view;
        //return inflater.inflate(R.layout.fragment_recycler, container, false);
    }
}