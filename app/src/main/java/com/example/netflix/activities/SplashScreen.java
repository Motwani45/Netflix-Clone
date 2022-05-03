package com.example.netflix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.netflix.databinding.ActivitySplashScreenBinding;
import com.example.netflix.mainscreens.MainScreen;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    //    static int counter=0;
//    static int duration=5000;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    DocumentReference reference;
    Date today, ending;
    String useremail,password,userid,firstname,lastname,contactnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        verifyDate();
    }

    public void verifyDate() {
        Calendar calendar = Calendar.getInstance();
        today = calendar.getTime();
        if (auth.getCurrentUser() != null) {
            userid = auth.getCurrentUser().getUid();
            reference = firestore.collection("Users").document(userid);
           Log.d("Userid", userid);
            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()) {
                        Log.d("DocumentSnapshot","true");
                        ending=documentSnapshot.getDate("Ending");
                        firstname=documentSnapshot.getString("Firstname");
                        lastname=documentSnapshot.getString("Lastname");
                        contactnumber=documentSnapshot.getString("Contact Number");
                        useremail=documentSnapshot.getString("Email");
                        if(today!=null) {
                            if (ending.compareTo(today) >= 0) {
                                start(2100);
                            }
                            else {
                                start(2200);
                            }
                        }
                    }
                    else{
                        Log.d("DocumentSnapshot","false");
                        auth.signOut();
                        start(2300);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e instanceof FirebaseNetworkException){
                        Toast.makeText(SplashScreen.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SplashScreen.this, "Data can't be fetched due to:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            start(2300);
        }
    }

    //    public void progress(){
//        final Timer timer=new Timer();
//        TimerTask timerTask=new TimerTask() {
//            @Override
//            public void run() {
//                while(counter<=duration){
//                    counter++;
//                    binding.progressbar.setProgress(counter);
//                }
//                timer.cancel();
//            }
//        };
//        timer.schedule(timerTask,0,100);
//    }
    public void start(int duration) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (duration == 2300) {
                    Intent intent = new Intent(SplashScreen.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }else if (duration == 2200) {
                    Intent intent = new Intent(SplashScreen.this, PaymentOverdue.class);
                    intent.putExtra("userid", userid);
                    intent.putExtra("firstname", firstname);
                    intent.putExtra("lastname", lastname);
                    intent.putExtra("useremail", useremail);
                    intent.putExtra("contactnumber", contactnumber);
                    startActivity(intent);
                    finish();
                }if (duration == 2100) {
                    Intent intent = new Intent(SplashScreen.this, MainScreen.class);
                    startActivity(intent);
                    finish();
                }
            }


        }, duration);
    }
}