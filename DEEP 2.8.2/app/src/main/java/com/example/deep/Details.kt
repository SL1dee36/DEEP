package com.example.deep

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback

class Details : AppCompatActivity() {
    var textTitle: TextView? = null
    var textFDesc: TextView? = null
    var textParms: TextView? = null
    var buttonBack: Button? = null
    var adapterDetails: AdapterDetails? = null
    var viewPager: ViewPager2? = null
    var sliderDots: LinearLayout? = null
    private var dotsCount = 0
    private lateinit var dots: Array<ImageView?>
    private var image: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        textTitle = findViewById(R.id.detailTitle)
        textFDesc = findViewById(R.id.textFullDesc)
        buttonBack = findViewById(R.id.buttonBack)
        textParms = findViewById(R.id.textParamers)
        viewPager = findViewById(R.id.viewPager)
        sliderDots = findViewById(R.id.SliderDots)
        val i = intent
        val title = i.getStringExtra("title")
        val desc = i.getStringExtra("desc")
        val parms = i.getStringExtra("parms")
        val images = i.getStringExtra("drawable/images")
        image = images?.split(" ") ?: emptyList()
        adapterDetails =
            images?.let { AdapterDetails(this, image as ArrayList<String>, it) } // Image
        viewPager?.adapter = adapterDetails
        // viewPager?.setCurrentItem(2);
        dotsCount = adapterDetails?.itemCount ?: 0
        dots = arrayOfNulls(dotsCount)
        for (j in 0 until dotsCount) {
            dots[j] = ImageView(this)
            dots[j]?.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.nonactive_dot
                )
            )
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            sliderDots?.addView(dots[j], params)
        }
        dots.getOrNull(0)?.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.active_dot
            )
        )
        viewPager?.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (j in 0 until dotsCount) {
                    dots.getOrNull(j)?.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.nonactive_dot
                        )
                    )
                }
                dots.getOrNull(position)?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.active_dot
                    )
                )
            }
        })
        buttonBack?.setOnClickListener { finish() }
        textFDesc?.text = desc
        textTitle?.text = title
        textParms?.text = parms
    }
}
