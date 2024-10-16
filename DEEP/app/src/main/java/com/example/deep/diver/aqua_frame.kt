package com.example.deep.diver

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.deep.R

class aqua_frame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aqua)
    }


    // КНОПКИ меню:

    fun startMainActivity(v: View) {
        // Возвращаемся назад
        finish();
    }

    fun startDecompressActivity(v: View) {
        // Создаем объект класса
        val intent = Intent(this, DecompressionFrameActivity::class.java);
        // Выполняем переход
        startActivity(intent);
    }

    fun startOxygenActivity(v: View) {
        // Создаем объект класса
        val intent = Intent(this, OxygenFrameActivity::class.java);
        // Выполняем переход
        startActivity(intent);
    }


}