package com.example.deep

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.abs

class DecompressionFrameActivity : AppCompatActivity() {
                                  // 0   1   2   3   4   5   6   7   8   9   10   11   12   13   14  15  16  17
    private val arrdeep = intArrayOf(0, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42,  45,  48,  51,  54,  57, 60)
    private val arrtime = intArrayOf(0,  5, 10, 15, 20, 25, 30, 35, 40, 45, 60, 80, 105, 145, 180, 240, 360)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decompression_frame)

        val button = findViewById<Button>(R.id.getresult_btn)
        button.setOnClickListener {
            getResult()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getResult() {
        val depthPicker = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.depth_picker)
        val timePicker = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.time_picker)
        val decompResult = findViewById<TextView>(R.id.decomp_result)

        try {
            var depth = depthPicker.text.toString().toInt()
            var time = timePicker.text.toString().toInt()


            if ((depth < 12 && time <= 360) || (depth <= 15 && time <= 105)) {
                decompResult.text = "Эта глубина позволяет вам всплыть без дополнительных методов декомпрессии."
            } else if ((depth > 60 || time > 360) || (depth == 60 && time > 60)) {
                decompResult.text = "Указанные вами данные вне доступного нами диапазона!"
            } else if ((depth == 18 && time == 45) ||
                       (depth == 21 && time == 35) ||
                       (depth == 24 && time == 25) ||
                       (depth == 27 && time == 20) ||
                       (depth == 30 && time == 15) ||
                       (depth == 33 && time == 15) ||
                       (depth == 36 && time == 10) ||
                       (depth == 39 && time == 10) ||
                       (depth == 42 && time == 10) ||
                       (depth == 45 && time == 10) ||
                       (depth == 48 && time == 5)  ||
                       (depth == 51 && time == 5)  ||
                       (depth == 54 && time == 5)  ||
                       (depth == 57 && time == 5)  ||
                       (depth == 60 && time == 5)    ) {

                depth -= 3
                if (depth == 15) {
                    time = 240
                } else if (depth == 18 || depth == 21 || depth == 24) {
                    time = 180
                } else if (depth == 27 || depth == 30 || depth == 33) {
                    time = 145
                } else if (depth == 36 || depth == 39 || depth == 42 || depth == 45 || depth == 48) {
                    time = 105
                } else if (depth == 51 || depth == 54) {
                    time = 80
                } else if (depth == 57) {
                    time = 60
                }

                val nD = findNearestValue(depth, arrdeep)
                val nT = findNearestValue(time, arrtime)
                val indexD = arrdeep.indexOf(nD)
                val indexT = arrtime.indexOf(nT)

                table(nD, nT) // Запускает функцию проверки внутри таблицы и вывода из неё

            } else {

                val nD = findNearestValue(depth, arrdeep)
                val nT = findNearestValue(time, arrtime)
                val indexD = arrdeep.indexOf(nD)
                val indexT = arrtime.indexOf(nT)

                table(nD, nT) // Запускает функцию проверки внутри таблицы и вывода из неё
            }

        } catch (e: NumberFormatException) {
            decompResult.text = "🤔 Кажется, вы не указали Глубину/Время"
        }
    }

    private fun findNearestValue(value: Int, array: IntArray): Int {
        var nearestValue = array[0]
        var minDifference = abs(value - array[0])

        for (index in 1 until array.size) {
            val difference = abs(value - array[index])
            if (difference < minDifference) {
                nearestValue = array[index]
                minDifference = difference
            }
        }
        return nearestValue
    }

    private fun table(nD: Int, nT: Int) {
        val decompResult = findViewById<TextView>(R.id.decomp_result)
        val inputStream = assets.open("mode.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val lines = reader.readLines()

        var message = ""
        var elementsCount = 0
        var step = 0
        var previousStep = 0 // Variable to store previous step value
        for (line in lines) {
            val column = line.split(",")
            if (column[0].contains(nD.toString()) && column[1].contains(nT.toString())) {
                val trimmedColumn = column.drop(3) // Удаляем первые три элемента
                val reversedString = StringBuilder()
                for (i in trimmedColumn.indices.reversed()) {
                    val cleanedElement = trimmedColumn[i].replace("\"", "").trim()
                    if (cleanedElement.isNotEmpty()) {
                        reversedString.append(cleanedElement).append(" ")
                    }
                }
                val elements = reversedString.trim().toString().split(" ")
                val elementCount = elements.size
                step = elementCount

                for (i in elements.indices.reversed()) {
                    val num = elements[i]

                    if (num.none { it.isDigit() || it == '(' || it == ')' }) {
                        // If num doesn't contain digits or parentheses
                        step = previousStep // Set step to the previous step value
                    }

                    if (i == 0) {
                        message += "[$step] Всплывайте на 3 м-а с выдержкой: $num\n\n"
                    } else {
                        val depth = (i + 1) * 3
                        step = elementCount - i
                        message += "[$step] Всплывайте на $depth м-ов с выдержкой: $num\n\n"
                    }
                }
                break
            }
            previousStep = step // Update the previous step value
        }
        decompResult.text = "Рекомендуем двигаться по следующей системе:\n\n$message"
    }


    fun startAquaActivity(v: View) {
        finish()
    }
}
