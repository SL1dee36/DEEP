package com.example.deep.handbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.deep.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




public class Details extends AppCompatActivity {
    TextView textTitle, textFDesc, textParms, textView2;
    EditText NoteText;
    Button buttonBack, NoteSave;
    AdapterDetails adapterDetails;
    ViewPager2 viewPager;
    LinearLayout sliderDots;
    private int dotsCount;
    private ImageView[] dots;
    private List<String> image;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        textTitle = findViewById(R.id.detailTitle);
        textFDesc = findViewById(R.id.textFullDesc);
        buttonBack = findViewById(R.id.buttonBack);
        textParms = findViewById(R.id.textParamers);
        viewPager = findViewById(R.id.viewPager);
        sliderDots = findViewById(R.id.SliderDots);
        NoteText = findViewById(R.id.NoteText);
        NoteSave = findViewById(R.id.SaveNotes);
        textView2 = findViewById(R.id.textView2);



        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String desc = i.getStringExtra("desc");
        String parms = i.getStringExtra("parms");
        String images = i.getStringExtra("images");


        image = new ArrayList<String>(Arrays.asList(images.split(" ")));

        adapterDetails  = new AdapterDetails(this, image, images);      //Image
        viewPager.setAdapter(adapterDetails);
        //viewPager.setCurrentItem(2);



        dotsCount = adapterDetails.getItemCount();
        dots = new ImageView[dotsCount];


        for (int j = 0; j < dotsCount; j++) {
            dots[j] = new ImageView(this);
            dots[j].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDots.addView(dots[j], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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
        createJsonFileInDownloadFolder(getApplicationContext(), "data");

        try {
            JSONObject jsonResponse = new JSONObject(JsonDataFromAsset("note.json"));
            JSONArray pp_minesArray = jsonResponse.getJSONArray("mine");
            pp_minesArray.put(1,"20");
            textView2.setText((pp_minesArray.getString(1)));














        } catch (JSONException e) {
            e.printStackTrace();
        }









        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textFDesc.setText(desc);
        textTitle.setText(title);
        textParms.setText(parms);
    }
    private String JsonDataFromAsset(String fileName) {
        String json = null;
        try {
            InputStream inputStream = getAssets().open(fileName);
            int sizeOfFile = inputStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            json = new String(bufferData, "UTF-8");
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return json;
    }
    public static void createJsonFileInDownloadFolder(Context context, String appName) {
        JSONObject jsonObject = new JSONObject();
        // Заполните JSON объект данными по вашему усмотрению

        try {
            String fileName = "download/" + appName + ".json";
            File file = new File(context.getExternalFilesDir(null), fileName);

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}