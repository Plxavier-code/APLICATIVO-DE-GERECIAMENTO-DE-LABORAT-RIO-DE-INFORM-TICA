# Sistema de Gerenciamento de Reservas de Laboratório

**Desenvolvedor:** Osmir Santos Meira  
**Módulo:** TASK-ADGLI -1_E_9 (Agendamento, Notificações e Gestão de Reservas)

---

## 📋 Sobre o Projeto

Este aplicativo Android oferece uma solução para o gerenciamento de reservas de laboratórios de informática e estações de trabalho. O foco deste módulo é garantir a integridade dos agendamentos (evitando conflitos de horário) e manter o usuário informado através de notificações locais.

### Funcionalidades Implementadas

1.  **Autenticação e Sessão**:
    * Login simulado com diferentes perfis de acesso (**Aluno**, **Professor** e **Administrador**).
    * Gestão de sessão via Singleton (`SessionManager`), mantendo o usuário logado durante o uso.
    * Direcionamento inteligente: Professores reservam laboratórios inteiros; Alunos reservam estações individuais.

2.  **Gestão de Reservas**:
    * **Agendamento**: Interface para seleção de data e horário.
    * **Validação de Conflitos**: Algoritmo robusto que impede choques de horário. O sistema respeita a hierarquia: se um laboratório for reservado, suas estações ficam indisponíveis no mesmo horário (e vice-versa).
    * **Minhas Reservas**: Listagem das reservas do usuário com opção de cancelamento.

3.  **Sistema de Notificações (Push Local)**:
    * Notificações na barra de status do Android informando o sucesso de operações.
    * **Eventos notificados**: Confirmação de nova reserva e Cancelamento de reserva.
    * Compatível com permissões de tempo de execução do Android 13+ (`POST_NOTIFICATIONS`).

---

## 🛠 Estrutura do Código

O projeto segue uma arquitetura organizada em pacotes para facilitar a manutenção e escalabilidade:

### `com.example.a2.data` (Modelos)
Classes que representam as entidades e a persistência de dados em memória.
* **`Usuario`**: Define os dados do usuário e seu tipo (Enum `Tipo`).
* **`Laboratorio`**: Representa tanto laboratórios quanto estações de trabalho.
* **`Reserva`**: Armazena detalhes do agendamento (Horário, Lab, Solicitante).
* **`ReservaRepository`**: Singleton que atua como banco de dados em memória, armazenando as listas de objetos.

### `com.example.a2.service` (Regras de Negócio)
Camada lógica que processa as operações.
* **`ReservaService`**: Contém a lógica crítica de verificação de conflitos (`existeConflito`) e métodos CRUD para reservas.
* **`UsuarioService`**: Simula a autenticação e fornece usuários de teste pré-cadastrados.
* **`SessionManager`**: Gerencia o estado do usuário logado globalmente.

### `com.example.a2.ui` (Interface do Usuário)
Activities responsáveis pela interação com o usuário.
* **`LoginActivity`**: Tela de entrada.
* **`MainActivity`**: Tela principal, gerencia permissões e navegação.
* **`AgendamentoActivity`**: Formulário para criar novas reservas.
* **`MinhasReservasActivity`**: Lista reservas ativas e permite cancelamento.
* **`ListaLaboratoriosActivity`** / **`ListaEstacoesActivity`**: Telas de seleção do recurso a ser reservado.

### `com.example.a2.util` (Utilitários)
* **`NotificationHelper`**: Classe auxiliar centralizada para a criação de canais de notificação e disparo dos alertas visuais.

---

## 🚀 Credenciais para Teste

O sistema não utiliza banco de dados externo. Utilize as seguintes credenciais "hardcoded" para validar as funcionalidades:

| Tipo de Usuário | E-mail | Senha | Permissão Principal |
| :--- | :--- | :--- | :--- |
| **Administrador** | `admin@if.com` | `admin123` | Acesso total (Simulado) |
| **Professor** | `prof@if.com` | `prof123` | Reservar Laboratórios |
| **Aluno** | `aluno@if.com` | `aluno123` | Reservar Estações |

---

## 📱 Guia de Uso Rápido

1.  **Login**: Utilize uma das contas acima.
2.  **Permissões**: Ao abrir o app pela primeira vez, aceite a permissão de notificações.
3.  **Criar Reserva**:
    * Toque em "Fazer Reserva".
    * Escolha o laboratório ou estação desejada.
    * Defina a data e o intervalo de horário.
    * *Resultado*: Você receberá uma notificação confirmando o agendamento.
4.  **Cancelar Reserva**:
    * Acesse "Minhas Reservas".
    * Identifique a reserva e toque no ícone de lixeira.
    * *Resultado*: A reserva será removida e uma notificação confirmará o cancelamento.

---

## ⚙️ Requisitos Técnicos

* **Linguagem**: Java
* **SDK Mínimo**: API 26 (Android 8.0 Oreo) - Necessário para Notification Channels.
* **SDK Alvo**: API 33 (Android 13 Tiramisu).