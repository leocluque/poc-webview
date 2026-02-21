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

        binding.webViewLogin.addJavascriptInterface(WebAppInterface { token ->
            handleLoginSuccess(token)
        }, "AndroidBridge")

        // Para acessar o localhost do seu computador via Emulador Android,
        // use o IP especial 10.0.2.2 em vez de localhost ou 127.0.0.1.
        binding.webViewLogin.loadUrl("http://10.0.2.2:4200/login")

        binding.webViewLogin.webViewClient = WebViewClient()
    }

    private fun handleLoginSuccess(token: String) {
        runOnUiThread {
            Toast.makeText(this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("USER_TOKEN", token)
            }
            startActivity(intent)
            finish()
        }
    }
}