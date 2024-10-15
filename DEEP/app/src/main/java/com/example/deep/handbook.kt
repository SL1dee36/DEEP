package com.example.deep

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.deep.databinding.ActivityHandbookBinding

class handbook : AppCompatActivity() {

    private lateinit var binding: ActivityHandbookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHandbookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Включаем кнопку "назад"

        val webView: WebView = binding.webview
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://агз-эсс.рф")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Обрабатываем нажатие на кнопку "назад"
        return true
    }
}