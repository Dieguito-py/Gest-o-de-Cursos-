
# Gestão de Cursos do Ensino Superior

Este projeto é um sistema de gestão de cursos desenvolvido como parte do Projeto Integrador Senac. O objetivo é facilitar o gerenciamento de informações relacionadas a cursos, alunos e professores.


## Features

- **Cadastro de Cursos:** Adicionar, editar e excluir cursos.
- **Cadastro de Alunos:** Gerenciar informações dos alunos matriculados.
- **Matrícula de Alunos:** Associar alunos a cursos específicos.
- **Visualização em Tabelas:** Exibir dados de cursos e alunos em tabelas interativas.
- **Gráficos:** Visualizar a quantidade de alunos por curso em gráficos.

## Tech Stack

- **Java:** Linguagem de programação principal.
- **JavaFX:** Framework para a interface gráfica.
- **MySQL:** Banco de dados relacional.
- **Maven:** Gerenciamento de dependências.
- **JUnit:** Testes unitários.
- **Git:** Controle de versão.
- **Scene Builder:** Ferramenta para construção de interfaces JavaFX.

## Requirements

- **JDK 11+**
- **MySQL Server**
- **Maven**

## Installation
- **1.** Clone o repositório:

```
git clone https://github.com/Dieguito-py/Gestao-de-Cursos.git
```
- **2.** Configure o banco de dados no arquivo ```db.properties```.
 - **3.** Execute as *querys* da pasta ```sqls``` para criar e adicionas os *inserts* no banco
- **4.** Compile e execute o projeto com 

```
Maven:mvn clean install
mvn javafx:run
```
