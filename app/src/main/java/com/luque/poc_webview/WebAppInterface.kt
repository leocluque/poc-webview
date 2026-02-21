package com.luque.poc_webview

import android.webkit.JavascriptInterface

class WebAppInterface(private val onLoginSuccess: (String) -> Unit) {

    /** * O nome deste método deve ser combinado com o dev Web.
     * O @JavascriptInterface é obrigatório para segurança.
     */
    @JavascriptInterface
    fun onLoginComplete(token: String) {
        // Aqui recebemos o dado da Web e repassamos para a Activity
        onLoginSuccess(token)
    }
}
