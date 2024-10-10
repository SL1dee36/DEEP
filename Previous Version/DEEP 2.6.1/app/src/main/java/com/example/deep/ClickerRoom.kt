package com.example.deep

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class ClickerRoom : AppCompatActivity() {
    private lateinit var pointTable: TextView
    private lateinit var showSpeed: TextView
    private lateinit var Score: MutableList<String>

    private var lastClickTime: Long = 0
    private var clickCount: Int = 0
    private var speed: Int = 0
    private val handler = Handler()
    private var scoreRunnable: Runnable? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicker)

        pointTable = findViewById(R.id.pointTable)
        showSpeed = findViewById(R.id.showSpeed)
        Score = mutableListOf()

        try {
            val inputStream = openFileInput("score.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String? = reader.readLine()

            while (line != null) {
                Score.add(line)
                line = reader.readLine()
            }

            pointTable.text = "${Score.size} POINTS"
        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(this, "Error reading scores", Toast.LENGTH_SHORT).show()
        }

        startSpeedIncrease()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSpeedIncrease()
    }

    fun stopClickerActivity(v: View) {
        val filename = "score.txt"
        val outputStream: FileOutputStream
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE)
            val writer = BufferedWriter(OutputStreamWriter(outputStream))

            for (score in Score) {
                writer.write(score)
                writer.newLine()
            }

            writer.flush()
            writer.close()
            outputStream.close()
            finish()

            Toast.makeText(this, "Scores saved", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(this, "Error saving scores", Toast.LENGTH_SHORT).show()
        }
    }

    fun tap(view: View) {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime < 400) {
            clickCount++
        } else {
            clickCount = 1
        }

        lastClickTime = currentTime

        speed = clickCount
        Score.add("1")
        updateScore()
    }

    private fun updateScore() {
        pointTable.text = "${Score.size} POINTS"
    }

    private fun startSpeedIncrease() {
        scoreRunnable = object : Runnable {
            override fun run() {
                updateScore()
                showSpeed.text = "+$speed per second"
                handler.postDelayed(this, 500)
                speed = 0
            }
        }

        scoreRunnable?.let {
            handler.post(it)
        }
    }

    private fun stopSpeedIncrease() {
        scoreRunnable?.let {
            handler.removeCallbacks(it)
        }
    }
}
