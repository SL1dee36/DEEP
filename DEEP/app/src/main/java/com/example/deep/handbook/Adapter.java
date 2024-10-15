package com.example.deep.handbook;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deep.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> title_title;
    private List<String> title_desc;
    private List<String> title_image;

    // others details
    private List<String> desc;
    private List<String> parms;
    private List<String> images;


    Adapter(Context context, List<String> title_title, List<String> title_desc, List<String> title_image, List<String> desc, List<String> parms, List<String> images) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.title_title = title_title;
        this.title_desc = title_desc;
        this.title_image = title_image;
        // others details
        this.desc = desc;
        this.parms = parms;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.custom_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String title = title_title.get(i);
        viewHolder.textTitle.setText(title);

        String title_Desc = title_desc.get(i);
        viewHolder.textDescription.setText(title_Desc);

        String title_Image = title_image.get(i);
        loadImageFromAssets(viewHolder.imageTitle, title_Image);

    }
    @Override
    public int getItemCount() {
        return title_title.size();
    }
    private void loadImageFromAssets(ImageView imageView, String imagePath) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(imagePath);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imageView.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle,textDescription;
        ImageView imageTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), Details.class);
                    i.putExtra("title", title_title.get(getAdapterPosition()));
                    i.putExtra("desc", desc.get(getAdapterPosition()));
                    i.putExtra("parms", parms.get(getAdapterPosition()));
                    i.putExtra("images", images.get(getAdapterPosition()));
                    v.getContext().startActivity(i);

                }
            });
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDesc);
            imageTitle = itemView.findViewById(R.id.imageTitle);
        }
    }
}