package com.luque.poc_webview

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.luque.poc_webview.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userToken = intent.getStringExtra("USER_TOKEN") ?: "Guest"
        
        setupHomeUI(userToken)
        setupWebView(userToken)
    }

    private fun setupHomeUI(token: String) {
        binding.tvWelcome.text = "Olá, Token: $token"
    }

    private fun setupWebView(token: String) {
        binding.webViewHome.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        binding.webViewHome.webViewClient = WebViewClient()

        // Ajuste para o localhost do computador (via Emulador Android)
        val homeUrl = "http://10.0.2.2:4200/dashboard?token=$token"
        binding.webViewHome.loadUrl(homeUrl)
    }
}