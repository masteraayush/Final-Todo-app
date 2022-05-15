package com.aayush.journeyjournal;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.aayush.journeyjournal.dashboard.DashboardMain;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    //setting variable for Register Button
    Button register_button;

    //assigning variables for all edit text fields
    EditText aFullName, aEmail, aPassword, aConfPass;

    //assigning variable to the progressbar
    ProgressBar pgRegBar;

    //Firebase authentication variable
    FirebaseAuth firebaseAuth;

    //FirebaseFirestore authentication variable
    FirebaseFirestore firebaseFirestore;

    //assigning variable for user ID
    String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initializing edittext variables to the xml counterpart
        aFullName = findViewById(R.id.full_name);
        aEmail    = findViewById(R.id.reg_email);
        aPassword = findViewById(R.id.reg_pass);
        aConfPass = findViewById(R.id.confirm_pass);

        //initializing progress bar variable
        pgRegBar = findViewById(R.id.progressBarReg);

        //initializing firebase authentication instance for current database from firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //initializing firebasefirestore authentication instance for firestore database
        firebaseFirestore = FirebaseFirestore.getInstance();

        //initializing register button
        register_button = (Button) findViewById(R.id.registerBtn);

        //checking if the user is already logged in or not along with if the user has already created an account previously or not
        if(firebaseAuth.getCurrentUser() != null){
            Intent intentLoadRegDashboard = new Intent(getApplicationContext(), DashboardMain.class);
            startActivity(intentLoadRegDashboard);
            finish();
        }

        //creating an instance for loading dashboard after successful user registration
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //setting password pattern for error handling
                Pattern pass_pattern = Pattern.compile("^" +
                                                        "(?=.*[@#$%^&+=])" + // specifies atleast 1 special character
                                                        "(?=\\S+$)" +        // no spaces
                                                        ".{4,}" +            // at least four characters
                                                        "$");

                // extracting full name, email and password value
                String full_name = aFullName.getText().toString();
                String email = aEmail.getText().toString().trim();
                String password = aPassword.getText().toString().trim();
                String confirm_pass = aConfPass.getText().toString().trim();

                //validation all fields
                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(full_name) && TextUtils.isEmpty(confirm_pass)){
                    aFullName.setError("Field cannot be empty");
                    aEmail.setError("Field cannot be empty");
                    aPassword.setError("Field cannot be empty");
                    aConfPass.setError("Field cannot be empty");
                    return;
                }

                //validating data in full name field if any numbers are provided
                if(full_name.matches(".*\\d.*")){
                    aFullName.setError("Full name cannot have numeric value");
                    return;
                }

                //validating data in full name field
                if(TextUtils.isEmpty(full_name)){
                    aFullName.setError("Please provide your email");
                    return;
                }

                //validating data in email field
                if(TextUtils.isEmpty(email)) {
                    aEmail.setError("Please provide your email.");
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    aEmail.setError("Invalid email address");
                    return;
                }

                //validating data in password field
                if(TextUtils.isEmpty(password)){
                    aPassword.setError("Please set your password");
                    return;
                }

                //validating data in confirm password field
                if(TextUtils.isEmpty(confirm_pass)){
                    aConfPass.setError("Please match your set password");
                    return;
                }

                //validating the length of password
                if(password.length() < 6){
                    aPassword.setError("Password must be 6 or more characters");
                    return;
                }

                //validating password if it meets the password pattern or not
                if(!pass_pattern.matcher(password).matches()) {
                    aPassword.setError("Weak Password");
                    return;
                }

                //validating password and confirm password matches
                if(!password.equals(confirm_pass)){
                    aConfPass.setError("Password does not match");
                    return;
                }

                Toast.makeText(SignUp.this, "User Created", Toast.LENGTH_LONG).show();

                //displaying progress bar if all the input field are correct and the registration process has started
                pgRegBar.setVisibility(View.VISIBLE);

                //registering user in the firebase
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUp.this, "User Created", Toast.LENGTH_LONG).show();
                            Intent intentLoadRegDashboard = new Intent(getApplicationContext(), DashboardMain.class);
                            uID = firebaseAuth.getCurrentUser().getUid();

                            //storing data in firestore database to store all data entered in registration
                            DocumentReference dRef = firebaseFirestore.collection("users").document(uID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fullName",full_name);
                            user.put("email",email);

                            dRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void nVoid) {
                                    Log.d(TAG, "onSuccess: user Profile has been created successfully for" + uID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });

                            //after successful registration, directing user to the main dashboard activity
                            startActivity(intentLoadRegDashboard);
                        }
                        else{
                            Toast.makeText(SignUp.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pgRegBar.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });

        //referencing the textview that is to be made clickable to load login activity
//        TextView registerToLogin = (TextView) findViewById(R.id.login_here);
//
//        //creating an instance for loading login activity when clickable text is clicked
//        registerToLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intentLoadLogin = new Intent(getApplicationContext(), Login.class);
//                startActivity(intentLoadLogin);
//            }
//        });

    }
}