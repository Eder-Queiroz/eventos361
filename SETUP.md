# Eventos361 - Sistema de Gerenciamento de Eventos

## 🚀 Inicialização Rápida

### Pré-requisitos
- Java 21+
- Maven 3.6+
- Node.js 16+
- PostgreSQL 12+
- Redis (opcional, mas recomendado)

### Scripts Disponíveis

#### 1. Setup Inicial
```bash
./start-project.sh
```
Este script irá:
- ✅ Verificar todas as dependências necessárias
- ✅ Instalar Maven via Homebrew (se necessário)
- ✅ Criar o banco de dados PostgreSQL
- ✅ Iniciar Redis (se disponível)
- ✅ Instalar dependências do Node.js
- ✅ Compilar CSS com Tailwind
- ✅ Executar migrações do banco de dados
- ✅ Compilar o projeto Java

#### 2. Modo de Desenvolvimento
```bash
./dev-mode.sh
```
Este script inicia:
- 🔄 Hot reload com Spring Boot DevTools
- 👁️ Watch mode para Tailwind CSS
- 🚀 Servidor em https://localhost:8443

#### 3. Parar Aplicação
```bash
./stop-project.sh
```
Para todos os processos relacionados ao projeto.

## 🗄️ Banco de Dados

### Configuração Padrão
- **Host:** localhost:5432
- **Database:** controlevacinacaoeco
- **Usuário:** postgres
- **Senha:** Kokorozzdew1455

### Estrutura das Tabelas
- `usuario` - Usuários do sistema
- `evento` - Eventos cadastrados
- `participante` - Participações em eventos

## 🎨 Frontend

O projeto usa **Tailwind CSS** para estilização. Os arquivos relevantes são:
- `src/main/resources/static/css/input.css` - CSS de entrada
- `src/main/resources/static/css/style.css` - CSS compilado (não editar)
- `tailwind.config.js` - Configuração do Tailwind

### Comandos CSS
```bash
# Compilar CSS uma vez
pnpm run build

# Watch mode (recompila automaticamente)
pnpm run watch
```

## 🛡️ Segurança

A aplicação usa HTTPS por padrão:
- **HTTPS:** https://localhost:8443
- **HTTP:** http://localhost:8080 (redireciona para HTTPS)

O certificado é auto-assinado, então seu navegador mostrará um aviso de segurança.

## 🔧 Desenvolvimento

### Estrutura do Projeto
```
src/
├── main/
│   ├── java/web/eventos361/     # Código Java
│   │   ├── controller/          # Controllers REST/Web
│   │   ├── model/              # Entidades JPA
│   │   ├── repository/         # Repositórios
│   │   ├── service/            # Serviços de negócio
│   │   └── config/             # Configurações
│   └── resources/
│       ├── static/             # Arquivos estáticos (CSS, JS, imagens)
│       ├── templates/          # Templates Thymeleaf
│       └── db/migration/       # Migrações Flyway
```

### Tecnologias Utilizadas
- **Backend:** Spring Boot 3.4.0, Java 21
- **Frontend:** Thymeleaf, Tailwind CSS, HTMX
- **Banco:** PostgreSQL, Flyway
- **Segurança:** Spring Security
- **Sessões:** Redis
- **Build:** Maven, pnpm

## 📝 Logs

Os logs da aplicação usam Log4j2 e estão configurados em:
- `src/main/resources/log4j2-spring.xml`

## 🚨 Troubleshooting

### Erro de Conexão com Banco
```bash
# Verificar se PostgreSQL está rodando
pg_isready -h localhost -p 5432

# Iniciar PostgreSQL (Homebrew)
brew services start postgresql
```

### Erro de Porta em Uso
```bash
# Verificar o que está usando a porta
lsof -i :8443

# Parar processos
./stop-project.sh
```

### Erro de Dependências
```bash
# Limpar e reinstalar
mvn clean
pnpm install
./start-project.sh
```

## 📞 Suporte

Para problemas ou dúvidas:
1. Verifique os logs da aplicação
2. Execute `./stop-project.sh` e depois `./start-project.sh`
3. Verifique se todas as dependências estão instaladas
