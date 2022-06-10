package com.example.netflix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.netflix.databinding.ActivityStepTwoBinding;
import com.example.netflix.databinding.ToolbarsteponeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class StepTwo extends AppCompatActivity {
    String planname, price, planformatofcost, useremail, password;
    ActivityStepTwoBinding binding;
    ToolbarsteponeBinding toolbarsteponeBinding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    boolean iscorrect;
    DocumentSnapshot ds;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStepTwoBinding.inflate(getLayoutInflater());
        toolbarsteponeBinding = ToolbarsteponeBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());
        dialog = new ProgressDialog(this);
        dialog.setMessage("Checking Your Email and Password");
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        binding.progressbarsignup.setVisibility(View.GONE);
        toolbarsteponeBinding.signinstepone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StepTwo.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Intent intent = getIntent();
        planname = intent.getStringExtra("planname");
        price = intent.getStringExtra("price");
        planformatofcost = intent.getStringExtra("planformatofcost");
        getSupportActionBar().hide();
        SpannableString st = new SpannableString("STEP 2 OF 3");
        StyleSpan boldspan = new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan1 = new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan, 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan1, 10, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.step2of3.setText(st);
        binding.buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iscorrect = true;
                useremail = binding.emailedittextstep2.getText().toString().trim();
                password = binding.passwordedittextstep2.getText().toString().trim();
                if (!useremail.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
                    binding.emailedittextstep2.setError("Enter a valid Email-Id");
                    iscorrect = false;
                }
                if (password.length() < 8) {
                    binding.passwordedittextstep2.setError("Please enter a password of atleast 8 characters long");
                    iscorrect = false;
                }
                if (iscorrect) {
                    dialog.show();
                    auth.signInWithEmailAndPassword(useremail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful() && task.getResult().getUser().isEmailVerified()) {
                                Task<DocumentSnapshot> df=firestore.collection("Users").document(task.getResult().getUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()){
                                            dialog.dismiss();
                                            Log.d("CheckQuery","True");
                                            Toast.makeText(StepTwo.this, "Email-id already in use", Toast.LENGTH_SHORT).show();
                                            auth.signOut();
                                        }
                                        else{
                                            dialog.dismiss();
                                            Log.d("CheckQuery","False");
                                            auth.signOut();
                                            start(1000);
                                        }
                                    }
                                });
                            }
                            else if (task.isSuccessful() && !task.getResult().getUser().isEmailVerified()) {
                                dialog.dismiss();
                                Log.d("CheckQuery","False");
                                auth.signOut();
                                start(1000);
                            } else {
                                auth.createUserWithEmailAndPassword(useremail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = task.getResult().getUser();
                                            doEmailVerification(user, useremail);


                                        } else {
                                            dialog.dismiss();
                                            Toast.makeText(StepTwo.this, "Problem with signing up: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }
                    });
                }


            }
        });
    }

    private void doEmailVerification(FirebaseUser user, String email) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    new AlertDialog.Builder(StepTwo.this).setTitle("EMAIL VERIFICATION").setMessage("Verification email has been sent to the mail: " + email).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            start(1000);
                        }
                    }).create().show();
                } else {
                    new AlertDialog.Builder(StepTwo.this).setTitle("ERROR").setMessage("Verification email not sent TRY AGAIN!").setCancelable(true).create().show();
                }
            }
        });
    }

//    private boolean checkQuery(String uid) {
//        boolean[]a=new boolean[1];
//        DocumentSnapshot sn;
//        checkUid(uid,a);
//        if(a[0]) {
//            Log.d("CheckQuery", "checkQuery: True" );
//        }
//        else {
//            Log.d("CheckQuery", "checkQuery: False");
//        }
//        return a[0];
//
//
//
//    }
//
//    private void checkUid(String uid, boolean[] a) {
//        Task<DocumentSnapshot> documentReference = firestore.collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                a[0]=documentSnapshot.exists();
//            }
//        });
//    }

    public void start(int duration) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                Intent intent = new Intent(StepTwo.this, StepThree.class);
                intent.putExtra("planname", planname);
                intent.putExtra("price", price);
                intent.putExtra("planformatofcost", planformatofcost);
                intent.putExtra("useremail", useremail);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        }, duration);
    }

//    @Override
//    public void onSuccess(Object o) {
//
//    }
}