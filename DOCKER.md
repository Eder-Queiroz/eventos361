# Docker Setup - Eventos361

Este projeto inclui uma configuração Docker Compose para facilitar o desenvolvimento local.

## 🐳 Serviços Incluídos

- **PostgreSQL 15**: Banco de dados principal
- **Redis 7**: Cache e sessões
- **Adminer**: Interface web para administração do banco

## 🚀 Uso Rápido

```bash
# Iniciar todos os serviços
./docker-start.sh

# Ou usando docker compose diretamente
docker compose up -d
```

## 📋 Comandos Disponíveis

```bash
# Iniciar serviços
./docker-start.sh up

# Parar serviços
./docker-start.sh down

# Reiniciar serviços
./docker-start.sh restart

# Ver status
./docker-start.sh status

# Ver logs
./docker-start.sh logs
./docker-start.sh logs postgres  # logs específicos

# Conectar ao banco
./docker-start.sh shell postgres

# Conectar ao Redis
./docker-start.sh shell redis

# Limpar dados (CUIDADO!)
./docker-start.sh clean
```

## 🔧 Configuração

### Variáveis de Ambiente (.env)

```env
POSTGRES_DB=controlevacinacaoeco
POSTGRES_USER=postgres
POSTGRES_PASSWORD=Kokorozzdew1455
POSTGRES_PORT=5432
REDIS_PORT=6379
ADMINER_PORT=8080
TZ=America/Sao_Paulo
```

### Portas Utilizadas

- **5432**: PostgreSQL
- **6379**: Redis
- **8080**: Adminer (interface web)

## 📊 Adminer (Interface Web)

Acesse: http://localhost:8080

**Credenciais:**
- Server: `postgres`
- Username: `postgres`
- Password: `Kokorozzdew1455`
- Database: `controlevacinacaoeco`

## 🔄 Integração com o Projeto

Após iniciar os serviços Docker:

```bash
# 1. Iniciar serviços Docker
./docker-start.sh

# 2. Instalar dependências e compilar
./start-project.sh

# 3. Iniciar aplicação Spring Boot
mvn spring-boot:run
```

## 📁 Volumes e Persistência

Os dados são persistidos em volumes Docker:

- `postgres_data`: Dados do PostgreSQL
- `redis_data`: Dados do Redis

Para backup dos dados:

```bash
# Backup PostgreSQL
docker exec eventos361-postgres pg_dump -U postgres controlevacinacaoeco > backup.sql

# Restaurar backup
docker exec -i eventos361-postgres psql -U postgres controlevacinacaoeco < backup.sql
```

## 🛠 Troubleshooting

### Porta em uso
```bash
# Verificar o que está usando a porta
lsof -i :5432
lsof -i :6379
lsof -i :8080

# Parar serviços
./docker-start.sh down
```

### Problemas de permissão
```bash
# Recriar volumes
docker compose down -v
docker compose up -d
```

### Logs detalhados
```bash
# Ver logs em tempo real
./docker-start.sh logs

# Logs específicos
docker compose logs postgres
docker compose logs redis
```

## 🔒 Segurança

⚠️ **IMPORTANTE**: Este setup é para desenvolvimento local. Para produção:

1. Altere as senhas padrão
2. Configure redes isoladas
3. Use secrets em vez de variáveis de ambiente
4. Configure SSL/TLS
5. Implemente backups automatizados

## 🆚 Alternativas

Se preferir não usar Docker:

1. **PostgreSQL local**: `brew install postgresql`
2. **Redis local**: `brew install redis`
3. **Usar o script original**: `./start-project.sh`

## 📞 Suporte

Em caso de problemas:

1. Verifique se Docker está rodando: `docker version`
2. Verifique logs: `./docker-start.sh logs`
3. Reinicie serviços: `./docker-start.sh restart`
4. Se necessário, limpe dados: `./docker-start.sh clean`
