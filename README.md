# CyberAudit

**Aplicação desktop de auditoria e gestão de cibersegurança para empresas tecnológicas**

## Sobre o projeto

O **CyberAudit** é uma aplicação desktop para Windows, macOS e Linux, desenvolvida para apoiar empresas de IT e tecnologia na gestão de auditorias de segurança. É um programa instalado e executado no computador, com janela própria — não é um site nem uma aplicação web.

O objetivo é centralizar ativos, auditorias, vulnerabilidades e riscos, acompanhar correções e apoiar a melhoria contínua da segurança de aplicações e sistemas. O projeto está a ser desenvolvido no âmbito da **Prova de Aptidão Profissional (PAP) de 2027**.

## Objetivos

- Registar organizações, utilizadores e ativos de tecnologia.
- Organizar auditorias SAST, DAST e de dependências.
- Registar e classificar vulnerabilidades, incluindo CWE e CVSS.
- Acompanhar correções, riscos e resultados ao longo do tempo.
- Comparar auditorias e preparar relatórios técnicos.
- Guardar os dados localmente, com possibilidade de evolução futura para uma base de dados partilhada.

## Funcionalidades

### Organizações e utilizadores
- Gestão de organizações e respetivos utilizadores.
- Associação de cada utilizador a uma organização.
- Funções disponíveis: `ADMIN`, `ANALISTA` e `LEITOR`.
- Autenticação local por email e palavra-passe.
- Palavras-passe guardadas com hash bcrypt, nunca em texto simples.
- Apenas administradores podem criar/eliminar organizações e utilizadores.
- No primeiro arranque, é apresentada a configuração da organização e criação da conta de administrador.

### Próximos módulos
- Registo de aplicações, APIs, projetos e sistemas.
- Auditorias SAST, DAST e de dependências (DAST apenas em alvos próprios ou com autorização explícita).
- Gestão de vulnerabilidades com CWE/CVSS, riscos, correções e relatórios.

## Tecnologias

| Tecnologia | Utilização |
|---|---|
| Java 21 | Linguagem principal |
| JavaFX | Interface gráfica nativa da aplicação desktop |
| Maven | Gestão do projeto e dependências |
| SQLite | Base de dados local da aplicação |
| JDBC | Acesso à base de dados |
| jBCrypt | Hash e verificação de palavras-passe |
| JUnit 5 | Testes |
| Git e GitHub | Controlo de versões |

## Arquitetura

A interface é construída com JavaFX, a lógica é organizada em módulos e os dados ficam numa base de dados SQLite local em `data/cyberaudit.db`. A aplicação não necessita de navegador nem de servidor web.

## Estado atual

A aplicação inclui gestão de organizações e utilizadores e autenticação local. Na primeira execução, cria-se a organização e a conta inicial de administrador. Depois, inicia-se sessão com o email e a palavra-passe escolhidos. As funções diferentes estão disponíveis; atualmente, as ações de gestão estão limitadas à função `ADMIN`. Os módulos de ativos, auditorias, vulnerabilidades, riscos e relatórios ainda estão por desenvolver.

## Executar em desenvolvimento

Requisitos: **JDK 21** e **Maven**.

```bash
mvn clean javafx:run
```

Na primeira execução, a aplicação cria automaticamente a pasta `data` e a base de dados SQLite. Executa-a num computador com ambiente gráfico.

## Segurança e utilização responsável

As análises serão realizadas apenas em aplicações, sistemas e infraestruturas próprios ou para os quais exista autorização explícita. Os dados guardados localmente devem ser protegidos pelo utilizador do computador. Os resultados automáticos podem conter falsos positivos e devem ser revistos antes de serem tratados como vulnerabilidades confirmadas.

**Projeto desenvolvido no âmbito da PAP 2027.**
