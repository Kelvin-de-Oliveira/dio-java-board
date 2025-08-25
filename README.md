# 📌 Sistema de Gerenciamento de Tarefas baseado em Board

Este projeto implementa um sistema de gerenciamento de tarefas no estilo **Kanban**, onde **boards** possuem **colunas** e **cards** que representam tarefas.  
Foi desenvolvido em **Java 17**, utilizando **Maven, JDBC e MySQL**, com controle de migrations feito via **Flyway**.

O objetivo é totalmente didático, servindo como prática no uso de JDBC, MySQL e boas práticas de organização de código, no contexto do **BootCamp Santander 2025 - Back-End com Java (DIO)**.

---

## 🚀 Funcionalidades

### Menu inicial
- Criar novo board
- Selecionar board
- Excluir boards
- Sair

### Menu do board
- Visualizar colunas e cards organizados
- Criar novos cards
- Selecionar um card para abrir o menu de operações

### Menu do card
- Mover o card para a próxima coluna
- Cancelar o card
- Bloquear o card (informando o motivo)
- Desbloquear o card (informando o motivo)
- Fechar o menu do card (retornar ao menu do board)

---

## 📋 Regras de Negócio

### Estrutura do Board
- Um board deve ter **nome** e **pelo menos 3 colunas**:
    - **Coluna inicial** (primeira do board)
    - **Coluna final** (penúltima do board)
    - **Coluna de cancelamento** (última do board)
- Colunas pendentes opcionais podem ser inseridas entre elas

### Colunas
- Atributos: **Nome**, **Ordem de exibição**, **Tipo** (INICIAL, PENDENTE, FINAL, CANCELLED)
- Restrições:
    - Apenas **1 inicial**, **1 final** e **1 cancelamento** por board
    - Inicial = primeira
    - Final = penúltima
    - Cancelamento = última

### Cards
- Atributos: **Título**, **Descrição**, **Data de criação**, **Status de bloqueio**

### Movimentação de Cards
- Seguem a ordem das colunas, sem pular etapas
- Podem ser movidos diretamente para a coluna de cancelamento (se não estiverem na final)

### Bloqueio de Cards
- Cards bloqueados não podem ser movidos
- É obrigatório informar o motivo do **bloqueio** e do **desbloqueio**

---

## 🛠️ Tecnologias Utilizadas
- Java 17
- Maven
- MySQL
- Flyway (migrations)
- JDBC

---

## ⚙️ Configuração e Execução

### Pré-requisitos
- Java 17 instalado
- MySQL configurado
- Maven instalado

### Passo a passo

1. **Clone o repositório**
   ```bash
   git clone https://github.com/seu-usuario/seu-repositorio.git
   cd seu-repositorio
    ```

2. **Configure a conexão com o banco**
    ```bash
    cp src/main/resources/db.properties.example src/main/resources/db.properties
    ```

Edite o arquivo `db.properties` com os dados do seu MySQL e conforme as orientações contidas em db.properties.example :

```properties
db.url=jdbc:mysql://localhost:3306/seu_banco
db.user=seu_usuario
db.password=sua_senha
```

## Compile e execute o projeto

```bash
    mvn clean install
    mvn exec:java -Dexec.mainClass="br.com.kelvin.Main"
```

---

# 🎯 Objetivo

Este projeto tem como foco o aprendizado prático de:

- JDBC
- Persistência em MySQL
- Estruturação de regras de negócio em sistemas baseados em workflow
- Controle de versão do banco de dados com Flyway
- Desenvolvido como prática no BootCamp Santander 2025 - Back-End com Java (DIO)

---





