package com.example.netflix.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix.databinding.ActivityFinishUpAccountBinding;
import com.example.netflix.databinding.ToolbarsteponeBinding;

public class FinishUpAccount extends AppCompatActivity {
    String planname,price,planformatofcost;
    ActivityFinishUpAccountBinding binding;
    ToolbarsteponeBinding toolbarsteponeBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFinishUpAccountBinding.inflate(getLayoutInflater());
        toolbarsteponeBinding=ToolbarsteponeBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        toolbarsteponeBinding.signinstepone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FinishUpAccount.this,SignInActivity.class);
                startActivity(intent);

            }
        });
        Intent intent=getIntent();
        planname=intent.getStringExtra("planname");
        price=intent.getStringExtra("price");
        planformatofcost=intent.getStringExtra("planformatofcost");
        Toast.makeText(this, "You Have Taken Plan "+planname+" Whose Cost is "+planformatofcost, Toast.LENGTH_SHORT).show();
        SpannableString st=new SpannableString("STEP 1 OF 3");
        StyleSpan boldspan=new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan1=new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan,5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan1,10,11,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.step1of3finish.setText(st);
        binding.step1of3continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FinishUpAccount.this,StepTwo.class);
                intent.putExtra("planname",planname);
                intent.putExtra("price",price);
                intent.putExtra("planformatofcost",planformatofcost);
                startActivity(intent);
            }
        });
    }
}