
# CyberAudit

**Plataforma de Auditoria e Gestão de Cibersegurança para Empresas Tecnológicas**

## Sobre o projeto

O **CyberAudit** é um projeto de desenvolvimento de uma plataforma de cibersegurança destinada a empresas de IT e tecnologia.

O seu principal objetivo é ajudar as organizações a identificar vulnerabilidades, avaliar riscos, acompanhar correções e melhorar continuamente a segurança das suas aplicações e sistemas.

A plataforma pretende centralizar diferentes tipos de auditoria de segurança, disponibilizando uma visão organizada dos problemas encontrados e do seu estado de resolução.

Este projeto está a ser desenvolvido no âmbito da **Prova de Aptidão Profissional (PAP) de 2027**.

## Objetivos

- Identificar vulnerabilidades em aplicações e projetos de software.
- Realizar análises estáticas ao código-fonte (SAST).
- Realizar análises dinâmicas a aplicações em execução (DAST).
- Identificar vulnerabilidades em dependências de software.
- Classificar vulnerabilidades de acordo com a sua gravidade.
- Avaliar e acompanhar riscos de segurança.
- Gerir auditorias e respetivos resultados.
- Comparar resultados de auditorias anteriores e posteriores às correções.
- Gerar relatórios técnicos de segurança.

## Funcionalidades previstas

### 1. Gestão de organizações e utilizadores
- Registo e gestão de organizações.
- Autenticação de utilizadores.
- Gestão de permissões e funções.
- Separação dos dados entre organizações.

### 2. Gestão de ativos
- Registo de aplicações web.
- Registo de APIs.
- Gestão de projetos de software.
- Consulta dos ativos associados a cada organização.

### 3. Auditorias de segurança
- Criação e gestão de auditorias.
- Análise estática de código (SAST).
- Análise dinâmica de aplicações (DAST).
- Análise de dependências.
- Histórico de auditorias.

### 4. Gestão de vulnerabilidades
- Registo de vulnerabilidades identificadas.
- Classificação por gravidade.
- Associação a identificadores CWE.
- Recomendações de correção.
- Acompanhamento do estado de resolução.

### 5. Gestão de riscos
- Avaliação da gravidade técnica.
- Consideração do impacto para a organização.
- Priorização das vulnerabilidades.
- Acompanhamento da evolução do risco.

### 6. Relatórios e comparação
- Geração de relatórios de auditoria.
- Visualização de indicadores de segurança.
- Comparação entre auditorias.
- Acompanhamento das vulnerabilidades corrigidas e pendentes.

## Tecnologias previstas

| Tecnologia | Utilização |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot | Desenvolvimento da aplicação |
| Spring Security | Autenticação e autorização |
| Maven | Gestão do projeto e dependências |
| PostgreSQL | Base de dados |
| Spring Data JPA | Persistência de dados |
| HTML, CSS e JavaScript | Interface web |
| Thymeleaf | Integração da interface com Spring |
| JUnit 5 | Testes |
| Git e GitHub | Controlo de versões |

## Ferramentas de segurança previstas

- **OWASP ZAP** — análise dinâmica de aplicações.
- **OWASP Dependency-Check** — identificação de dependências vulneráveis.
- **SonarQube** — análise estática e qualidade do código.
- **CWE** — classificação de vulnerabilidades.
- **CVSS** — avaliação da gravidade técnica das vulnerabilidades.

A integração destas ferramentas será realizada progressivamente.

## Arquitetura prevista

O CyberAudit será inicialmente desenvolvido como uma aplicação web baseada numa arquitetura modular.

Os principais componentes serão:

- Interface web.
- Sistema de autenticação e permissões.
- Gestão de organizações e utilizadores.
- Gestão de ativos.
- Motor de auditorias.
- Analisadores de segurança.
- Gestão de vulnerabilidades e riscos.
- Sistema de relatórios.
- Base de dados.

## Plano de desenvolvimento

O desenvolvimento será realizado por etapas:

1. Preparação do ambiente de desenvolvimento.
2. Criação da estrutura do projeto.
3. Desenvolvimento da gestão de organizações e utilizadores.
4. Implementação da base de dados.
5. Desenvolvimento da gestão de ativos.
6. Implementação da autenticação e permissões.
7. Desenvolvimento do módulo de auditorias.
8. Integração da análise estática (SAST).
9. Integração da análise dinâmica (DAST).
10. Implementação da gestão de vulnerabilidades e riscos.
11. Desenvolvimento do dashboard e relatórios.
12. Testes, documentação e preparação da apresentação da PAP.

## Segurança e utilização responsável

As análises de segurança serão realizadas apenas em aplicações, sistemas e infraestruturas próprios ou para os quais exista autorização explícita.

O projeto terá em consideração a proteção dos dados, o controlo de acessos, o isolamento entre organizações e a utilização responsável das ferramentas de auditoria.

Os resultados automáticos poderão incluir falsos positivos e deverão ser analisados antes de serem considerados vulnerabilidades confirmadas.

## Estado do projeto

**Em fase de planeamento e preparação do desenvolvimento.**

As funcionalidades descritas representam os objetivos previstos e serão implementadas progressivamente.

## Objetivo final

Criar uma plataforma de cibersegurança que permita às empresas tecnológicas centralizar auditorias, identificar vulnerabilidades, gerir riscos e acompanhar a evolução da segurança dos seus sistemas.

---

**Projeto desenvolvido no âmbito da PAP 2027.**
ins académicos. Licença a definir (por ex
