# POC: Integração Híbrida (WebView & Android Nativo)

Este projeto demonstra a arquitetura de comunicação bidirecional entre uma aplicação Android nativa e uma aplicação Web encapsulada em uma `WebView`. O objetivo é validar a fluidez de dados durante um fluxo de login e a persistência de estado entre telas nativas e web.

## 🏗️ Arquitetura da Solução

A solução utiliza o conceito de **Javascript Interface** para criar uma "ponte" (Bridge) de comunicação.

### Componentes Principais:
1.  **`MainActivity` (Login Web):** Atua como o host para a aplicação de autenticação legada ou web.
2.  **`WebAppInterface` (The Bridge):** Classe mediadora que recebe eventos do JavaScript e os traduz para ações nativas (Kotlin).
3.  **`HomeActivity` (Nativo + Web):** Demonstra como o app nativo consome dados recebidos da web para injetar contexto em novas instâncias de WebView.

---

## 🔄 Fluxo de Comunicação

### 1. Web -> Nativo (Javascript Interface)
Quando o usuário realiza o login na WebView, o JavaScript da página invoca um método exposto pelo Android.

*   **Ponte Nativa:** Definida na classe `WebAppInterface`.
*   **Chamada no JS:**
    ```javascript
    if (window.AndroidBridge) {
        window.AndroidBridge.onLoginComplete("TOKEN_GERADO_NA_WEB");
    }
    ```

### 2. Nativo -> Web (Injeção de Contexto)
Após o login, o dado (Token) é passado para a `HomeActivity` nativa, que por sua vez carrega uma nova WebView injetando esse token via **Query Parameters** ou **Headers**.

*   **Implementação:**
    ```kotlin
    val homeUrl = "https://seu-portal.com.br/dashboard?token=$token"
    binding.webViewHome.loadUrl(homeUrl)
    ```

---

## 💎 Benefícios

| Benefício | Descrição |
| :--- | :--- |
| **Agilidade de Negócio** | Permite atualizar fluxos críticos (como regras de login) sem necessidade de um novo deploy na Play Store. |
| **Consistência Visual** | Mantém a mesma interface entre plataformas (Web e App) para fluxos complexos. |
| **Redução de Custo** | Reutilização de código web existente dentro de um container nativo de alta performance. |
| **Transição Suave** | Permite migrar gradualmente funcionalidades de Web para Nativo conforme a necessidade de performance. |

---

## ⚠️ Riscos e Considerações de Segurança

A integração via WebView exige cautela arquitetural:

1.  **XSS (Cross-Site Scripting):**
    *   *Risco:* Se a página carregada for comprometida, um atacante pode executar comandos nativos através da `AndroidBridge`.
    *   *Mitigação:* Restringir o uso de `addJavascriptInterface` apenas para domínios HTTPS confiáveis e usar o `@JavascriptInterface` (obrigatório desde API 17).

2.  **Performance e UX:**
    *   *Risco:* WebViews consomem mais memória e podem parecer "travadas" se o JS for pesado.
    *   *Mitigação:* Ativar `domStorageEnabled` e utilizar `WebChromeClient` para melhor renderização.

3.  **Vazamento de Credenciais:**
    *   *Risco:* Tokens expostos na URL podem ser interceptados em logs de servidor.
    *   *Mitigação:* Preferir o envio de dados sensíveis via injeção de JavaScript (`evaluateJavascript`) ou POST requests customizados.

---

## 🚀 Como Executar a POC

1.  Certifique-se de que o dispositivo/emulador tem acesso à internet.
2.  O arquivo `build.gradle.kts` já possui o **View Binding** ativado.
3.  A `MainActivity` iniciará carregando a URL de login. Ao disparar o evento `onLoginComplete`, a transição nativa ocorrerá automaticamente.

---
**Documentação elaborada para fins de validação técnica (POC).**
