package com.example.netflix.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.netflix.R;
import com.example.netflix.databinding.ActivityPaymentGatewayBinding;
import com.example.netflix.mainscreens.MainScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentGateway extends AppCompatActivity implements PaymentResultListener {
    String planname, price, planformatofcost, useremail, password, firstname, lastname, contactnumber, fullname, userid;
    Double amount;
    FirebaseAuth auth;
    boolean x;
    ActivityPaymentGatewayBinding binding;
    ProgressDialog progressDialog;
    FirebaseFirestore firestore;
    Calendar calendar;
    Date starting, ending;
    public static final String TAG = "Error In Payment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityPaymentGatewayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();
        starting = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        ending = calendar.getTime();
        progressDialog = new ProgressDialog(PaymentGateway.this);
        progressDialog.setTitle("CREATING ACCOUNT");
        progressDialog.setMessage("WE ARE CREATING YOUR ACCOUNT");
        Checkout.preload(getApplicationContext());
        Intent intent = getIntent();
        planname = intent.getStringExtra("planname");
        price = intent.getStringExtra("price");
        amount = Double.parseDouble(price) * 100;
        useremail = intent.getStringExtra("useremail");
        password = intent.getStringExtra("password");
        planformatofcost = intent.getStringExtra("planformatofcost");
        binding.startmembershipbutton.setEnabled(false);
        binding.startmembershipbutton.setBackgroundColor(Color.WHITE);
        SpannableString st = new SpannableString("STEP 3 OF 3");
        StyleSpan boldspan = new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan1 = new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan, 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan1, 10, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.step3of3payment.setText(st);
        binding.formatofcost.setText(planformatofcost);
        binding.nameofplan.setText(planname);
        SpannableString ss = new SpannableString("By checking the checkbox below, you agree to our Term of use, Privacy Statement, and that you are over 18. Netflix will automatically continue your membership and charge the monthly memebership fee to your payment method untill you cancel. You may cancel at any time to avoid future charges.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/legal/termsofuse")));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/legal/privacy")));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 49, 60, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan1, 62, 79, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.terms.setText(ss);
        binding.terms.setMovementMethod(LinkMovementMethod.getInstance());
        binding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext()).setTitle("CHANGE PLAN").setMessage("Are You Sure To Change Your Plan").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent in = new Intent(PaymentGateway.this, ChooseYourPlan.class);
                        startActivity(in);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });
        binding.iagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.iagree.isChecked()) {
                    binding.startmembershipbutton.setEnabled(true);
                    binding.startmembershipbutton.setBackgroundColor(Color.RED);
                } else {
                    binding.startmembershipbutton.setBackgroundColor(Color.WHITE);
                    binding.startmembershipbutton.setEnabled(false);
                }
            }
        });
        binding.startmembershipbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                x = true;
                firstname = binding.firstnameedittext.getText().toString().trim();
                if (firstname.length() < 3 || !firstname.matches("[a-zA-Z]+")) {
                    binding.firstnameedittext.setError("Enter atleast 3 characters long firstname and it should not contain any number or special character");
                    x = false;
                }

                lastname = binding.lastnameedittext.getText().toString().trim();
                if (lastname.length() < 3 || !lastname.matches("[a-zA-Z]+")) {
                    binding.lastnameedittext.setError("Enter atleast 3 characters long lastname and it should not contain any number or special character");
                    x = false;
                }
                contactnumber = binding.contactnumberedittext.getText().toString().trim();
                if (contactnumber.length() != 10) {
                    binding.contactnumberedittext.setError("Enter  10 numbers long contact number");
                    x = false;
                }
                if (x) {
                    startPayment();
                }
            }
        });
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        progressDialog.show();
        Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        auth.createUserWithEmailAndPassword(useremail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {

                    Log.d("Signupsucces", "Sign Up is success due to :");
                    userid = auth.getCurrentUser().getUid();
                    DocumentReference reference = firestore.collection("Users").document(userid);
                    Map<String, Object> user = new HashMap<>();
                    user.put("Email", useremail);
                    user.put("Firstname", firstname);
                    user.put("Lastname", lastname);
                    user.put("Price", price);
                    user.put("Starting", starting);
                    user.put("Ending", ending);
                    user.put("Contact Number", contactnumber);
                    reference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(PaymentGateway.this, MainScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PaymentGateway.this, "User details failed to store because of :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    Log.d("SignupFailed", "Sign Up is Failed due to :" + task.getException().getMessage());
                }

            }
        });
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
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
}