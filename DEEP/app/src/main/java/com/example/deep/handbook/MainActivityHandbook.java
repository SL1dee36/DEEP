package com.example.deep.handbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.deep.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivityHandbook extends AppCompatActivity implements TextWatcher {
    RecyclerView recyclerView;
    EditText findeditText;
    Adapter adapter;
    ArrayList<String> title;
    ArrayList<String> title_image;
    ArrayList<String> title_desc;
    ArrayList<String> desc;
    ArrayList<String> parms;
    ArrayList<String> images;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_handbook);

        title = new ArrayList<>();
        title_image = new ArrayList<>();
        title_desc = new ArrayList<>();
        desc = new ArrayList<>();
        parms = new ArrayList<>();
        images = new ArrayList<>();

        findeditText = findViewById(R.id.findeditText);
        findeditText.addTextChangedListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, title, title_image, title_desc, desc, parms, images);
        recyclerView.setAdapter(adapter);

        findeditText.setText("");
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
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    @Override
    public void afterTextChanged(Editable s) {
        text = findeditText.getText().toString();
        text = text.replaceAll("[ ,@#!№;$%:^?&*()_+=`~.,/?-]", "").toLowerCase();


        title.clear();
        title_desc.clear();
        title_image.clear();
        desc.clear();
        parms.clear();
        images.clear();

        try {
            JSONObject jsonResponse = new JSONObject(JsonDataFromAsset("information.json"));
            JSONArray pp_minesArray = jsonResponse.getJSONArray("mines");

            for (int i = 0; i < pp_minesArray.length(); i++) {
                String tmp1 = "";
                String tmp2 = "";
                JSONObject mineInfo = pp_minesArray.getJSONObject(i);
                if ((text == "") || (mineInfo.getString("title").replaceAll("[ ,@#!№;$%:^?&*()_+=`~.,/?-]", "").toLowerCase().contains(text))) {
                    title.add(mineInfo.getString("title"));
                    title_desc.add(mineInfo.getString("title_image"));
                    title_image.add(mineInfo.getString("title_desc"));
                    desc.add(mineInfo.getString("desc"));

                    JSONArray parmsArray = mineInfo.getJSONArray("parms");
                    for (int j = 0; j < parmsArray.length(); j++) {
                        JSONObject parm = parmsArray.getJSONObject(j);
                        tmp1 = tmp1 + parm.getString("name_parms") + parm.getString("parmetr") + "\n";
                    }
                    parms.add(tmp1);
                    JSONArray imgArray = mineInfo.getJSONArray("img");
                    for (int k = 0; k < imgArray.length(); k++) {
                        if (k == imgArray.length() - 1) {
                            tmp2 = tmp2 + imgArray.getString(k);
                        } else {
                            tmp2 = tmp2 + imgArray.getString(k) + " ";
                        }

                    }
                    images.add(tmp2);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();

    }

    public void handleBackButtonClick(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); //This will finish the activity normally
    }
}