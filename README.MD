### Desenvolvedor:

- [Leonardo Duarte](https://www.linkedin.com/in/leonardoduarte1305/) - leonardoduarte1305@gmail.com

<hr>

# Git Lib

## Overview

#### A idéia deste projeto é automatizar algumas tarefas relacionadas ao Git Flow, ao Maven e etc.

<hr>

## Requisitos para rodar os comandos:

#### Java 17 instalado

#### Sistema Linux

## 1 - Clonando repos

- Para clonar um ou mais repositórios insira-o(s) no Set de repos contidos
  em: [GitCloneTest](./src/test/java/dev/leoduarte/automations/GitCloneTest.java) e execute o teste.
- Esta funcionalidade leva em consideração que você tem uma chave SSH configurada na sua máquina e no repo remoto
- O destino padrão é: ${HOME}/Git, se precisar modificar o local o problema é teu, te vira lendo a classe!!!

## 2 - Executando comandos Maven

- Para executar recursivamente comandos maven utilize a classe de
  testes [LazyDevWithBashCommandsTest](./src/test/java/dev/leoduarte/automations/LazyDevWithBashCommandsTest.java).
    - GIT_DIRECTORY: Diretório raíz onde você deseja executar o comando.
    - Existem casos de teste básicos para limpar, verificar, construir e instalar os projetos.
    - Nesta classe existem outros comandos porém são para fins didádicos e para consultas futuras.
