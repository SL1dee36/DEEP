package com.example.deep.diver

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.deep.R
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
                generateResultMessage()
                radioButton.text = if (isMPa) "Перевести в литры" else "Перевести в МПа"
            }
        }

        generateButton.setOnClickListener {
            generateResultMessage()
        }

        vol.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                volumeT = (progress * 160) + 300
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

        val volumeA = volumeT * pressure
        val baseVolume = ((depth * 0.1 + 1) * table[i][j] * time).roundTo(2)
        val volume = minOf(baseVolume, volumeT.toDouble()) // Ограничение по объему

        val airConsumptionPerMinute = ((depth * 0.1 + 1) * table[i][j]).roundTo(2)
        val totalMinutes = (volumeT / airConsumptionPerMinute).roundTo(2)

        // Дополнительные рекомендации
        val recommendations = StringBuilder()
        if (volume < baseVolume) {
            recommendations.append("Рекомендуется увеличить объем баллона или уменьшить глубину/время погружения.\n")
        }
        if (difficulty == "Тяжёлая" && termal >= 20) {
            recommendations.append("Высокая нагрузка при высокой температуре. Рассмотрите возможность снижения нагрузки.\n")
        }
        if (pressure < 3.0) {
            recommendations.append("Давление слишком низкое. Рекомендуется увеличить давление до 3.0 МПа или выше для более безопасного погружения.\n")
        }
        if (recommendations.isEmpty()) {
            recommendations.append("Отлично! В данный момент у нас нет подходящих рекомендаций.\n")
        }

        val generatedMessage = if (isMPa) {
            val volumeP = (volume / volumeT * pressure).roundTo(2)
            val volumeRP = (volumeA / volumeT * pressure).roundTo(2)
            val volumeSP = ((volume + volumeA) / volumeT * pressure).roundTo(2)

            "Рабочий объем воздуха: $volumeP МПа\n" +
                    "Резервный запас: $volumeRP МПа\n" +
                    "Общий объем воздуха: $volumeSP МПа\n" +
                    "\n" +
                    "Потребление воздуха на минуту: $airConsumptionPerMinute МПа\n" +
                    "Общее количество минут работы: $totalMinutes минут"
        } else {
            "Рабочий объем воздуха: $volume л\n" +
                    "Резервный запас: $volumeA л\n" +
                    "Общий объем воздуха: ${volume + volumeA} л\n" +
                    "\n" +
                    "Потребление воздуха на минуту: $airConsumptionPerMinute л\n" +
                    "Общее количество минут работы: $totalMinutes минут"
        }

        val newMessage = "\nСгенерированный ответ:\n\n$generatedMessage\n\n\nРекомендации:\n\n$recommendations"
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
