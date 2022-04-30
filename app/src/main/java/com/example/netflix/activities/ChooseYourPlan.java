package com.example.netflix.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix.R;
import com.example.netflix.databinding.ActivityChooseYourPlanBinding;
import com.example.netflix.databinding.ToolbarsteponeBinding;

public class ChooseYourPlan extends AppCompatActivity {
    ActivityChooseYourPlanBinding binding;
    ToolbarsteponeBinding toolbarsteponeBinding;
    String planname,price,planformatofcost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChooseYourPlanBinding.inflate(getLayoutInflater());
        toolbarsteponeBinding=ToolbarsteponeBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());
        planname="PREMIUM";
        price="799";
        planformatofcost="₹799/month";
        getSupportActionBar().hide();
        binding.radiobuttonforpremium.setChecked(true);
        binding.radiobuttonforbasic.setOnCheckedChangeListener(new CheckRadio());
        binding.radiobuttonforstandard.setOnCheckedChangeListener(new CheckRadio());
        binding.radiobuttonforpremium.setOnCheckedChangeListener(new CheckRadio());
        binding.continuebuttonchooseplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChooseYourPlan.this,FinishUpAccount.class);
                intent.putExtra("planname",planname);
                intent.putExtra("price",price);
                intent.putExtra("planformatofcost",planformatofcost);
                startActivity(intent);
            }
        });
        toolbarsteponeBinding.signinstepone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChooseYourPlan.this,SignInActivity.class);
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
                    binding.radiobuttonforstandard.setChecked(false);
                    binding.radiobuttonforpremium.setChecked(false);
                }
                else if(compoundButton.getId()==R.id.radiobuttonforstandard){
                    planname="STANDARD";
                    price="649";
                    planformatofcost="₹649/month";
                    binding.radiobuttonforbasic.setChecked(false);
                    binding.radiobuttonforpremium.setChecked(false);
                }
                else if(compoundButton.getId()==R.id.radiobuttonforpremium){
                    planname="PREMIUM";
                    price="799";
                    planformatofcost="₹799/month";
                    binding.radiobuttonforstandard.setChecked(false);
                    binding.radiobuttonforbasic.setChecked(false);
                }
            }
        }
    }
}