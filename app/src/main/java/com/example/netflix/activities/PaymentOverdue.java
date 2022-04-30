package com.example.netflix.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.netflix.R;
import com.example.netflix.databinding.ActivityChooseYourPlanBinding;
import com.example.netflix.databinding.ActivityPaymentOverdueBinding;
import com.example.netflix.databinding.ToolbarsteponeBinding;
import com.example.netflix.mainscreens.MainScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentOverdue extends AppCompatActivity implements PaymentResultListener {
    Double amount;
    String planname, price, planformatofcost, useremail, firstname, lastname, contactnumber, fullname, userid;
    ActivityPaymentOverdueBinding binding;
    ToolbarsteponeBinding toolbarsteponeBinding;
    Calendar calendar;
    Intent intent;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Date starting, ending;
    DocumentReference reference;
    public static final String TAG = "Error In Payment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPaymentOverdueBinding.inflate(getLayoutInflater());
        toolbarsteponeBinding=ToolbarsteponeBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        planname="PREMIUM";
        price="799";
        planformatofcost="₹799/month";
        amount = Double.parseDouble(price) * 100;
        intent=getIntent();
        userid=intent.getStringExtra("userid");
        firstname=intent.getStringExtra("firstname");
        lastname=intent.getStringExtra("lastname");
        useremail=intent.getStringExtra("useremail");
        contactnumber=intent.getStringExtra("contactnumber");
        calendar = Calendar.getInstance();
        starting = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        ending = calendar.getTime();
        getSupportActionBar().hide();
        Checkout.preload(getApplicationContext());
        binding.radiobuttonforpremium.setChecked(true);
        binding.radiobuttonforbasic.setOnCheckedChangeListener(new CheckRadio());
        binding.radiobuttonforstandard.setOnCheckedChangeListener(new CheckRadio());
        binding.radiobuttonforpremium.setOnCheckedChangeListener(new CheckRadio());
        binding.continuebuttonpaymentoverdue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });
        toolbarsteponeBinding.signinstepone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PaymentOverdue.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class CheckRadio implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if(isChecked){
                if(compoundButton.getId()==R.id.radiobuttonforbasic){
                    planname="BASIC";
                    price="349";
                    planformatofcost="₹349/month";
                    amount = Double.parseDouble(price) * 100;
                    binding.radiobuttonforstandard.setChecked(false);
                    binding.radiobuttonforpremium.setChecked(false);
                }
                else if(compoundButton.getId()==R.id.radiobuttonforstandard){
                    planname="STANDARD";
                    price="649";
                    planformatofcost="₹649/month";
                    amount = Double.parseDouble(price) * 100;
                    binding.radiobuttonforbasic.setChecked(false);
                    binding.radiobuttonforpremium.setChecked(false);
                }
                else if(compoundButton.getId()==R.id.radiobuttonforpremium){
                    planname="PREMIUM";
                    price="799";
                    planformatofcost="₹799/month";
                    amount = Double.parseDouble(price) * 100;
                    binding.radiobuttonforstandard.setChecked(false);
                    binding.radiobuttonforbasic.setChecked(false);
                }
            }
        }
    }
    public void startPayment() {
        Checkout checkout = new Checkout();
        final Activity activity = this;
        fullname = firstname + " " + lastname;
        try {
            JSONObject options = new JSONObject();
            options.put("name", fullname);
            options.put("description", "PAYMENT FOR NETFLIX");
            options.put("currency", "INR");
            options.put("amount", amount);
            JSONObject prefill = new JSONObject();
            prefill.put("email", useremail);
            prefill.put("contact", contactnumber);
            options.put("prefill", prefill);
            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        reference=firestore.collection("Users").document(userid);
        reference.update("Price",price,"Ending",ending,"Starting",starting);
        Intent intent=new Intent(PaymentOverdue.this,MainScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
}