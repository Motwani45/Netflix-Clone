package com.example.netflix.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.netflix.R;
import com.example.netflix.adapters.ViewPagerAdapter;
import com.example.netflix.databinding.ActivitySwipeScreenBinding;

public class SwipeScreen extends AppCompatActivity {
    TextView signin, help, privacy;
    Button getstarted;
    ViewPager viewPager;
    LinearLayout swipedots;
    private int dotscount,adapterposition;
    ImageView[] dots;
    String privacyurl="https://help.netflix.com/legal/privacy",helpurl="https://help.netflix.com/en/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_screen);
        getSupportActionBar().hide();
        help =findViewById(R.id.helptextview);
        signin =findViewById(R.id.signintextview);
        privacy =findViewById(R.id.privacytextview);
        getstarted=findViewById(R.id.getstarted);
        viewPager=findViewById(R.id.viewpagerswipescreen);
        swipedots=findViewById(R.id.swipedots);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        dotscount=viewPagerAdapter.getCount();
        dots=new ImageView[dotscount];
        for(int i=0;i<dotscount;i++){
            dots[i]=new ImageView(this);
            if(i!=0) {
                dots[i].setImageResource(R.drawable.inactivedots);
            }
            else{
                dots[i].setImageResource(R.drawable.activedots);
            }
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for(int i=0;i<dotscount;i++){
                        dots[i].setImageResource(R.drawable.inactivedots);
                    }
                    dots[position].setImageResource(R.drawable.activedots);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            dots[i].setPadding(20,0,20,0);
            swipedots.addView(dots[i]);
        }
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SwipeScreen.this,SignInActivity.class);
                startActivity(intent);
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(privacyurl));
                startActivity(intent);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(helpurl));
                startActivity(intent);
            }
        });
        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SwipeScreen.this,StepOne.class);
                startActivity(intent);
            }
        });

    }
}