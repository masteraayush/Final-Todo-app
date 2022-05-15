package com.aayush.journeyjournal.dashboard;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.aayush.journeyjournal.MapsActivity;
import com.aayush.journeyjournal.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditDeleteActivity extends AppCompatActivity {

    //assigning variable for textview fields
    TextView jTitle, jThought, jLocation, jDate, edLocation;

    EditText editTitle;

    //assigning variable for imageview field
    ImageView jImage;

    //assigning variable for edit button
    FloatingActionButton btnEdit;

    //assigning variable for delete button
    FloatingActionButton btnDelete;

    //assigning variable for image button
    AppCompatImageButton editLocationBtn;

    //Firebase authentication variable
    FirebaseAuth firebaseAuth;

    //FirebaseFirestore authentication variable
    FirebaseFirestore firebaseFirestore;

    //firebase storage variable
    StorageReference storageReference;

    // intent code
    final static int UPDATE_RESULT_CODE = 1003;

    // Intent Request Code
    private final static int MAP_REQUEST_CODE = 4004;

    // check if data is updated or not
    private boolean isUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);

        //initializing textview variables with xml counterpart
        jTitle = findViewById(R.id.detailTitle);
        jThought = findViewById(R.id.detailJournalThoughts);
        jLocation = findViewById(R.id.detailLocation);
        jDate = findViewById(R.id.detailDate);

        //initializing imageview variable with xml counterpart
        jImage = findViewById(R.id.detailImage);

        //initializing edit floating action button variable
        btnEdit = findViewById(R.id.editJournalBtn);

        //initializing delete floating action button variable
        btnDelete = findViewById(R.id.deleteJournalBtn);

        //instantiating firebase variables
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //feeding data from cardview/recycler view into this activity
        Picasso.get().load(getIntent().getStringExtra("image")).into(jImage);
        jTitle.setText(getIntent().getStringExtra("title"));
        jThought.setText(getIntent().getStringExtra("thought"));
        jLocation.setText(getIntent().getStringExtra("location"));
        jDate.setText(getIntent().getStringExtra("date"));



        // on clicking edit floating action button
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Creating BottomSheetDialog
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EditDeleteActivity.this);
                bottomSheetDialog.setContentView(R.layout.bottom_edit_sheet);
                bottomSheetDialog.setCanceledOnTouchOutside(false);

                //Initializing and assigning variable
                String docID = getIntent().getStringExtra("documentID");
                ImageView edImg = bottomSheetDialog.findViewById(R.id.editImage);
                editTitle = bottomSheetDialog.findViewById(R.id.editJournalTitle);
                EditText edThoughts = bottomSheetDialog.findViewById(R.id.editJournalThought);
//                edLocation = bottomSheetDialog.findViewById(R.id.editLocation);
                TextView edDate = bottomSheetDialog.findViewById(R.id.editDate);
                Button saveBtnSheet = bottomSheetDialog.findViewById(R.id.saveBtnEdit);
                //initializing image button for editing location
//                editLocationBtn = bottomSheetDialog.findViewById(R.id.editLocationBtn);
               // Button editLocationBtn = bottomSheetDialog.findViewById(R.id.editAddLocation);

                //feeding data from the editdelete activity into the bottom dialog sheet
                assert edImg != null;
                Picasso.get().load(getIntent().getStringExtra("image")).into(edImg);
                assert editTitle != null;
                editTitle.setText(getIntent().getStringExtra("title"));
                assert edThoughts != null;
                edThoughts.setText(getIntent().getStringExtra("thought"));
//                assert edLocation != null;
                //edLocation.setText(getIntent().getStringExtra("location"));
                //assert edDate != null;//
                edDate.setText(getIntent().getStringExtra("date"));

                //shows the bottom modal sheet
                bottomSheetDialog.show();

                //creating an instance to load Maps activity to edit location
//                editLocationBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intentLoadMapEditer = new Intent(getApplicationContext(), MapsActivity.class);
//                        startActivityForResult(intentLoadMapEditer, MAP_REQUEST_CODE);
//                    }
//                });

                //creating an instance for save button to save all the edited changes
                saveBtnSheet.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        //sending updated/edited data to send data to the firebase firestore using Map
                        Map<String, Object> dataMap = new HashMap<>();
                        //feeding data under Map
                        dataMap.put("Title", editTitle.getText().toString());
                        dataMap.put("Thought", edThoughts.getText().toString());
//                        dataMap.put("location", edLocation.getText().toString());
                        dataMap.put("date", edDate.getText().toString());

                        String jT = editTitle.getText().toString();
                        String jTh = edThoughts.getText().toString();

                        //validating all fields
                        if(TextUtils.isEmpty(jT) && TextUtils.isEmpty(jTh)) {
                            editTitle.setError("Please provide your title.");
                            edThoughts.setError("Please provide your description.");
                        }

                        //title text view validation
                        if(TextUtils.isEmpty(jT)) {
                            editTitle.setError("Please provide your  title.");
                            return;
                        }

                        // thought text view validation
                        if(TextUtils.isEmpty(jTh)) {
                            edThoughts.setError("Please provide your description.");
                            return;
                        }

                        //updating data in the firebase firestore
                        DocumentReference documentReference = firebaseFirestore.collection("journalData").document(docID);
                        documentReference.update(dataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                bottomSheetDialog.dismiss();
                                jTitle.setText(editTitle.getText().toString());
                                jThought.setText(edThoughts.getText().toString());
//                                jLocation.setText(edLocation.getText().toString());
                                jDate.setText(edDate.getText().toString());
                                Toast.makeText(getApplicationContext(), "Update Successful!", Toast.LENGTH_SHORT).show();
                                isUpdated = true;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                bottomSheetDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        });


        // on clicking delete floating action button
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Initializing and assigning variable
                String docID = getIntent().getStringExtra("documentID");

                //alert dialog box that confirms if the user want to delete the particular item or not
                AlertDialog.Builder alertBld = new AlertDialog.Builder(EditDeleteActivity.this);
                alertBld.setTitle("Delete Confirmation");
                alertBld.setMessage("Are you sure you want to delete this item?");

                //setting YES and NO button
                alertBld.setPositiveButtonIcon(getDrawable(R.drawable.ic_delete));
                alertBld.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //fetching Data ID as document id
                        DocumentReference documentReference = firebaseFirestore.collection("journalData").document(docID);
                        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    int pos = getIntent().getIntExtra("pos", -1);
                                    if (pos != -1) {
                                        Log.d("activityRPos", Integer.toString(pos));
                                        Intent intent = new Intent();
                                        intent.putExtra("pos", pos);
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();

                                    }
                                    Toast.makeText(EditDeleteActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d("delete", task.getException().getLocalizedMessage());
                                }
                            }
                        });

                    }
                });

                alertBld.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                alertBld.show();

            }
        });


    }



//    public String GetLocation(Intent data) {
//        HashMap<String, String> location = (HashMap<String, String>)data.getSerializableExtra("location");
//        String countryName = location.get("countryName");
//        String adminArea = location.get("adminArea");
//        String subAdmin = location.get("subAdmin");
//        String locality = location.get("locality");
//        String selectedAddr = "";
//
//        if (locality != null && countryName != null) {
//            return locality+", "+countryName;
//        }
//        if (subAdmin != null && countryName != null) {
//            return subAdmin+", "+countryName;
//        }
//        if (adminArea != null && countryName != null) {
//            return adminArea+", "+countryName;
//        }
//        return selectedAddr;
//    }

    //getting URL of particular image that the user has opted to upload
        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            Log.d("activityRaddimg",Integer.toString(resultCode));

//            if(requestCode == MAP_REQUEST_CODE && data != null) {
//                Log.d("mapcode","adfasdf");
//                edLocation.setText(GetLocation(data));
//
//            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    @Override
    public void onBackPressed() {
        if (isUpdated) {
            int pos = getIntent().getIntExtra("pos", -1);
            Intent intent = new Intent();
            intent.putExtra("pos", pos);
            setResult(UPDATE_RESULT_CODE, intent);
            finish();
        }
        super.onBackPressed();
    }
}