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
import com.example.netflix.databinding.ActivityStepOneBinding;
import com.example.netflix.databinding.ToolbarsteponeBinding;

public class StepOne extends AppCompatActivity {
    ActivityStepOneBinding binding;
    ToolbarsteponeBinding toolbarsteponeBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityStepOneBinding.inflate(getLayoutInflater());
        toolbarsteponeBinding=ToolbarsteponeBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        toolbarsteponeBinding.signinstepone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StepOne.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.steponebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StepOne.this,ChooseYourPlan.class);
                startActivity(intent);
            }
        });
        SpannableString st=new SpannableString("STEP 1 OF 3");
        StyleSpan boldspan=new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan1=new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan,5,6,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan1,10,11,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.step1of3.setText(st);
    }
}