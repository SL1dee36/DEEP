package com.example.deep

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class developer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer)

        val videoView = findViewById<VideoView>(R.id.videoView)
        videoView.setVideoURI(Uri.parse("android.resource://${packageName}/${R.raw.background}"))
        videoView.start()
        videoView.setOnPreparedListener { mp -> mp.isLooping = false } // Циклическое воспроизведение
    }


    fun startMainActivity(v: View) {
        // Возвращаемся назад
        finish()
    }

    /*data class AppUpdate(
        val version: String,
        val link: String
    )

    interface APIService {
        @GET("SL1dee36/DEEP/main/version.json")
        fun getAppUpdate(): Call<AppUpdate>
    }

    private val nowVer = "#02032024h7"

    fun checkUpdate(context: Context) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIService::class.java)
        val call = service.getAppUpdate()

        call.enqueue(object : Callback<AppUpdate> {
            override fun onResponse(call: Call<AppUpdate>, response: Response<AppUpdate>) {
                if (response.isSuccessful) {
                    val appUpdate = response.body()
                    if (appUpdate != null && appUpdate.version != nowVer) {
                        // Новая версия доступна, выведите уведомление
                        val notification = "Доступно обновление. Перейдите по ссылке для загрузки: ${appUpdate.link}"
                        Toast.makeText(context, notification, Toast.LENGTH_SHORT).show()
                    } else if (appUpdate != null && appUpdate.version == nowVer) {
                        Toast.makeText(context, "Текущая версия является последней", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<AppUpdate>, t: Throwable) {
                // Обработка ошибки запроса
                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
            }
        })
    }*/

    /*fun startMotionRoom(v: View) {
        val intent = Intent(this, MotionRoom::class.java)
        startActivity(intent)
    }*/

    /*fun startClickRoom(v: View) {
        val intent = Intent(this, ClickerRoom::class.java)
        startActivity(intent)
    }*/

    /*fun onUpdButtonClick(v: View) {
        // Проверка обновлений при нажатии на кнопку updbutton
        checkUpdate(v.context) // Передача контекста кнопки в качестве параметра
    }*/
}