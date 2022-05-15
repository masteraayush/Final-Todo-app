package com.aayush.journeyjournal.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.aayush.journeyjournal.R;
import com.aayush.journeyjournal.dashboard.EditDeleteActivity;
import com.aayush.journeyjournal.interfaces.RecyclerViewClickEvent;
import com.aayush.journeyjournal.recyclerview.RecyclerDataModel;
import com.aayush.journeyjournal.recyclerview.RecyclerFragAdapter;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements RecyclerViewClickEvent {
    RecyclerView itemRecycler;
    ArrayList<RecyclerDataModel> recyclerDataModels;
    RecyclerFragAdapter fragAdapter;
    FirebaseFirestore firebaseFirestore;
    Context ctx;
    // intent code
    final static int DELETE_REQUEST_CODE = 1001;
    final static int UPDATE_REQUEST_CODE = 1002;
    final static int UPDATE_RESULT_CODE = 1003;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ctx = getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        itemRecycler = view.findViewById(R.id.home_RecyclerView);
        recyclerDataModels = new ArrayList<>();

        firebaseFirestore.collection("journalData").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String documentID = documentSnapshot.getId();
                        String imageURL = documentSnapshot.getString("image");
                        String title = documentSnapshot.getString("Title");
                        String location = documentSnapshot.getString("location");
                        String date = documentSnapshot.getString("date");
                        String thought = documentSnapshot.getString("Thought");
                        recyclerDataModels.add(new RecyclerDataModel(documentID, imageURL, title, location, date, thought));
                    }

                    fragAdapter = new RecyclerFragAdapter(HomeFragment.this, getContext(), recyclerDataModels);
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    itemRecycler.setLayoutManager(llm);
                    itemRecycler.setAdapter(fragAdapter);
                }
            }
        });

        //data
//        recyclerDataModels.add(new RecyclerDataModel("https://free4kwallpapers.com/uploads/originals/2015/10/01/beautiful-paris.jpg","A fine sunset in Paris", "Paris", "02/11/2022"));
//        recyclerDataModels.add(new RecyclerDataModel("https://images.unsplash.com/photo-1564507592333-c60657eea523?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8dGFqJTIwbWFoYWx8ZW58MHx8MHx8&w=1000&q=80","A day in Agra", "Agra", "02/11/2022"));
//        recyclerDataModels.add(new RecyclerDataModel("https://www.ft.com/__origami/service/image/v2/images/raw/http%3A%2F%2Fcom.ft.imagepublish.upp-prod-eu.s3.amazonaws.com%2Fd26908ec-49c6-11ea-aee2-9ddbdc86190d?fit=scale-down&source=next&width=700","Such a beautiful sunset in Brazil", "Sao Paulo", "02/11/2022"));


        return view;
    }

    public void UpdateAdapterData(int pos) {
        ArrayList<RecyclerDataModel> updatedRecyclerDataModels = new ArrayList<>();
        firebaseFirestore.collection("journalData").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String documentID = documentSnapshot.getId();
                        String imageURL = documentSnapshot.getString("image");
                        String title = documentSnapshot.getString("Title");
                        String location = documentSnapshot.getString("location");
                        String date = documentSnapshot.getString("date");
                        String thought = documentSnapshot.getString("Thought");
                        updatedRecyclerDataModels.add(new RecyclerDataModel(documentID, imageURL, title, location, date, thought));
                    }
                    fragAdapter.UpdateAdapter(updatedRecyclerDataModels, pos);
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == DELETE_REQUEST_CODE && data != null && resultCode == RESULT_OK) {
            int pos = data.getIntExtra("pos", -1);
            if (pos != -1) {
                fragAdapter.removeItemAt(pos);
            }

        }
        if (resultCode == UPDATE_RESULT_CODE && data != null) {
            int pos = data.getIntExtra("pos", -1);
            UpdateAdapterData(pos);
            Toast.makeText(getContext(), Integer.toString(data.getIntExtra("pos",-1)), Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRecyclerItemClicked(View view, int pos, RecyclerDataModel recyclerDataModel) {
        Toast.makeText(ctx, "Item clicked", Toast.LENGTH_SHORT).show();
        //creating an intent to load a new activity
        Intent intent = new Intent(ctx, EditDeleteActivity.class);
        intent.putExtra("documentID", recyclerDataModel.getJournalID());
        intent.putExtra("image", recyclerDataModel.getImgResource());
        intent.putExtra("title", recyclerDataModel.getJournalTitle());
        intent.putExtra("thought", recyclerDataModel.getJournalThought());
        intent.putExtra("location", recyclerDataModel.getJournalLocation());
        intent.putExtra("date", recyclerDataModel.getJournalDate());
        intent.putExtra("pos", pos);

        //load editdelete activity
        startActivityForResult(intent, DELETE_REQUEST_CODE);
    }
}