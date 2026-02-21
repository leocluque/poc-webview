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

        // Pegamos a informação que veio da MainActivity (nativo)
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

        // Exemplo: Abrir uma nova URL enviando o token como parâmetro (informação do nativo)
        // Isso simula a integração de enviar dados nativos para a Web.
        val homeUrl = "https://seu-portal.com.br/dashboard?token=$token"
        binding.webViewHome.loadUrl(homeUrl)
    }
}
