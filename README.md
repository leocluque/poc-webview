# POC: Estratégias de Integração Híbrida (WebView vs. Custom Tabs)

Este projeto é uma Prova de Conceito (POC) avançada que demonstra as duas principais abordagens para integrar aplicações Web (Angular) em um ecossistema Android Nativo, com foco em arquitetura escalável e segurança.

---

## 🏛️ Comparativo Detalhado: WebView vs. Custom Tabs

| Característica | WebView | Chrome Custom Tabs |
| :--- | :--- | :--- |
| **Escopo** | Componente interno (Container) | Instância otimizada do navegador Chrome |
| **Comunicação** | **Bridge JS:** Bidirecional direta. | **Deep Links:** URIs customizadas. |
| **Experiência** | 100% Integrada (Invisível) | Transição Suave (Aba do Navegador) |
| **Segurança** | **Risco Elevado** | **Alta Segurança (Padrão Oauth)** |
| **Sessão/Cookies** | Isolada do navegador | Compartilhada com o Chrome |

---

## 🔐 Diferenças Críticas de Segurança

A escolha entre uma tecnologia e outra deve ser pautada principalmente pela **origem do conteúdo** e a **sensibilidade dos dados**.

### 1. WebView: Controle Total vs. Vulnerabilidade
*   **Vulnerabilidade XSS:** Se o código JavaScript da página for comprometido, um atacante pode usar a `JavascriptInterface` para executar comandos nativos (ex: acessar câmera, contatos ou sistema de arquivos).
*   **Interceptação de Dados:** O App Android tem poder total sobre a WebView. Ele pode injetar scripts para capturar senhas digitadas ou interceptar requisições HTTP, o que é um risco se o conteúdo web não for de propriedade da empresa.
*   **Manutenção:** O motor da WebView depende da versão instalada no sistema, podendo conter vulnerabilidades não corrigidas em dispositivos antigos.

### 2. Custom Tabs: O "Cofre" do Navegador
*   **Isolamento (Sandboxing):** O App Android **não tem acesso** ao que acontece dentro da Custom Tab. Ele não pode ler o DOM, capturar cliques ou interceptar cookies.
*   **Proteção do Chrome:** Utiliza todos os recursos de segurança do Google Chrome, como *Safe Browsing* (detecção de phishing e malware) e atualizações automáticas de segurança.
*   **Cookie Sharing:** É o cenário ideal para Login (SSO). O usuário aproveita sua sessão já logada no Chrome, e o App recebe apenas o token de volta via Deep Link, sem nunca "ver" a senha do usuário.

---

## 🔄 Fluxos de Comunicação Implementados

### Fluxo via WebView (Ponte Nativa)
*   **Web -> Nativo:** `window.AndroidBridge.onLoginComplete(token)`
*   **Nativo -> Web:** Carregamento de URL com parâmetros ou injeção via `evaluateJavascript`.
*   **Uso Ideal:** Dashboards internos, telas de suporte e fluxos onde a UI web e nativa devem ser indistinguíveis.

### Fluxo via Custom Tabs (Redirecionamento)
*   **Nativo -> Web:** Início via `CustomTabsIntent`.
*   **Web -> Nativo:** Redirecionamento via URI Scheme:
    *   *Login:* `window.location.href = 'pocwebview://callback?token=XYZ'`
    *   *Ação:* `window.location.href = 'pocwebview://callback?action=navigateToSecondActivity'`
*   **Uso Ideal:** Autenticação (Login/Cadastro), Termos de Uso, Pagamentos e Conteúdos de Terceiros.

---

## 🛠️ Detalhes Técnicos de Implementação

*   **Rede:** Uso de `10.0.2.2:4200` para acesso ao localhost via emulador.
*   **Resiliência:** Tratamento nativo de erros de carregamento na WebView com tela de "Retry".
*   **Navegação:** A `HomeActivity` detecta a origem e adapta a UI automaticamente (WebView interna vs. Botão de abertura de Custom Tab).

---

## 🚀 Como Executar

1.  **Lado Web (Angular):**
    *   Execute: `ng serve --host 0.0.0.0 --disable-host-check`.
2.  **Lado Android:**
    *   Rode o App no Emulador.
    *   Escolha o fluxo na tela inicial e observe o comportamento das barras de sistema e a transição entre telas.

---
**Documentação estratégica elaborada para fins de arquitetura de solução.**
