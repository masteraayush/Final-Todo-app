package com.aayush.journeyjournal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.aayush.journeyjournal.dashboard.DashboardMain;

public class Login extends AppCompatActivity {

    //setting variable for Login button
    Button login_button;

    //assigning variables for all edit text fields
    EditText aEmail, aPassword;

    //assigning variable to the progressbar
    ProgressBar pgLoginBar;

    //Firebase authentication variable
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing edittext variables to the xml counterpart
        aEmail = findViewById(R.id.email);
        aPassword = findViewById(R.id.pass);

        //initializing firebase authentication instance for current database from firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //initializing progress bar variable
        pgLoginBar = findViewById(R.id.progressBarLog);

        //initializing login button
        login_button = (Button) findViewById(R.id.loginButton);

        //creating an instance for loading dashboard after successful login
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // extracting email and password value
                String email = aEmail.getText().toString().trim();
                String password = aPassword.getText().toString().trim();

                //validation both fields
                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
                    aEmail.setError("Field cannot be empty");
                    aPassword.setError("Field cannot be empty");
                    return;
                }

                //validating data in email field
                if(TextUtils.isEmpty(email)) {
                    aEmail.setError("Please provide your email");
                    return;
                }

                //validating data in password field
                if(TextUtils.isEmpty(password)){
                    aPassword.setError("Please provide your password");
                    return;
                }

                //validating the length of password
                if(password.length() < 6){
                    aPassword.setError(" Invalid Password");
                    return;
                }

                pgLoginBar.setVisibility(View.VISIBLE);

                //authenticating the user

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
                            Intent intentLoadLoginDashboard = new Intent(getApplicationContext(), DashboardMain.class);
                            startActivity(intentLoadLoginDashboard);
                            finish();
                        }
                        else{
                            Toast.makeText(Login.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pgLoginBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        //referencing to the textview that is to be made clickable to load register activity
        TextView loginToRegister = (TextView) findViewById(R.id.register_here);

        //creating an instance for loading registration activity when clickable text is clicked
        loginToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadReg = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intentLoadReg);

            }
        });

        //referencing to the textview that is to be made clickable to load forgot password section
        TextView loginToPassReset = (TextView) findViewById(R.id.forgotPass);

        //creating an instance for loading forgot password section when clickable text is clicked
        loginToPassReset.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mailReset = new EditText(view.getContext());
                AlertDialog.Builder dialogPassReset = new AlertDialog.Builder(view.getContext());
                dialogPassReset.setTitle("Reset Password?");
                dialogPassReset.setMessage("Enter you Email to receive reset link:");
                dialogPassReset.setView(mailReset);

                //when user clicks on Yes for password reset
                dialogPassReset.setPositiveButton("Send Link", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // extracting user email and sending reset link to it
                        String resetMail = mailReset.getText().toString();

                        //when the reset link has been successfully sent OnSuccessListener shall be called
                        firebaseAuth.sendPasswordResetEmail(resetMail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Login.this, "Reset link has been sent to your Email.", Toast.LENGTH_SHORT).show();
                            }
                            //if the operation fails OnFailureListener shall be called
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error! Reset link cannot be sent. Please Try Again!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                //when user clicks on No for password reset
                dialogPassReset.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //closes the password reset dialog and redirecting user back to login activity
                    }
                });

                //displays the error dialog
                dialogPassReset.create().show();

            }
        }));
    }
}