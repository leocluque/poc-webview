# POC: Estratégias de Integração Híbrida (WebView vs. Custom Tabs)

Este projeto é uma Prova de Conceito (POC) avançada que demonstra as duas principais abordagens para integrar aplicações Web (Angular) em um ecossistema Android Nativo, com foco em arquitetura escalável e segurança.

---

## 🏛️ Comparativo Detalhado: WebView vs. Custom Tabs

| Característica | WebView | Chrome Custom Tabs |
| :--- | :--- | :--- |
| **Escopo** | Componente interno (Container) | Instância otimizada do navegador Chrome |
| **Comunicação** | **Bridge JS:** Bidirecional direta. | **Deep Links:** URIs customizadas. |
| **Experiência** | 100% Integrada (Invisível) | Transição Suave (Barra de URL visível*) |
| **Segurança** | **Risco Elevado** | **Alta Segurança (Padrão Oauth)** |
| **Sessão/Cookies** | Isolada do navegador | Compartilhada com o Chrome |

### ⚡ Performance e Latência
Abaixo, uma comparação do tempo de inicialização. Note como a **Custom Tab** (à esquerda) carrega quase instantaneamente comparada à **WebView** (à direita), devido ao pré-aquecimento do motor do Chrome.

![Comparativo de Latência: Custom Tab vs Chrome vs WebView](https://miro.medium.com/v2/resize:fit:1280/format:webp/1*PJAu8wk0e6AjWqiJTnxWYQ.gif)

---

## 🔐 Diferenças Críticas de Segurança

### 1. WebView: Controle Total vs. Vulnerabilidade
*   **Vulnerabilidade XSS:** Se o código JavaScript da página for comprometido, um atacante pode usar a `JavascriptInterface` para executar comandos nativos.
*   **Interceptação de Dados:** O App Android tem poder total sobre a WebView. Ele pode injetar scripts para capturar senhas ou interceptar requisições.

### 2. Custom Tabs: O "Cofre" do Navegador
*   **Isolamento (Sandboxing):** O App Android **não tem acesso** ao que acontece dentro da Custom Tab. Não pode ler o DOM ou capturar cliques.
*   **Cookie Sharing:** Cenário ideal para Login (SSO). O usuário aproveita sua sessão já logada no Chrome.

---

## 🚀 Experiência Nativa com Digital Asset Links (TWA)

Para remover a barra de endereço nas Custom Tabs e obter uma experiência "Full Native", implementamos o **Digital Asset Links**. Veja a diferença visual abaixo:

![Diferença Visual com e sem Asset Link]()![customtab](https://github.com/user-attachments/assets/1a867b0b-1f8f-40d5-8316-56e73a766920)
![customtab](https://github.com/user-attachments/assets/1a867b0b-1f8f-40d5-8316-56e73a766920)


1.  **Verificação de Propriedade:** Cria-se um arquivo de confiança entre o App e o Servidor.
2.  **Configuração:** Hospedar em `https://seu-dominio.com.br/.well-known/assetlinks.json`.
3.  **Resultado:** O Chrome valida a assinatura e oculta a UI do navegador, transformando a Custom Tab em uma **Trusted Web Activity (TWA)**.

---

## 🔄 Fluxos de Comunicação Implementados

### Fluxo via WebView (Ponte Nativa)
*   **Web -> Nativo:** `window.AndroidBridge.onLoginComplete(token)`
*   **Uso Ideal:** Dashboards internos e fluxos onde a UI deve ser indistinguível.

### Fluxo via Custom Tabs (Redirecionamento)
*   **Web -> Nativo:** Redirecionamento via URI Scheme: `pocwebview://callback?token=XYZ`
*   **Uso Ideal:** Autenticação (OAuth2), Pagamentos e Conteúdos de Terceiros.

---

## 🛠️ Detalhes Técnicos
*   **Rede:** Uso de `10.0.2.2:4200` para acesso ao localhost via emulador.
*   **Resiliência:** Tratamento nativo de erros na WebView com tela de "Retry".
*   **Navegação:** A `HomeActivity` detecta a origem e adapta a UI automaticamente.

---
**Documentação estratégica elaborada para fins de arquitetura de solução.**
