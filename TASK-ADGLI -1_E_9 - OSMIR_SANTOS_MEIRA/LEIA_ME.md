Módulo de Reservas (Professor & Aluno) - Epics ADGLI-1 e ADGLI-9

Autor: Osmir Santos Meira
Tasks: Reserva de Laboratórios e Reserva de Estações

Abordagem Técnica e Integração

Este módulo implementa a interface de usuário (Frontend Android) e a persistência de dados para o sistema de gerenciamento.

Arquitetura Híbrida

Para atender aos requisitos de persistência real e acesso concorrente, este módulo utiliza o Firebase Firestore. No entanto, ele foi desenhado para ser compatível com a lógica de estruturas de dados em memória desenvolvida nos outros módulos (ex: IAN-LIMA-VIEIRA).

Camada de Dados (Atual): Firestore (para garantir que os dados não se percam).

Camada de Lógica (Integração Futura): As classes de modelo (Laboratorio, Reserva) foram estruturadas para facilitar a injeção das listas encadeadas/filas manuais desenvolvidas pelo grupo como um cache em memória ou mecanismo de processamento local.

Funcionalidades Implementadas

Autenticação: Login de Professores e Alunos.

Reserva de Laboratórios (Prof): Listagem, seleção de horário e validação de conflitos.

Reserva de Estações (Aluno): Fluxo análogo para estações individuais.

Gestão: Visualização e cancelamento de reservas.

Como Executar Este Módulo

No Android Studio, abra este diretório (TASK-ADGLI-1_E_9...) como um projeto.

Sincronize o Gradle.

Execute no emulador.

Login de Teste: teste@ifba.edu.br / 123456