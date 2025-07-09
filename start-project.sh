#!/bin/bash

# Script para inicializar o projeto Eventos361
# Autor: Sistema de Eventos
# Data: $(date +%Y-%m-%d)

set -e  # Parar execução se algum comando falhar

echo "🚀 Iniciando setup do projeto Eventos361..."

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para imprimir mensagens coloridas
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCESSO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[AVISO]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERRO]${NC} $1"
}

# Verificar se estamos no diretório correto
if [ ! -f "pom.xml" ] || [ ! -f "package.json" ]; then
    print_error "Execute este script no diretório raiz do projeto!"
    exit 1
fi

print_status "Verificando dependências do sistema..."

# Verificar se Docker está disponível e containers estão rodando
if command -v docker &> /dev/null && docker compose ps | grep -q "eventos361-postgres.*Up"; then
    print_success "Docker detectado! Containers PostgreSQL e Redis já estão rodando."
    print_status "Pulando configuração de PostgreSQL e Redis locais..."
    USING_DOCKER=true
else
    USING_DOCKER=false
    if command -v docker &> /dev/null; then
        print_warning "Docker disponível, mas containers não estão rodando."
        print_status "Para usar Docker, execute: ./docker-start.sh"
        print_status "Continuando com setup local..."
    fi
fi

# Verificar se Java está instalado
if ! command -v java &> /dev/null; then
    print_error "Java não está instalado. Instale Java 21 ou superior."
    exit 1
fi

# Verificar versão do Java
JAVA_VERSION=$(java -version 2>&1 | head -1 | sed 's/.*version "\([^"]*\)".*/\1/')
print_success "Java encontrado: versão $JAVA_VERSION"

# Verificar se Maven está instalado
if ! command -v mvn &> /dev/null; then
    print_error "Maven não está instalado."
    print_status "Instalando Maven via Homebrew..."
    
    if command -v brew &> /dev/null; then
        brew install maven
    else
        print_error "Homebrew não encontrado. Instale o Maven manualmente."
        exit 1
    fi
fi

MVN_VERSION=$(mvn -version | head -1)
print_success "Maven encontrado: $MVN_VERSION"

# Verificar se Node.js está instalado (para Tailwind CSS)
if ! command -v node &> /dev/null; then
    print_error "Node.js não está instalado."
    print_status "Instale Node.js para compilar o Tailwind CSS."
    exit 1
fi

NODE_VERSION=$(node --version)
print_success "Node.js encontrado: $NODE_VERSION"

# Verificar se pnpm está instalado
if ! command -v pnpm &> /dev/null; then
    print_warning "pnpm não encontrado. Instalando..."
    npm install -g pnpm
fi

PNPM_VERSION=$(pnpm --version)
print_success "pnpm encontrado: versão $PNPM_VERSION"

if [ "$USING_DOCKER" = false ]; then
    # Verificar se PostgreSQL está rodando
    print_status "Verificando PostgreSQL..."
    if ! pg_isready -h localhost -p 5432 &> /dev/null; then
        print_warning "PostgreSQL não está rodando ou não está acessível."
        print_status "Tentando iniciar PostgreSQL..."
        
        if command -v brew &> /dev/null; then
            if brew services list | grep postgresql | grep started &> /dev/null; then
                print_success "PostgreSQL já está rodando via Homebrew"
            else
                brew services start postgresql@14 || brew services start postgresql
            fi
        else
            print_error "Não foi possível iniciar PostgreSQL automaticamente."
            print_status "Inicie o PostgreSQL manualmente e execute o script novamente."
            exit 1
        fi
        
        # Aguardar PostgreSQL inicializar
        print_status "Aguardando PostgreSQL inicializar..."
        sleep 3
    fi

    # Verificar se o banco de dados existe
    print_status "Verificando banco de dados 'controlevacinacaoeco'..."
    if ! psql -h localhost -U postgres -d controlevacinacaoeco -c '\q' 2>/dev/null; then
        print_warning "Banco de dados 'controlevacinacaoeco' não existe."
        print_status "Criando banco de dados..."
        
        # Tentar criar o banco de dados
        if psql -h localhost -U postgres -c "CREATE DATABASE controlevacinacaoeco;" 2>/dev/null; then
            print_success "Banco de dados criado com sucesso!"
        else
            print_error "Erro ao criar banco de dados. Verifique as credenciais do PostgreSQL."
            print_status "Execute manualmente: CREATE DATABASE controlevacinacaoeco;"
            exit 1
        fi
    else
        print_success "Banco de dados 'controlevacinacaoeco' encontrado!"
    fi

    # Verificar se Redis está rodando (opcional, mas recomendado)
    print_status "Verificando Redis..."
    if ! redis-cli ping &> /dev/null; then
        print_warning "Redis não está rodando."
        print_status "Tentando iniciar Redis..."
        
        if command -v brew &> /dev/null; then
            if brew services list | grep redis | grep started &> /dev/null; then
                print_success "Redis já está rodando via Homebrew"
            else
                brew services start redis
            fi
        else
            print_warning "Redis não foi iniciado automaticamente. A aplicação pode funcionar sem Redis, mas sessões não serão persistidas."
        fi
    else
        print_success "Redis está rodando!"
    fi
else
    print_success "Usando PostgreSQL e Redis via Docker!"
fi

# Instalar dependências do Node.js
print_status "Instalando dependências do Node.js..."
if pnpm install; then
    print_success "Dependências do Node.js instaladas!"
else
    print_error "Erro ao instalar dependências do Node.js"
    exit 1
fi

# Compilar CSS com Tailwind
print_status "Compilando CSS com Tailwind..."
if pnpm run build; then
    print_success "CSS compilado com sucesso!"
else
    print_error "Erro ao compilar CSS"
    exit 1
fi

# Instalar dependências do Maven
print_status "Baixando dependências do Maven..."
if mvn dependency:resolve; then
    print_success "Dependências do Maven baixadas!"
else
    print_error "Erro ao baixar dependências do Maven"
    exit 1
fi

# Executar migrações do Flyway
print_status "Executando migrações do banco de dados..."
if mvn flyway:migrate; then
    print_success "Migrações executadas com sucesso!"
else
    print_warning "Erro ao executar migrações. Tentando compilar e executar novamente..."
    mvn clean compile flyway:migrate
fi

# Compilar o projeto
print_status "Compilando o projeto..."
if mvn clean compile; then
    print_success "Projeto compilado com sucesso!"
else
    print_error "Erro ao compilar projeto"
    exit 1
fi

print_success "🎉 Setup concluído com sucesso!"
echo
print_status "Para iniciar a aplicação, execute:"
echo -e "${GREEN}  mvn spring-boot:run${NC}"
echo
print_status "Ou execute o script de desenvolvimento:"
echo -e "${GREEN}  ./dev-mode.sh${NC}"
echo
print_status "A aplicação estará disponível em:"
echo -e "${GREEN}  https://localhost:8443${NC} (HTTPS)"
echo -e "${GREEN}  http://localhost:8080${NC} (HTTP - redirecionará para HTTPS)"
echo
print_warning "Nota: Como a aplicação usa HTTPS com certificado auto-assinado,"
print_warning "seu navegador mostrará um aviso de segurança. Aceite para continuar."
