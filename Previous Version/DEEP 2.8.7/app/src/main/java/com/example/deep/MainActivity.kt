package com.example.deep

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.util.Locale

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var manager: SensorManager? = null
    private var currentDegree: Int = 0
    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        manager = getSystemService(SENSOR_SERVICE) as SensorManager

        val locationButton = findViewById<Button>(R.id.locationButton)
        locationButton.setOnClickListener {
            showLocation()
        }
    }

    // КНОПКИ меню:

    fun startAquaActivity(v: View) {
        val intent = Intent(this, aqua_frame::class.java)
        startActivity(intent)
    }

    fun startHandBookActivity(v: View) {
        val intent = Intent(this, handbook::class.java)
        startActivity(intent)
    }

    fun startCompassActivity(v: View) {
        val intent = Intent(this, CompassFrameActivity::class.java)
        startActivity(intent)
    }

    fun startDeveloperActivity(v: View) {
        val intent = Intent(this, developer::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        manager?.registerListener(
            this,
            manager?.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onPause() {
        super.onPause()
        manager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val imDinamic: ImageView = findViewById(R.id.imDinamic)
        val degree: Int = event?.values?.get(0)?.toInt() ?: 0
        val rotationAnim = RotateAnimation(
            currentDegree.toFloat(), (-degree).toFloat(),
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotationAnim.duration = 210
        rotationAnim.fillAfter = true
        currentDegree = -degree

        imDinamic.startAnimation(rotationAnim)
    }

    private fun showLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location: Location? =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)


        if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude

            val geocoder = Geocoder(this, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val address = addresses[0]
                    val country = address.countryName ?: ""
                    val city = address.locality ?: ""

                    val locationString = "$country $city\n$latitude $longitude"
                    val locationButton = findViewById<Button>(R.id.locationButton)
                    locationButton.text = locationString
                } else {
                    showError("Не удалось определить адрес.")
                }
            } catch (e: IOException) {
                showError("Ошибка геокодирования: ${e.message}")
            }
        } else {
            showError("Местоположение недоступно.")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showLocation()
            } else {
                showError("Доступ к местоположению отклонен.")
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        val locationButton = findViewById<Button>(R.id.locationButton)
        locationButton.text = "Ошибка: $message"
    }
}