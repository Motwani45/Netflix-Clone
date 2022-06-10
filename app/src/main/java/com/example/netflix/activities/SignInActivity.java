package com.example.netflix.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix.databinding.ActivitySignInBinding;
import com.example.netflix.mainscreens.MainScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.firestore.Query;

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
                            if (task.isSuccessful() && task.getResult().getUser().isEmailVerified()) {
                                Task<DocumentSnapshot> df = firestore.collection("Users").document(task.getResult().getUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
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
                                            auth.signOut();
                                            Toast.makeText(SignInActivity.this, "Please Choose the Plan by going on Sign Up Now", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                });
                            } else if (task.isSuccessful() && !task.getResult().getUser().isEmailVerified()) {
                                dialog.dismiss();
                                Toast.makeText(SignInActivity.this, "Please Verify Your Email and Choose the Plan by going on Sign Up Now", Toast.LENGTH_SHORT).show();
                                Log.d("CheckQuery", "False");
                                auth.signOut();
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
                } else {
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

                EditText forgotpasswordet = new EditText(view.getContext());
                AlertDialog.Builder forgotpassword = new AlertDialog.Builder(view.getContext());
                forgotpassword.setTitle("Forgot Password");
                forgotpassword.setCancelable(false);
                forgotpasswordet.setHint("Enter The Email With You Have Been Registered");
//                forgotpasswordet.setSingleLine();
                forgotpassword.setView(forgotpasswordet);
                forgotpassword.setPositiveButton("Reset Password", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String emailstring = forgotpasswordet.getText().toString();
                        auth.sendPasswordResetEmail(emailstring).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    new AlertDialog.Builder(SignInActivity.this).setTitle("RESET PASSWORD").setMessage(" Email has been sent to the mail: " + emailstring+" for resetting the pasword").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).create().show();
                                }
                                else{
                                    Toast.makeText(SignInActivity.this, "Error in Sending Reset Password Link Try Again ", Toast.LENGTH_LONG).show();
                                }
                            }


                        });
                    }
                }) ;
                forgotpassword.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                forgotpassword.create().

                        show();
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
                    binding.progressbarsignin.setVisibility(View.GONE);
                    finish();
                } else {
                    binding.progressbarsignin.setVisibility(View.GONE);
                    Intent intent = new Intent(SignInActivity.this, SwipeScreen.class);
                    startActivity(intent);
                }
            }
        }, duration);
    }
}