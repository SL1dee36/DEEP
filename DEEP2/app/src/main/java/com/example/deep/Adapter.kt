package com.example.deep

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

class Adapter internal constructor(
    private val context: Context,
    private val title_title: List<String>,
    private val title_desc: List<String>,
    private val title_image: List<String>, // others details
    private val desc: List<String>,
    private val parms: List<String>,
    private val images: List<String>
) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: View = layoutInflater.inflate(R.layout.custom_view, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val title = title_title[i]
        viewHolder.textTitle.text = title
        val title_Desc = title_desc[i]
        viewHolder.textDescription.text = title_Desc
        val title_Image = title_image[i]
        loadImageFromAssets(viewHolder.imageTitle, title_Image)
    }

    override fun getItemCount(): Int {
        return title_title.size
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
        var textTitle: TextView
        var textDescription: TextView
        var imageTitle: ImageView

        init {
            itemView.setOnClickListener { v ->
                val i = Intent(v.context, Details::class.java)
                i.putExtra("title", title_title[adapterPosition])
                i.putExtra("desc", desc[adapterPosition])
                i.putExtra("parms", parms[adapterPosition])
                i.putExtra("drawable/images", images[adapterPosition])
                v.context.startActivity(i)
            }
            textTitle = itemView.findViewById(R.id.textTitle)
            textDescription = itemView.findViewById(R.id.textDesc)
            imageTitle = itemView.findViewById(R.id.imageTitle)
        }
    }
}