package com.example.deep

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var LOCATION_PERMISSION_REQUEST_CODE = 123
    private var locationManager: LocationManager? = null
    private var manager: SensorManager? = null
    private var currentDegree: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        manager = getSystemService(SENSOR_SERVICE) as SensorManager

        val locationButton = findViewById<Button>(R.id.locationButton)
        locationButton.setOnClickListener {
            showLocation()
            //checkLocationPermission()
        }

    }

    // КНОПКИ меню:

    fun startAquaActivity(v: View) {
        // Создаем объект класса
        val intent = Intent(this, aqua_frame::class.java);

        // Выполняем переход
        startActivity(intent);
    }

    fun startMineActivity(v: View) {
        val intent = Intent(this, mineoff::class.java);

        // Выполняем переход
        startActivity(intent);
    }

    fun startHandBookActivity(v: View) {
        val intent = Intent(this, handbook::class.java);

        // Выполняем переход
        startActivity(intent);
    }

    fun startCompassActivity(v: View) {
        // Создаем объект класса
        val intent = Intent(this, CompassFrameActivity::class.java);
        // Выполняем переход
        startActivity(intent);
    }

    fun startDeveloperActivity(v: View) {
        // Создаем объект класса
        val intent = Intent(this, developer::class.java);
        // Выполняем переход
        startActivity(intent);
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
        // Опционально, не требует исправления
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

    data class CellLocationResponse(
        val lat: Double,
        val lon: Double
    )


    companion object {
        interface OpenCellIdApiService {
            @GET("cell")
            suspend fun getCellLocation(
                @Query("key") apiKey: String,
                @Query("mnc") mnc: Int,
                @Query("mcc") mcc: Int,
                @Query("lac") lac: Int,
                @Query("cellid") cellId: Int
            ): Response<CellLocationResponse>
        }
    }

    private fun showLocation() {
        // Получите данные ячейки сотовой связи (MNC, MCC, LAC, CellId)

        val openCellIdApiService = Retrofit.Builder()
            .baseUrl("https://us1.unwiredlabs.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenCellIdApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val apiKey = "pk.ed4e5acfb2abf2598f44b02dd20dc73d"
            val mnc = 2 // ваш MNC
            val mcc = 262 // ваш MCC
            val lac = 434 // ваш LAC
            val cellId = 9200 // ваш CellId

            val response = openCellIdApiService.getCellLocation(apiKey, mnc, mcc, lac, cellId)
            if (response.isSuccessful) {
                val cellLocation = response.body()
                val latitude = cellLocation?.lat ?: 0.0
                val longitude = cellLocation?.lon ?: 0.0

                runOnUiThread {
                    val locationButton = findViewById<Button>(R.id.locationButton)
                    locationButton.text =
                        "🌍 Мое местоположение:\n\nШирота: $latitude\nДолгота: $longitude"
                    locationButton.isEnabled = true
                }
            } else {
                runOnUiThread {
                    val locationButton = findViewById<Button>(R.id.locationButton)
                    locationButton.text = "❌ Ошибка при получении местоположения"
                    locationButton.isEnabled = true
                }
            }
        }
    }
}


/*private fun checkLocationPermission() {
    locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED
    ) {
        val locationProvider = LocationManager.GPS_PROVIDER
        val lastKnownLocation = locationManager?.getLastKnownLocation(locationProvider)
        if (lastKnownLocation != null) {
            // Предыдущее известное местоположение
            showLocation(lastKnownLocation)
        } else {
            // Получить текущее местоположение
            requestLocationUpdate()
        }
    } else {
        requestLocationPermission()
    }
}

private fun requestLocationPermission() {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        LOCATION_PERMISSION_REQUEST_CODE
    )
}

override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
) {
    if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val locationProvider = LocationManager.GPS_PROVIDER
            val lastKnownLocation = if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            } else {
                locationManager?.getLastKnownLocation(locationProvider)
            }
            if (lastKnownLocation != null) {
                showLocation(lastKnownLocation)
            } else {
                requestLocationUpdate()
            }
        } else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                val locationButton = findViewById<Button>(R.id.locationButton)
                locationButton.text = "🪄\n\nОжидание доступа..."
                val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                settingsIntent.data = uri
                startActivity(settingsIntent)
            } else {
                // Обработка случая, когда пользователь отклонил запрос разрешения
                // Здесь можно показать дополнительное объяснение или выполнить другие действия

                val locationButton = findViewById<Button>(R.id.locationButton)
                locationButton.text = "⚠️\n\nВы отклонили запрос!"

            }
        }
    } else {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}


private fun requestLocationUpdate() {
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        try {
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
        } catch (e: SecurityException) {
            val locationButton = findViewById<Button>(R.id.locationButton)
            locationButton.text = "⚠️\n\nSecurityException"
            // Обработка исключения SecurityException
        }
    } else {
        val locationButton = findViewById<Button>(R.id.locationButton)
        locationButton.text = "🧩\n\nВы не предоставили доступ к локации"
    }
}

private val locationListener: LocationListener = object : LocationListener {
    override fun onLocationChanged(location: Location) {
        // Получено новое местоположение
        showLocation(location)
        locationManager?.removeUpdates(this)
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}

private fun showLocation(location: Location) {

    val latitude = String.format("%.2f", location.latitude)
    val longitude = String.format("%.2f", location.longitude)

    val locationButton = findViewById<Button>(R.id.locationButton)
    locationButton.text = "🌎 location is: \n\n$latitude широты, $longitude долготы"
}*/

//}
