# Minha Parte do Trabalho: O Painel do Administrador

Eu criei o **Painel do Administrador**.

Minha responsabilidade era garantir que, assim que alguém loga, o sistema verifica: "Esse cara é um Admin?". Se for, ele vê meu menu. Se for Aluno ou Professor, ele não vê nada disso.

## O que esse painel faz?

### 1. Gerencia os Laboratórios (as salas):
* Permite ao Admin listar todas as salas.
* Permite **Bloquear** ou **Desbloquear** uma sala inteira (ex: "LAB-01 interditado").

### 2. Gerencia as Estações (os PCs):
* Permite ao Admin ver todos os PCs de uma sala específica e o status de cada um (se está livre, bloqueado ou em manutenção).
* Permite **Bloquear** ou **Liberar** um PC específico.
* Permite **Registrar Manutenção:** O Admin pode dizer, por exemplo, que o "PC 05" está em manutenção e descrever o problema. O sistema guarda esse histórico.

---

## Como eu usei as Estruturas de Dados

Para fazer isso, eu usei as próprias estruturas que a gente criou:

* **`ListaEstatica` (Array Fixo):** Usei para: 
    1.  Guardar os **PCs dentro de um Laboratório**. Faz todo sentido, porque um laboratório tem um número fixo de máquinas (ex: 20 PCs).
    2.  Para guardar o **histórico de manutenção** de cada PC (já que um PC pode quebrar várias vezes).
    3.  Para guardar a **lista de laboratórios** do sistema (já que podemos adicionar novas salas depois).

> **Extra:** Eu também fiz um método que, quando o Admin cadastra um novo laboratório com 20 vagas, o sistema já cria automaticamente os 20 PCs (com ID de 1 a 20) dentro dele.