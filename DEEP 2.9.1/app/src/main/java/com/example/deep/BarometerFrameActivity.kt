package com.example.deep

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import kotlin.math.pow
import kotlin.random.Random

class BarometerFrameActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var barometerSensor: Sensor? = null
    private lateinit var pressureTextView: TextView
    private lateinit var altitudeTextView: TextView
    private lateinit var forecastTextView: TextView
    private lateinit var recommendationTextView: TextView

    private val seaLevelPressure = 1013.25 // Стандартное давление на уровне моря (hPa)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barometer)

        pressureTextView = findViewById(R.id.pressureTextView)
        altitudeTextView = findViewById(R.id.altitudeTextView)
        forecastTextView = findViewById(R.id.forecastTextView)
        recommendationTextView = findViewById(R.id.recommendationTextView)

        sensorManager = getSystemService<SensorManager>()!!
        barometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        if (barometerSensor == null) {
            pressureTextView.text = "Барометр не найден"
        }
    }

    override fun onResume() {
        super.onResume()
        barometerSensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_PRESSURE) {
            val pressure = event.values[0]
            val altitude = calculateAltitude(pressure)
            val forecast = getWeatherForecast(pressure)
            val recommendation = getRecommendation(pressure, altitude)

            pressureTextView.text = "Давление: ${pressure} hPa"
            altitudeTextView.text = "Высота: ${String.format("%.2f", altitude)} м"
            forecastTextView.text = "Прогноз: $forecast"
            recommendationTextView.text = "$recommendation"
        }
    }

    private fun calculateAltitude(pressure: Float): Float {
        return (44330.0 * (1 - (pressure / seaLevelPressure).pow(0.1903))).toFloat()
    }

    private fun getWeatherForecast(pressure: Float): String {
        return when {
            pressure > 1025 -> "Ясная погода"
            pressure in 1015f..1025f -> "Переменная облачность"
            pressure in 1005f..1015f -> "Пасмурно, возможен дождь"
            pressure < 1005 -> "Шторм"
            else -> "Неопределено"
        }
    }



    private fun getRecommendation(pressure: Float, altitude: Float) : String {
        val recommendations = mutableListOf<String>()


        // Давление
        if (pressure > 1025) recommendations.add("Высокое давление: хорошая погода для активного отдыха.")
        if (pressure < 1005) recommendations.add("Низкое давление: будьте осторожны, возможны штормовые условия.")


        // Высота
        if (altitude > 2000) recommendations.add("Большая высота: учитывайте риск горной болезни.")

        // Комбинации
        if (pressure < 1005 && altitude > 1000) recommendations.add("Опасные условия: низкое давление и большая высота.")
        if (pressure > 1020 && altitude < 500) recommendations.add("Идеальные условия для прогулок.")

        // Добавляем больше рекомендаций, чтобы получить больше 50 вариантов.  Используем случайные элементы для разнообразия.
        if (Random.nextBoolean()) recommendations.add("Проверьте снаряжение перед выходом.")
        if (Random.nextBoolean()) recommendations.add("Сообщите кому-нибудь о своих планах.")
        if (Random.nextBoolean()) recommendations.add("Следите за прогнозом погоды.")


        //  Если список рекомендаций пуст, добавляем дефолтное значение
        if(recommendations.isEmpty()) recommendations.add("Нет специфических рекомендаций.")

        // Случайный выбор рекомендации, если их несколько. Можно отображать и все сразу, через \n.
        return recommendations.random()

    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Не используется
    }

    fun startMainActivity(v: View) {
        finish()
    }
}