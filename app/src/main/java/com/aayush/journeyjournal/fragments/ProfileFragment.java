package com.aayush.journeyjournal.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.aayush.journeyjournal.Login;
import com.aayush.journeyjournal.R;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    //assigning variables for all changeable text view fields
    TextView fullName, email;

    //assigning variable for user ID
    String userID;

    //assigning variable to the profile image that the user changes
    ShapeableImageView profilePic;

    //assigning variable to the floating action button that invokes the image upload process
    FloatingActionButton profileFAB;

    //assigning variable for logout button
    AppCompatButton logoutBtn;

    //Firebase authentication variable
    FirebaseAuth firebaseAuth;

    //FirebaseFirestore authentication variable
    FirebaseFirestore firebaseFirestore;

    ListenerRegistration listenerRegistration;

    //firebase storage variable
    StorageReference storageReference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //initializing button variable with xml counterpart
        logoutBtn = view.findViewById(R.id.logoutBtn);

        //initializing textview variables with xml counterpart
        fullName = view.findViewById(R.id.userProfileFN);
        email = view.findViewById(R.id.userProfileEM);

        //initializing imageview variable with xml counterpart
        profilePic = view.findViewById(R.id.profilePicture);

        //initializing floating action button variable with xml counterpart
        profileFAB = view.findViewById(R.id.editImageBtn);

        //instantiating firebase variables
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //retrieving user ID to display user picture
        StorageReference pRef = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        pRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePic);
            }
        });

        //retrieving user ID from the firebase
        userID = firebaseAuth.getCurrentUser().getUid();
        Log.d("UID", userID.toString());

        //retrieving data from the firestore and assigning them to the initialized variables
        DocumentReference dRef = firebaseFirestore.collection("users").document(userID);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });


        //creating snapshot listener for retrieving data
        listenerRegistration = dRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                //Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                fullName.setText(value.getString("fullName"));
                email.setText(value.getString("email"));
            }
        });


        //creating an instance to allow the user to change the profile image through image picker
        profileFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating new intent to open gallery
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);
            }
        });

        return view;

    }

    //getting URI of particular image that the user has opted to upload
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageURI = data.getData();



                //setting image into the imageview
               // profilePic.setImageURI(imageURI);

                //calling method for uploading image to firebase storage
                uploadImageToFirebase(imageURI);
            }
        }
    }

    //method that invokes uploading process to the firebase storage
    private void uploadImageToFirebase(Uri imageURI) {
        StorageReference fRef = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        //when image is uploaded successfully
        fRef.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // when uri is successfully retrieved from the firebase
                fRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilePic);
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


    // initializes the logout procedure
    public void logout() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Toast.makeText(getContext(), "User Logged Out", Toast.LENGTH_SHORT).show();
            listenerRegistration.remove();
            FirebaseAuth.getInstance().signOut(); // user gets logged out from the app
            //getParentFragmentManager().beginTransaction().remove(new ProfileFragment());
            startActivity(new Intent(getContext(), Login.class));
            requireActivity().finish();
        }
    }
}