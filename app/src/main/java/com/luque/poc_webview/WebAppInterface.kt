package com.luque.poc_webview

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface

/**
 * Interface centralizada para comunicação entre a WebView e o Android.
 * Todas as ações disparadas via JavaScript devem passar por aqui.
 */
class WebAppInterface(
    private val context: Context,
    private val onLoginSuccess: ((String) -> Unit)? = null
) {

    private val mainHandler = Handler(Looper.getMainLooper())

    /**
     * Ação: Login concluído na Web.
     * Envia o token para o código nativo processar.
     */
    @JavascriptInterface
    fun onLoginComplete(token: String) {
        mainHandler.post {
            onLoginSuccess?.invoke(token)
        }
    }

    /**
     * Ação: Navegação para uma Activity nativa.
     * Centraliza a lógica de navegação solicitada pela Web.
     */
    @JavascriptInterface
    fun navigateToSecondActivity() {
        mainHandler.post {
            val intent = Intent(context, SecondActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    /**
     * Outras funções escaláveis podem ser adicionadas aqui:
     * Ex: showToast(), shareData(), closeApp(), etc.
     */
}