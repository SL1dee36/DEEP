package com.example.deep

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

class AdapterImages internal constructor(
    private val context: Context,
    private val imagePaths: List<String>
) : RecyclerView.Adapter<AdapterImages.ViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.full_image_view, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val imagePath = imagePaths[i]
        loadImageFromAssets(viewHolder.imageFullScreen, imagePath)
    }

    override fun getItemCount(): Int {
        return imagePaths.size
    }

    private fun loadImageFromAssets(imageView: ImageView, imagePath: String) {
        try {
            val assetManager = context.assets
            val inputStream = assetManager.open(imagePath)
            val drawable = Drawable.createFromStream(inputStream, null)
            imageView.setImageDrawable(drawable)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageFullScreen: ImageView

        init {
            imageFullScreen = itemView.findViewById(R.id.imageFullScreen)
        }
    }
}