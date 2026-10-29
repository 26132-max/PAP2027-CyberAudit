# PAP2027-CyberAudit

# 🛡️ CyberAudit

**Plataforma de Análise e Auditoria de Cibersegurança**

Projeto de Aptidão Profissional (PAP) 2027 · Curso Técnico de Gestão e Programação de Sistemas Informáticos (TGPSI)

![Java](https://img.shields.io/badge/Java-21-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-UI-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-DB-336791)
![Maven](https://img.shields.io/badge/Maven-build-C71A36)
![Estado](https://img.shields.io/badge/estado-em%20desenvolvimento-yellow)

---

##  Sobre o projeto

O **CyberAudit** é uma aplicação de auditoria de segurança que analisa outras aplicações, identifica possíveis pontos de risco e apresenta ao utilizador os problemas encontrados, o seu nível de gravidade e recomendações para os corrigir.

O objetivo **não** é "hackear" uma aplicação, mas verificar, de forma **autorizada e controlada**, se existem configurações ou comportamentos que possam representar riscos de segurança, como:

- cookies sem proteção;
- ausência de limites nas tentativas de login;
- permissões mal verificadas;
- servidores que revelam informação em excesso.

Para tornar o projeto demonstrável e seguro, este repositório inclui também o **MiniGram**, uma pequena rede social que serve de alvo de testes.

---

## 🎯 Funcionalidades

### CyberAudit
- Verificações independentes, todas a implementar a mesma interface
- Pedidos HTTP normais ao alvo, sem exploits nem ações destrutivas
- Áreas analisadas: **autenticação, gestão de sessões, autorização, API, proteção de dados e configuração de segurança** (HTTPS, headers e CORS)
- Classificação de cada problema em **5 níveis**, de *informativo* a *crítico*
- Associação de cada problema a uma referência **CWE** (Common Weakness Enumeration)
- **Pontuação de 0 a 100** com dashboard do estado de cada área
- **Histórico** das análises e comparação entre elas
- Geração de **relatórios de segurança em PDF**

### MiniGram
- Registo e login de utilizadores
- Publicações e mensagens
- Painel de administração
- API REST
- Dois modos de funcionamento, ativáveis por uma variável de configuração:
  - 🟢 **Seguro**: boas práticas de segurança aplicadas
  - 🔴 **Vulnerável**: falhas introduzidas de propósito para o CyberAudit detetar

### Ciclo de demonstração

```
Analisar o MiniGram (modo vulnerável)
        ↓
Identificar os problemas e a sua gravidade
        ↓
Corrigir as falhas (modo seguro)
        ↓
Analisar novamente e comparar os resultados
```

---

## Tecnologias

| Área | Tecnologia |
|------|-----------|
| Linguagem | Java |
| Interface gráfica | JavaFX (CyberAudit e cliente MiniGram) |
| Servidor / API | Javalin |
| Base de dados | PostgreSQL com JDBC |
| Segurança de passwords | BCrypt |
| Pedidos HTTP | `java.net.http.HttpClient` |
| JSON | Jackson |
| Relatórios PDF | OpenPDF |
| Testes | JUnit |
| Gestão do projeto | Maven |
| Estilização | CSS (JavaFX) |

---

##  Estrutura do repositório

> Estrutura prevista; ajustar conforme o desenvolvimento.

```
CyberAudit/
├── common/             # Código partilhado entre as aplicações
├── cyberaudit/         # Aplicação de auditoria (JavaFX)
├── minigram-server/    # Servidor e API REST (Javalin)
├── minigram-client/    # Cliente do MiniGram (JavaFX)
├── database/           # Scripts SQL
├── docs/               # Documentação e relatório da PAP
└── pom.xml
```

---

##  Instalação e execução

### Pré-requisitos
- JDK 21 (ou superior)
- Maven 3.9+
- PostgreSQL 15+

### Passos

```bash
# 1. Clonar o repositório
git clone https://github.com/<o-teu-utilizador>/CyberAudit.git
cd CyberAudit

# 2. Criar a base de dados
psql -U postgres -c "CREATE DATABASE minigram;"
psql -U postgres -d minigram -f database/schema.sql

# 3. Compilar o projeto
mvn clean install
```

### Configuração do MiniGram

O modo de funcionamento é definido por uma variável de configuração:

```bash
# Modo seguro
MINIGRAM_MODE=secure

# Modo vulnerável (apenas para testes locais!)
MINIGRAM_MODE=vulnerable
```

### Executar

```bash
# Servidor MiniGram
mvn -pl minigram-server exec:java

# Cliente MiniGram
mvn -pl minigram-client javafx:run

# CyberAudit
mvn -pl cyberaudit javafx:run
```

---

##  Ética e legalidade

O CyberAudit foi desenvolvido **exclusivamente para fins educativos e de auditoria autorizada**.

- Só analisa aplicações **próprias ou com autorização**.
- **Recusa qualquer alvo que não seja o computador local.**
- Não utiliza exploits nem executa ações destrutivas.
- O modo vulnerável do MiniGram **nunca deve ser exposto à internet**.

---

##  Testes

```bash
mvn test
```

---

##  Estado do projeto

- [ ] Estrutura base do projeto (Maven)
- [ ] MiniGram: servidor e API REST
- [ ] MiniGram: cliente JavaFX
- [ ] MiniGram: modo seguro e modo vulnerável
- [ ] CyberAudit: motor de verificações
- [ ] CyberAudit: classificação, CWE e pontuação
- [ ] CyberAudit: dashboard e histórico
- [ ] CyberAudit: relatórios PDF
- [ ] Testes e documentação final

---

## 🎓 Enquadramento académico

Este projeto integra várias áreas do curso de TGPSI: programação orientada a objetos, bases de dados, desenvolvimento de aplicações, APIs e redes, segurança informática, interfaces gráficas, tratamento de dados, testes e documentação.

---

## Autor

**<Idalina>**
Curso TGPSI · PAP 2027


---

## 📄 Licença

Projeto desenvolvido para fins académicos. Licença a definir (por ex
