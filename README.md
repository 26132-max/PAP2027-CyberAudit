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

## Funcionalidades previstas

### 1. Organizações e utilizadores
- Gestão de organizações e respetivos utilizadores.
- Associação de cada utilizador a uma organização.
- Funções disponíveis: `ADMIN`, `ANALISTA` e `LEITOR`.
- Palavras-passe guardadas com hash bcrypt, nunca em texto simples.
- Eliminação de utilizadores e listagem dos registos.
- A autenticação e aplicação efetiva das permissões ainda serão implementadas numa etapa posterior.

### 2. Ativos
- Registo de aplicações, APIs, projetos e sistemas.
- Consulta dos ativos associados a cada organização.

### 3. Auditorias
- Criação e histórico de auditorias.
- Análise estática de código (SAST).
- Análise dinâmica de aplicações (DAST), apenas com autorização explícita.
- Análise de dependências.

### 4. Vulnerabilidades e riscos
- Registo de vulnerabilidades e classificação de gravidade.
- Associação a identificadores CWE e avaliação CVSS.
- Recomendações e acompanhamento do estado de resolução.
- Priorização com base no impacto para a organização.

### 5. Relatórios
- Indicadores de segurança e relatórios técnicos.
- Comparação entre auditorias e acompanhamento de correções.

## Tecnologias

| Tecnologia | Utilização |
|---|---|
| Java 21 | Linguagem principal |
| JavaFX | Interface gráfica nativa da aplicação desktop |
| Maven | Gestão do projeto e dependências |
| SQLite | Base de dados local da aplicação |
| JDBC | Acesso à base de dados |
| jBCrypt | Hash de palavras-passe |
| JUnit 5 | Testes |
| Git e GitHub | Controlo de versões |

## Ferramentas de segurança previstas

- **OWASP ZAP** — análise dinâmica, em alvos próprios ou com autorização explícita.
- **OWASP Dependency-Check** — identificação de dependências vulneráveis.
- **SonarQube** — análise estática e qualidade do código.
- **CWE** — classificação de vulnerabilidades.
- **CVSS** — avaliação da gravidade técnica.

A integração destas ferramentas será realizada progressivamente. As análises não serão executadas contra sistemas sem autorização.

## Arquitetura

O CyberAudit é uma aplicação desktop modular. A interface gráfica é construída com JavaFX; a lógica de negócio é organizada em módulos; e os dados da primeira versão são guardados numa base de dados SQLite local no computador do utilizador. A aplicação não necessita de navegador nem de servidor web para ser utilizada.

## Estado atual

A aplicação inclui gestão local de organizações e utilizadores: é possível criar e eliminar registos, associar utilizadores a organizações e definir funções. As palavras-passe são armazenadas com hash bcrypt. Os dados ficam em `data/cyberaudit.db`. A autenticação real e o controlo das permissões ainda não estão implementados; as restantes funcionalidades descritas são objetivos futuros.

## Executar em desenvolvimento

Requisitos: **JDK 21** e **Maven**.

```bash
mvn clean javafx:run
```

Na primeira execução, a aplicação cria automaticamente a pasta `data` e a base de dados SQLite. O projeto está preparado para execução em ambiente gráfico; num sistema sem interface gráfica, a janela não poderá ser aberta.

## Plano de desenvolvimento

1. Preparar o ambiente e a estrutura da aplicação desktop.
2. Implementar a gestão de organizações e utilizadores.
3. Criar autenticação e aplicação de permissões.
4. Implementar gestão de ativos.
5. Criar os módulos de auditoria e histórico.
6. Integrar progressivamente análises SAST, DAST e de dependências.
7. Implementar gestão de vulnerabilidades e riscos.
8. Criar relatórios, testes e documentação.
9. Preparar empacotamento para instalação nos sistemas operativos pretendidos.

## Segurança e utilização responsável

As análises serão realizadas apenas em aplicações, sistemas e infraestruturas próprios ou para os quais exista autorização explícita. Os dados guardados localmente devem ser protegidos pelo utilizador do computador. Os resultados automáticos podem conter falsos positivos e devem ser revistos antes de serem tratados como vulnerabilidades confirmadas.

**Projeto desenvolvido no âmbito da PAP 2027.**
