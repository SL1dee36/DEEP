package com.example.deep

import android.Manifest
import android.content.Context
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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import java.io.IOException
import java.util.Locale

class CompassFrameActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var imDynamic: ImageView
    private var currentDegree: Int = 0
    private lateinit var geocoder: Geocoder
    private val LOCATION_PERMISSION_REQUEST_CODE = 123
    private lateinit var locationButton: MaterialButton
    private var currentLocationString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)
        imDynamic = findViewById(R.id.compass_dynamic)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        geocoder = Geocoder(this, Locale.getDefault())
        locationButton = findViewById(R.id.locationButton)

        locationButton.setOnClickListener {
            showLocation()
        }
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
        // Not used
    }

    override fun onSensorChanged(event: SensorEvent) {
        val degree: Int = event.values[0].toInt()
        val rotationAnim = RotateAnimation(
            currentDegree.toFloat(), (-degree).toFloat(),
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )

        rotationAnim.duration = 210
        rotationAnim.fillAfter = true
        currentDegree = -degree

        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        val index = (degree / 45 + 0.5).toInt() % 8

        val comp = findViewById<TextView>(R.id.compassView)
        comp.text = "${-currentDegree}° ${directions[index]}"
        imDynamic.startAnimation(rotationAnim)
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

            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val address = addresses[0]
                    val country = address.countryName ?: ""
                    val city = address.locality ?: ""

                    val locationString = "$country $city\n$latitude $longitude"
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
        locationButton.text = "Ошибка: $message"
    }

    fun startMainActivity(v: View) {
        finish()
    }
}