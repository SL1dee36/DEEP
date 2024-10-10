package com.example.deep

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
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
    private lateinit var timePicker: TextInputEditText
    private lateinit var depthPicker: TextInputEditText
    private lateinit var resultView: TextView
    private lateinit var generateButton: Button
    private lateinit var radioButton: RadioButton

    private var volumeT: Int = 300
    private var difficulty: String = "Лёгкая"
    private var pressure: Double = 5.0
    private var termal: Int = 10

    private var isMPa = false
    private var lastGeneratedMessage = ""

    // Заранее установленные значения
    private val defaultTime = 60 // Например, 60 минут
    private val defaultDepth = 10 // Например, 10 метров


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oxygen_frame)

        // Инициализация view элементов
        vol = findViewById(R.id.vol)
        volOut = findViewById(R.id.volOut)
        difficult = findViewById(R.id.difficult)
        difficultOut = findViewById(R.id.difficultOut)
        pressures = findViewById(R.id.pressures)
        pressuresOut = findViewById(R.id.pressuresOut)
        term = findViewById(R.id.term)
        termOut = findViewById(R.id.termOut)
        timePicker = findViewById(R.id.time_picker)
        depthPicker = findViewById(R.id.depth_picker)
        resultView = findViewById(R.id.resultView)
        generateButton = findViewById(R.id.generateResult)
        radioButton = findViewById(R.id.radioButton)


        // Устанавливаем начальные значения для TextView
        volOut.text = "Суммарная вместимость баллонов:\n ≈ $volumeT литров"
        difficultOut.text = "Уровень нагрузки: $difficulty"
        pressuresOut.text = "Давление срабатывания: $pressure МПа"
        termOut.text = "Выберите температуру: $termal℃"

        // Устанавливаем значения по умолчанию для TextInputEditText
        timePicker.setText(defaultTime.toString())
        depthPicker.setText(defaultDepth.toString())

        radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                isMPa = isChecked
            }

            else if (buttonView.isPressed && isChecked) {
                isMPa = !isMPa
            }

            generateResultMessage()
            radioButton.text = if (isMPa) "Перевести в литры" else "Перевести в МПа"
        }

        generateButton.setOnClickListener {
            generateResultMessage()
        }

        vol.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                volumeT = (progress * 5) + 100
                volOut.text = "Суммарная вместимость баллонов:\n ≈ $volumeT литров"
                generateResultMessage()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        difficult.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                difficulty = when (progress) {
                    0 -> "Лёгкая"
                    1 -> "Средняя"
                    2 -> "Тяжёлая"
                    else -> ""
                }
                difficultOut.text = "Уровень нагрузки: $difficulty"
                generateResultMessage()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        pressures.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pressure = (progress * 0.5) + 2.0
                pressuresOut.text = "Давление срабатывания: $pressure МПа"
                generateResultMessage()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        term.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                termal = progress * 5
                termOut.text = "Выберите температуру: $termal℃"
                generateResultMessage()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun generateResultMessage() {
        val depth = depthPicker.text.toString().toIntOrNull() ?: 10
        val time = timePicker.text.toString().toIntOrNull() ?: 60

        val table = arrayOf(
            intArrayOf(30, 40, 60),
            intArrayOf(25, 35, 55),
            intArrayOf(20, 30, 50),
            intArrayOf(20, 30, 50)
        )

        val i = when {
            termal < 10 -> 0
            termal in 10..14 -> 1
            termal in 15..19 -> 2
            termal in 20..25 -> 3
            else -> 0
        }

        val j = when (difficulty) {
            "Лёгкая" -> 0
            "Средняя" -> 1
            "Тяжёлая" -> 2
            else -> 0
        }

        val volumeA = (pressure * 9.9 * volumeT).roundTo(2)
        val volume = ((depth * 0.1 + 1) * table[i][j] * time + volumeA).roundTo(2)
        val volumeS = (volume + volumeA).roundTo(2)


        val generatedMessage = if (isMPa) {
            val volumeP = (volume / volumeT * 0.1).roundTo(2)
            val volumeRP = (volumeA / volumeT * 0.1).roundTo(2)
            val volumeSP = (volumeS / volumeT * 0.1).roundTo(2)

            "Рабочий объем воздуха: $volumeP МПа\n" +
                    "Резервный запас: $volumeRP МПа\n" +
                    "Количество воздуха: $volumeSP МПа"
        } else {
            "Рабочий объем воздуха: $volume л\n" +
                    "Резервный запас: $volumeA л\n" +
                    "Количество воздуха: $volumeS л"
        }


        val newMessage = "Сгенерированный ответ: \n\n$generatedMessage"
        if (lastGeneratedMessage != newMessage) {
            resultView.text = newMessage
            lastGeneratedMessage = newMessage
        }

    }



    private fun Double.roundTo(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return (this * multiplier).roundToInt() / multiplier
    }


    fun goBack(v: View) {
        finish()
    }
}