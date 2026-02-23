package com.luque.poc_webview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
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
        val origin = intent.getStringExtra("ORIGIN") ?: "WEBVIEW"

        setupHomeUI(userToken)
        
        if (origin == "CUSTOM_TAB") {
            setupCustomTabUI(userToken)
        } else {
            setupWebView(userToken)
        }
    }

    private fun setupHomeUI(token: String) {
        binding.tvWelcome.text = "Olá, Token: $token"
    }

    private fun setupWebView(token: String) {
        binding.webViewHome.visibility = View.VISIBLE
        binding.webViewHome.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        binding.webViewHome.addJavascriptInterface(
            WebAppInterface(this), 
            "AndroidBridge"
        )

        binding.webViewHome.webViewClient = WebViewClient()
        val homeUrl = "http://10.0.2.2:4200/dashboard?token=$token"
        binding.webViewHome.loadUrl(homeUrl)
    }

    private fun setupCustomTabUI(token: String) {
        binding.customTabContainer.visibility = View.VISIBLE
        binding.btnOpenDashboard.setOnClickListener {
            val homeUrl = "http://10.0.2.2:4200/dashboard?token=$token"
            val builder = CustomTabsIntent.Builder()
            builder.setShowTitle(true)
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this, Uri.parse(homeUrl))
        }
    }
}