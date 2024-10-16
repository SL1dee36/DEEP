package com.example.deep.handbook;

import android.content.Context;
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
import java.util.List;


public class AdapterImages extends RecyclerView.Adapter<AdapterImages.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> imagePaths;

    AdapterImages(Context context, List<String> imagePaths) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.imagePaths = imagePaths;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.full_image_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String imagePath = imagePaths.get(i);
        loadImageFromAssets(viewHolder.imageFullScreen, imagePath, false);
        loadImageFromAssets(viewHolder.imageFullBack, imagePath, true);
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    private void loadImageFromAssets(ImageView imageView, String imagePath, boolean a) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(imagePath);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imageView.setImageDrawable(drawable);
            if (a) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //imageView.blu
            }
            else
                imageView.setScaleType(ImageView.ScaleType.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFullScreen;
        ImageView imageFullBack;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageFullScreen = itemView.findViewById(R.id.imageFullScreen);
            imageFullBack = itemView.findViewById(R.id.imageFullBack);

        }
    }
}