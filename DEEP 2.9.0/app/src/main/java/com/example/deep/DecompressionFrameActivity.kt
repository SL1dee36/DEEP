package com.example.deep

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.abs
import kotlin.random.Random

class DecompressionFrameActivity : AppCompatActivity() {
    private val arrdeep = intArrayOf(12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42, 45, 48, 51, 54, 57, 60)
    private val arrtime = intArrayOf(5, 10, 15, 20, 25, 30, 35, 40, 45, 60, 80, 105, 145, 180, 240, 360)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decompression_frame)

        val button = findViewById<Button>(R.id.getresult_btn)
        val depthPicker = findViewById<TextInputEditText>(R.id.depth_picker)
        val timePicker = findViewById<TextInputEditText>(R.id.time_picker)

        // Выбор случайных значений глубины (17-32 м) и времени (30-60 мин)
        val randomDepth = getRandomDepth(17, 32)
        val randomTime = getRandomTime(30, 60)

        // Установка этих значений в поля ввода
        depthPicker.setText(randomDepth.toString())
        timePicker.setText(randomTime.toString())

        getResult()

        button.setOnClickListener {
            getResult()
        }
    }

    // Функция для выбора случайной глубины в диапазоне
    private fun getRandomDepth(min: Int, max: Int): Int {
        return Random.nextInt(min, max + 1)
    }

    // Функция для выбора случайного времени в диапазоне
    private fun getRandomTime(min: Int, max: Int): Int {
        return Random.nextInt(min, max + 1)
    }

    @SuppressLint("SetTextI18n")
    private fun getResult() {
        val depthPicker = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.depth_picker)
        val timePicker = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.time_picker)
        val decompResult = findViewById<TextView>(R.id.decomp_result)

        try {
            val depth = depthPicker.text.toString().toInt()
            val time = timePicker.text.toString().toInt()

            if (depth < 12 || (depth <= 15 && time <= 105)) {
                decompResult.text = "Эта глубина позволяет вам всплыть без декомпрессии."
                return
            }

            if (depth > 60 || time > 360 || (depth == 60 && time > 60)) {
                decompResult.text = "Данные вне доступного диапазона!"
                return
            }

            val nearestDepth = findNearestValue(depth, arrdeep)
            val nearestTime = findNearestValue(time, arrtime)

            val decompressionStops = table(nearestDepth, nearestTime)

            if (decompressionStops.isEmpty()) {
                decompResult.text = "Не найдено данных для декомпрессии."
            } else {
                var message = ""
                for ((step, stopInfo) in decompressionStops.withIndex().reversed()) {
                    val depthStop = (step + 1) * 3
                    message += "[${step + 1}] Всплывайте на $depthStop м с выдержкой: $stopInfo мин.\n\n"
                }
                decompResult.text = "Рекомендуем следующий алгоритм всплытия:\n\n$message"
            }

        } catch (e: NumberFormatException) {
            decompResult.text = "Укажите Глубину и Время"
        }
    }

    private fun findNearestValue(value: Int, array: IntArray): Int {
        var nearestValue = array[0]
        var minDifference = abs(value - array[0])

        for (element in array) {
            val difference = abs(value - element)
            if (difference < minDifference) {
                nearestValue = element
                minDifference = difference
            }
        }
        return nearestValue
    }

    private fun table(nD: Int, nT: Int): List<String> {
        val inputStream = assets.open("mode.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val lines = reader.readLines()

        for (line in lines) {
            val columns = line.split(",").map { it.trim().replace("\"", "") }
            if (columns[0].toIntOrNull() == nD && columns[1].toIntOrNull() == nT) {
                return columns.drop(3).reversed().filter { it.isNotEmpty() }
            }
        }
        return emptyList()
    }

    fun startAquaActivity(v: View) {
        finish()
    }
}
