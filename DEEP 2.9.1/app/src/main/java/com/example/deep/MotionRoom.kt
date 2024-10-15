package com.example.deep

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MotionRoom : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_room)

        /*val videoView = findViewById<VideoView>(R.id.videoView)
        videoView.setVideoURI(Uri.parse("android.resource://${packageName}/${R.raw.bgm}"))
        videoView.start()
        videoView.setOnPreparedListener { mp -> mp.isLooping = false } // Циклическое воспроизведение*/

    }

    fun startMainActivity(v: View) {
        // Возвращаемся назад
        finish()
    }

}