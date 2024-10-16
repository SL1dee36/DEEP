package com.example.deep.handbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.deep.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Image extends AppCompatActivity {
    AdapterImages adapterImages;
    ViewPager2 viewPagerFull;
    Button buttonBackFull;
    private List<String> image;
    LinearLayout sliderDots;
    private int dotsCount;
    private ImageView[] dots;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        viewPagerFull = findViewById(R.id.viewPagerFull);
        sliderDots = findViewById(R.id.SliderDotsFull);
        buttonBackFull = findViewById(R.id.buttonBackFull);

        Intent i = getIntent();
        String images = i.getStringExtra("image");
        int set = i.getIntExtra("set", 0);

        image = new ArrayList<String>(Arrays.asList(images.split(" ")));

        adapterImages  = new AdapterImages(this, image);
        viewPagerFull.setAdapter(adapterImages);
        viewPagerFull.setCurrentItem(set);

        dotsCount = adapterImages.getItemCount();
        dots = new ImageView[dotsCount];

        for (int j = 0; j < dotsCount; j++) {
            dots[j] = new ImageView(this);
            dots[j].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDots.addView(dots[j], params);
        }
        dots[set].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPagerFull.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                for(int j = 0; j < dotsCount; j++) {
                    dots[j].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        buttonBackFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}


