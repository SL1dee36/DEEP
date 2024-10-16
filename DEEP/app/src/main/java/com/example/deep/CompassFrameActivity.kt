package com.example.deep

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class CompassFrameActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var imDynamic: ImageView
    private var currentDegree: Int = 0
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)
        imDynamic = findViewById(R.id.compass_dynamic)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        geocoder = Geocoder(this, Locale.getDefault())

        /*val videoView = findViewById<VideoView>(R.id.videoView)
        videoView.setVideoURI(Uri.parse("android.resource://${packageName}/${R.raw.bgb}"))
        videoView.start()
        videoView.setOnPreparedListener { mp -> mp.isLooping = true }*/

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Optional, no fixing required
    }

    override fun onSensorChanged(event: SensorEvent) {
        val degree: Int = event.values[0].toInt()
        val rotationAnim = RotateAnimation(
            currentDegree.toFloat(), (-degree).toFloat(),
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )

        rotationAnim.duration = 360
        rotationAnim.fillAfter = true
        currentDegree = -degree

        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        val index = (degree / 45 + 0.5).toInt() % 8

        val comp = findViewById<TextView>(R.id.compassView)

        val location = "Unavailable"
        val city = "Russia:n.Moscow"

        val locationInfo = findViewById<TextView>(R.id.locationInfo)
        locationInfo.text = "$location\n$city"
        comp.text = "${-currentDegree}° ${directions[index]}"
        imDynamic.startAnimation(rotationAnim)
    }


    fun startMainActivity(v: View) {
        finish()
    }
}
