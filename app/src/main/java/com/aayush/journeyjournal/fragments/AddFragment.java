package com.aayush.journeyjournal.fragments;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.aayush.journeyjournal.MapsActivity;
import com.aayush.journeyjournal.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddFragment extends Fragment {

    //declaring image variable for imageview
    ImageView addDayImage;

    //declaring edittext variable for journal title and thoughts
    EditText title, thought;

    //declaring textview variable for setting location
    TextView location;

    // declaring floating action button for uploading/click image
    FloatingActionButton fabImageAddBtn;

    // declaring button for saving journal entry
    Button saveBtn;

    // declaring button for setting maps
    ImageButton locationAddBtn;

    //Firebase authentication variable
    FirebaseAuth firebaseAuth;

    //FirebaseFirestore authentication variable
    FirebaseFirestore firebaseFirestore;

    ListenerRegistration listenerRegistration;

    //firebase storage variable
    StorageReference storageReference;

    //
    Uri filePath;

    //
    Bitmap bitmap;

    // Intent Request Code
    private final static int MAP_REQUEST_CODE = 4004;

    private String imgURI = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        //initializing edit text variable with the xml counterpart
        title = view.findViewById(R.id.Title);
        thought = view.findViewById(R.id.Thoughts);

//        //initializing text view variable with the xml counterpart
//        location = view.findViewById(R.id.jLocation);

        //initializing image view variable with the xml counterpart
        addDayImage = view.findViewById(R.id.addImageView);

        //initializing floating action button with the xml counterpart
        fabImageAddBtn = view.findViewById(R.id.fabAddImage);

//        //initializing image button with the xml counterpart
//        locationAddBtn = view.findViewById(R.id.addLocation);

        //instantiating firebase variables
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        // for save button to direct the user to the home where the saved journal entries are presented
        saveBtn = view.findViewById(R.id.saveBtn);

        //browsing the image through gallery invoked using button onClick method
        fabImageAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setting up permission that invokes when the add image fab is clicked
                Dexter.withActivity((Activity) getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        //when the permission is granted to access storage
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Please select an Image"), 1);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

            }
        });

//        //creating an instance to load Maps activity to set location
//        locationAddBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intentLoadMapSetter = new Intent(getContext(), MapsActivity.class);
//                startActivityForResult(intentLoadMapSetter, MAP_REQUEST_CODE);
//            }
//        });

        // creating an instance for toast message that is to be displayed on button click
        saveBtn.setOnClickListener(view1 -> {

            //extracting journal title and thought
            String jTitle = title.getText().toString();
            String jThought = thought.getText().toString();
//            String jLocation = location.getText().toString();

            SimpleDateFormat dateFormater = new SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date();
            String finalDate = dateFormater.format(date);

            DocumentReference documentReference = firebaseFirestore.collection("journalData").document();

            Map<String, Object> data = new HashMap<>();
            data.put("Title", jTitle);
            data.put("Thought", jThought);

            if(!imgURI.isEmpty()){
                data.put("image", imgURI);
            }

//            data.put("date", finalDate);
//            data.put("location", jLocation);


//            //validating all fields
//            if(imgURI.isEmpty() && TextUtils.isEmpty(jTitle) && TextUtils.isEmpty(jThought) && TextUtils.isEmpty(jLocation)){
//                title.setError("Field cannot be left empty");
//                thought.setError("Field cannot be left empty");
//                location.setError("Field cannot be left empty");
//                Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
//                return;
//            }

            //journal title text view validation
            if(TextUtils.isEmpty(jTitle)) {
                title.setError("Please provide your journal title.");
                return;
            }

            //journal description text view validation
            if(TextUtils.isEmpty(jTitle)) {
                thought.setError("Please provide your journal description.");
                return;
            }

//            //journal location validation
//            if(TextUtils.isEmpty(jLocation)){
//                location.setError("Please set a location.");
//                return;
//            }

            documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "onSuccess: data uploaded successfully");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.toString());
                }
            });

            //calling upload function to upload all the provided data to the firebase
            if(!imgURI.isEmpty()) {
                uploadDataToFirebase();
                //on clicking save button
                Toast.makeText(getContext(), "Your entry has been saved.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }

    //data uploading function
    private void uploadDataToFirebase() {

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Data Upload Progress");
        progressDialog.show();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference sRef = firebaseStorage.getReference().child("image1/ab.png");
        Log.d("imgURIFilepath",filePath.toString());
        sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float uploadPercent = (100 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded: " + (int)uploadPercent + "%");
            }
        });
    }

    //retrieving location from the Maps Activity
    public String GetLocation(Intent data) {
        HashMap<String, String> location = (HashMap<String, String>)data.getSerializableExtra("location");
        String countryName = location.get("countryName");
        String adminArea = location.get("adminArea");
        String subAdmin = location.get("subAdmin");
        String locality = location.get("locality");
        String selectedAddr = "";

        if (locality != null && countryName != null) {
            return locality+", "+countryName;
        }
        if (subAdmin != null && countryName != null) {
            return subAdmin+", "+countryName;
        }
        if (adminArea != null && countryName != null) {
            return adminArea+", "+countryName;
        }
        return selectedAddr;
    }

    //getting URL of particular image that the user has opted to upload
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("activityRaddimg",Integer.toString(resultCode));
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Log.d("activityRaddimg","after if");
            filePath = data.getData();

            try {
                uploadImageToFirebase(filePath);
            }catch (Exception exp){

            }
        }
        if(requestCode == MAP_REQUEST_CODE && data != null) {
            Log.d("mapcode","adfasdf");
            location.setText(GetLocation(data));

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //method that invokes uploading process to the firebase storage
    private void uploadImageToFirebase(Uri imageURI) {

        SimpleDateFormat dateFormater = new SimpleDateFormat("MM-dd-yyyy-hh-mm-ss");
        Date date = new Date();
        String finalDate = dateFormater.format(date);

        StorageReference fRef = storageReference.child("journalImage/"+ finalDate +"-journal.jpg");
        //when image is uploaded successfully
        fRef.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // when uri is successfully retrieved from the firebase
                fRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(addDayImage);
                        imgURI = uri.toString();
                        Log.d("imageURI",imgURI);
                    }
                });
            }
            // when image upload fails
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }
}