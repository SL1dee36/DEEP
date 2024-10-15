package com.example.deep.handbook;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deep.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AdapterDetails extends RecyclerView.Adapter<AdapterDetails.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> imagePaths;
    private String images;

    AdapterDetails(Context context, List<String> imagePaths, String images) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.imagePaths = imagePaths;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.image_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String imagePath = imagePaths.get(i);
        loadImageFromAssets(viewHolder.imageOther, imagePath);
        viewHolder.setPosition(i);
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    private void loadImageFromAssets(ImageView imageView, String imagePath) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(imagePath);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imageView.setImageDrawable(drawable);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOther;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), Image.class);
                    i.putExtra("image", images);
                    i.putExtra("set", position);
                    v.getContext().startActivity(i);
                }
            });

            imageOther = itemView.findViewById(R.id.imageFullScreen);
        }
        public void setPosition(int position) {
            this.position = position;
        }
    }
}
