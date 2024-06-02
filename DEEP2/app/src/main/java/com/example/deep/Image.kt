package com.example.deep

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback

class Image : AppCompatActivity() {
    var adapterImages: AdapterImages? = null
    var viewPagerFull: ViewPager2? = null
    var buttonBackFull: Button? = null
    private var image: List<String>? = null
    var sliderDots: LinearLayout? = null
    private var dotsCount = 0
    private lateinit var dots: Array<ImageView?>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        viewPagerFull = findViewById(R.id.viewPagerFull)
        sliderDots = findViewById(R.id.SliderDotsFull)
        buttonBackFull = findViewById(R.id.buttonBackFull)
        val i = intent
        val images = i.getStringExtra("image")
        val set = i.getIntExtra("set", 0)
        image = images?.split(" ") ?: listOf()
        adapterImages = AdapterImages(this, image!!)
        viewPagerFull?.adapter = adapterImages
        viewPagerFull?.setCurrentItem(set, false)
        dotsCount = adapterImages?.itemCount ?: 0
        dots = Array(dotsCount) { null }

        for (j in 0 until dotsCount) {
            dots[j] = ImageView(this)
            dots[j]?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.nonactive_dot))
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(8, 0, 8, 0)
            sliderDots?.addView(dots[j], params)
        }

        dots.getOrNull(set)?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.active_dot))

        viewPagerFull?.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (j in 0 until dotsCount) {
                    dots.getOrNull(j)?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.nonactive_dot))
                }
                dots.getOrNull(position)?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.active_dot))
            }
        })

        buttonBackFull?.setOnClickListener { finish() }
    }
}
