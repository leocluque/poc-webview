# POC: Integração Híbrida (WebView & Android Nativo)

Este projeto demonstra a arquitetura de comunicação bidirecional entre uma aplicação Android nativa e uma aplicação Web Angular encapsulada em uma `WebView`.

## 🏗️ Configuração de Redes Locais (Importante)

Para que o emulador Android acesse o seu servidor local (Angular rodando em `localhost:4200`), utilizamos o IP de loopback especial do Android: **`10.0.2.2`**.

*   **URL de Login:** `http://10.0.2.2:4200/login`
*   **URL de Dashboard:** `http://10.0.2.2:4200/dashboard`

**Nota:** Foi habilitado o `usesCleartextTraffic="true"` no `AndroidManifest.xml` para permitir conexões HTTP (não-seguras) durante esta fase de desenvolvimento local.

---

## 🔄 Fluxo de Comunicação

### 1. Web -> Nativo (Javascript Interface)
Quando o usuário realiza o login no Angular, o serviço de integração invoca a ponte injetada pelo Android.

*   **Ponte Nativa:** Chamada `AndroidBridge`.
*   **Chamada no Angular:**
    ```typescript
    if (window.AndroidBridge) {
        window.AndroidBridge.onLoginComplete("TOKEN_GERADO_NO_ANGULAR");
    }
    ```

### 2. Nativo -> Web (Injeção de Contexto)
Após o login, o app nativo abre a `HomeActivity` e carrega a rota de dashboard passando o token via Query Parameter.

*   **URL Gerada pelo App:** `http://10.0.2.2:4200/dashboard?token=xyz`

---

## 💎 Benefícios e Riscos

| Benefício | Risco |
| :--- | :--- |
| Agilidade de deploy Web | Vulnerabilidade a XSS se o domínio for comprometido |
| Reuso de código PrimeNG | Performance inferior comparado ao nativo puro |
| Sincronização de estado entre plataformas | Exposição de dados sensíveis em URLs (Mitigado com HTTPS em prod) |

---

## 🚀 Como Executar a POC

1.  **No Lado Web (Angular):**
    *   Inicie seu projeto Angular: `npm start` ou `ng serve`.
    *   Certifique-se de que ele está acessível em `http://localhost:4200`.

2.  **No Lado Mobile (Android):**
    *   Execute o projeto através do Android Studio em um **Emulador**.
    *   O App abrirá automaticamente na tela de Login.
    *   Ao realizar o login no Angular, o app redirecionará para a `HomeActivity` nativa, que carregará o Dashboard Web.

---
**Documentação elaborada para fins de validação técnica (POC).**
