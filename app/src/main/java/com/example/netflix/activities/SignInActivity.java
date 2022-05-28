package com.example.netflix.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix.databinding.ActivitySignInBinding;
import com.example.netflix.mainscreens.MainScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    FirebaseAuth auth;
    String useremail, password, userid, firstname, lastname, contactnumber;
    FirebaseFirestore firestore;
    DocumentReference reference;
    Date today, ending;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        Calendar calendar = Calendar.getInstance();
        today = calendar.getTime();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Signing In Please Wait...");
        getSupportActionBar().hide();
        binding.buttonsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                useremail = binding.emailedittext.getText().toString().trim();
                password = binding.passwordedittext.getText().toString().trim();
                if (!(useremail.length() == 0) && !(password.length() == 0)) {
                    dialog.show();
                    auth.signInWithEmailAndPassword(useremail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                userid = auth.getCurrentUser().getUid();
                                reference = firestore.collection("Users").document(userid);
                                reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        ending = documentSnapshot.getDate("Ending");
                                        firstname = documentSnapshot.getString("Firstname");
                                        lastname = documentSnapshot.getString("Lastname");
                                        contactnumber = documentSnapshot.getString("Contact Number");
                                        assert ending != null;
                                        if (ending.compareTo(today) >= 0) {
                                            start(2000);
                                            Toast.makeText(SignInActivity.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
//                                        binding.progressbarsignin.setVisibility(View.VISIBLE);
                                        } else {
                                            Toast.makeText(SignInActivity.this, "Plan Expired!!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignInActivity.this, PaymentOverdue.class);
                                            intent.putExtra("userid", userid);
                                            intent.putExtra("firstname", firstname);
                                            intent.putExtra("lastname", lastname);
                                            intent.putExtra("useremail", useremail);
                                            intent.putExtra("contactnumber", contactnumber);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            } else {
                                dialog.dismiss();
                                if (task.getException() instanceof FirebaseNetworkException) {
                                    Toast.makeText(SignInActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                                } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                    Toast.makeText(SignInActivity.this, "User Does not Exists ", Toast.LENGTH_SHORT).show();
                                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(SignInActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(SignInActivity.this, "Enter the email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.signuptextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(1000);
                binding.progressbarsignin.setVisibility(View.VISIBLE);
            }
        });
        binding.forgotpasswordtextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    public void start(int duration) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (duration == 2000) {
                    Intent intent = new Intent(SignInActivity.this, MainScreen.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SignInActivity.this, SwipeScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, duration);
    }
}