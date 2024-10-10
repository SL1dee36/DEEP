package com.example.deep

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class mineoff : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine)
    }

    fun goBack(v: View) {
        // Возвращаемся назад
        finish()
    }

}