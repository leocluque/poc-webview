package com.luque.poc_webview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luque.poc_webview.databinding.ActivityMainCustomTabBinding

class MainCustomTabActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainCustomTabBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainCustomTabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnStartLogin.setOnClickListener {
            launchCustomTab()
        }

        handleDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleDeepLink(intent)
    }

    private fun launchCustomTab() {
        val url = "http://10.0.2.2:4200/login"
        val builder = CustomTabsIntent.Builder()
        builder.setShowTitle(true)
        builder.setShareState(CustomTabsIntent.SHARE_STATE_OFF)
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    private fun handleDeepLink(intent: Intent?) {
        val data: Uri? = intent?.data
        if (data != null && data.scheme == "pocwebview" && data.host == "callback") {
            // Se o deep link pedir navegação para a SecondActivity (vindo do Dashboard em Custom Tab)
            val action = data.getQueryParameter("action")
            if (action == "navigateToSecondActivity") {
                startActivity(Intent(this, SecondActivity::class.java))
                return
            }

            // Caso padrão: Retorno do Login com Token
            val token = data.getQueryParameter("token")
            if (token != null) {
                proceedToHome(token)
            }
        }
    }

    private fun proceedToHome(token: String) {
        Toast.makeText(this, "Login via Custom Tab com sucesso!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("USER_TOKEN", token)
            putExtra("ORIGIN", "CUSTOM_TAB") // Sinalizador de origem
        }
        startActivity(intent)
        finish()
    }
}