package com.luque.poc_webview

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luque.poc_webview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupWebView()
        setupRetryButton()
    }

    private fun setupRetryButton() {
        binding.btnRetry.setOnClickListener {
            binding.errorLayout.visibility = View.GONE
            binding.webViewLogin.visibility = View.VISIBLE
            binding.webViewLogin.reload()
        }
    }

    private fun setupWebView() {
        binding.webViewLogin.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        // Usando a interface centralizada
        binding.webViewLogin.addJavascriptInterface(
            WebAppInterface(this) { token ->
                handleLoginSuccess(token)
            }, 
            "AndroidBridge"
        )

        binding.webViewLogin.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                if (request?.isForMainFrame == true) {
                    showError()
                }
            }
        }

        binding.webViewLogin.loadUrl("http://10.0.2.2:4200/login")
    }

    private fun showError() {
        runOnUiThread {
            binding.webViewLogin.visibility = View.GONE
            binding.errorLayout.visibility = View.VISIBLE
        }
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