package com.luque.poc_webview

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luque.poc_webview.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        // Usando a interface centralizada também na Home
        // Agora o Dashboard pode chamar AndroidBridge.navigateToSecondActivity()
        binding.webViewHome.addJavascriptInterface(
            WebAppInterface(this), 
            "AndroidBridge"
        )

        binding.webViewHome.webViewClient = WebViewClient()

        // Ajuste para o localhost do computador (via Emulador Android)
        val homeUrl = "http://10.0.2.2:4200/dashboard?token=$token"
        binding.webViewHome.loadUrl(homeUrl)
    }
}