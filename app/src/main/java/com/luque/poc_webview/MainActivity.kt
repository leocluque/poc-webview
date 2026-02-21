package com.luque.poc_webview

import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.luque.poc_webview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWebView()
    }

    private fun setupWebView() {
        binding.webViewLogin.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        // "AndroidBridge" será o objeto acessível no JavaScript da WebView
        binding.webViewLogin.addJavascriptInterface(WebAppInterface { token ->
            handleLoginSuccess(token)
        }, "AndroidBridge")

        binding.webViewLogin.loadUrl("https://seu-site-de-login.com.br")

        binding.webViewLogin.webViewClient = WebViewClient()
    }

    private fun handleLoginSuccess(token: String) {
        // As chamadas vindas do JavaScript rodam em uma thread separada.
        // Precisamos voltar para a Main Thread para operações de UI e Navegação.
        runOnUiThread {
            Toast.makeText(this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("USER_TOKEN", token)
            }
            startActivity(intent)
            finish() // Encerra a tela de login para que o 'back' não volte pra ela
        }
    }
}