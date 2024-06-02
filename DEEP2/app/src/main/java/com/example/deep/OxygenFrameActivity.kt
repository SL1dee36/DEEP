package com.example.deep

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class OxygenFrameActivity : AppCompatActivity() {

    private lateinit var vol: SeekBar
    private lateinit var volOut: TextView

    private lateinit var difficult: SeekBar
    private lateinit var difficultOut: TextView

    private lateinit var pressures: SeekBar
    private lateinit var pressuresOut: TextView

    private lateinit var term: SeekBar
    private lateinit var termOut: TextView

    private var volumeT: Int = 0
    private var difficulty: String = ""
    private var pressure: Double = 0.0
    private var termal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oxygen_frame)

        val button = findViewById<Button>(R.id.generateResult)
        button.setOnClickListener {
            generateResultMessage()
        }

        vol = findViewById(R.id.vol)
        volOut = findViewById(R.id.volOut)

        vol.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                volumeT = (progress * 5) + 5
                volOut.text = "Суммарная вместимость баллонов:\n ≈ $volumeT литров"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Ничего не делаем
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Ничего не делаем
            }
        })

        difficult = findViewById(R.id.difficult)
        difficultOut = findViewById(R.id.difficultOut)

        difficult.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                difficulty = when (progress) {
                    0 -> "Лёгкая"
                    1 -> "Средняя"
                    2 -> "Тяжёлая"
                    else -> ""
                }

                difficultOut.text = "Уровень нагрузки: $difficulty"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Ничего не делаем
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Ничего не делаем
            }
        })

        pressures = findViewById(R.id.pressures)
        pressuresOut = findViewById(R.id.pressuresOut)

        pressures.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pressure = (progress * 0.5) + 2
                pressuresOut.text = "Давление срабатывания указателя минимального давления: $pressure МПа"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Ничего не делаем
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Ничего не делаем
            }
        })

        term = findViewById(R.id.term)
        termOut = findViewById(R.id.termOut)

        term.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                termal = progress * 5
                termOut.text = "Выберите температуру: $termal℃"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Ничего не делаем
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Ничего не делаем
            }
        })
    }

    private fun generateResultMessage() {

        val result = findViewById<TextView>(R.id.resultView)

        val depthpicker = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.depth_picker)
        val timepicker = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.time_picker)
        var depth: Int = 0
        var time: Int = 0


        try {
            depth = depthpicker.text.toString().toInt()
            time = timepicker.text.toString().toInt()
        } catch (e: NumberFormatException) {
            // Handle the case when the input is not a valid number
            // You can show an error message, set default values, or take any other appropriate action
            // For example:
            depth = 0
            time = 0
            // Show an error message to the user
            Toast.makeText(this, "Invalid input. Please enter numeric values.", Toast.LENGTH_SHORT).show()
        }

        val table = arrayOf(
            intArrayOf(30, 40, 60),
            intArrayOf(25, 35, 55),
            intArrayOf(20, 30, 50),
            intArrayOf(20, 30, 50)
        )

        var i = 0
        var j = 0

        if (termal < 10) {
            i = 0
        } else if (termal >= 10 && termal < 15) {
            i = 1
        } else if (termal >= 15 && termal <= 19) {
            i = 2
        } else if (termal > 19 && termal <= 25) {
            i = 3
        }

        when (difficulty) {
            "Лёгкая"  -> j = 0
            "Средняя" -> j = 1
            "Тяжёлая" -> j = 2
        }
        try {

            if (volumeT < 5) {
                volumeT = 5
            }
            if (pressure < 2.0) {
                pressure = 5.0
            }

            val volumeA = ((pressure * 9.9 * volumeT)*100).roundToInt() / 100.0
            //val volumeRP = ((volumeA / volumeT * 0.1)*100).roundToInt() / 100.0
            val volume = (((depth * 0.1 + 1) * table[i][j] * time + volumeA)*100).roundToInt() / 100.0
            val volumeP = (((volume / volumeT) * 0.1)*100).roundToInt() / 100.0
            val volumeS = ((volume + volumeA)*100).roundToInt() / 100.0
            //val volumeSP = ((volumeS / volumeT * 0.1)*100).roundToInt() / 100.0


            val generatedMessage = "Рабочий объем воздуха: $volume л\n" +
                    //"Рабочий объем воздуха: $volumeP МПа\n" +
                    "Резервный запас: $volumeA л\n" +
                    //"Резервный запас: $volumeRP МПа\n" +
                    "Количество воздуха: $volumeS л\n"
                    //"Количество воздуха: $volumeSP МПа"
            result.text = "Сгенерированный ответ: \n\n$generatedMessage"

        } catch (e: Exception) {

            val volumeA = 0
            val volumeRP = 0
            val volume = 0
            val volumeP = 0
            val volumeS = 0
            val volumeSP = 0

            val generatedMessage = "Непредвиденная ошибка!\nПроверьте указаны ли (глубина,время)"
            result.text = "$generatedMessage"
            // Show an error message to the user
            // Toast.makeText(this, "Невозможно обработать результат!!", Toast.LENGTH_SHORT).show()
        }
    }


    fun goBack(v: View) {
            // Возвращаемся назад
            finish()
    }
}
