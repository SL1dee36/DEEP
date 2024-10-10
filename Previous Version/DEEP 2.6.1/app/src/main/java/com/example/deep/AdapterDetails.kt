package com.example.deep

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

class AdapterDetails internal constructor(
    private val context: Context,
    private val imagePaths: List<String>,
    private val images: String
) : RecyclerView.Adapter<AdapterDetails.ViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.image_view, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val imagePath = imagePaths[i]
        loadImageFromAssets(viewHolder.imageOther, imagePath)
        viewHolder.position = i
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
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageOther: ImageView
        private var position = 0

        init {
            itemView.setOnClickListener { v ->
                val i = Intent(v.context, Image::class.java)
                i.putExtra("image", images)
                i.putExtra("set", position)
                v.context.startActivity(i)
            }
            imageOther = itemView.findViewById(R.id.imageFullScreen)
        }

        fun setPosition(position: Int) {
            this.position = position
        }
    }
}
