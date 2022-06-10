package com.example.netflix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;

import com.example.netflix.R;
import com.example.netflix.databinding.ActivityStepThreeBinding;
import com.example.netflix.databinding.ToolbarstepthreeBinding;

public class StepThree extends AppCompatActivity {
    String planname,price,planformatofcost,useremail,password;
    ActivityStepThreeBinding binding;
    ToolbarstepthreeBinding toolbarstepthreeBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityStepThreeBinding.inflate(getLayoutInflater());
        toolbarstepthreeBinding=ToolbarstepthreeBinding.bind(binding.getRoot());
        toolbarstepthreeBinding.signoutstepthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StepThree.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        Intent intent=getIntent();
        planname=intent.getStringExtra("planname");
        price=intent.getStringExtra("price");
        planformatofcost=intent.getStringExtra("planformatofcost");
        useremail=intent.getStringExtra("useremail");
        password=intent.getStringExtra("password");
        SpannableString st=new SpannableString("STEP 3 OF 3");
        StyleSpan boldspan=new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan1=new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan,5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan1,10,11,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.step3of3.setText(st);
        binding.paymentlinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StepThree.this,PaymentGateway.class);
                intent.putExtra("planname",planname);
                intent.putExtra("price",price);
                intent.putExtra("planformatofcost",planformatofcost);
                intent.putExtra("useremail",useremail);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });

    }
}