#!/bin/bash

# Script para gerenciar os serviços Docker do projeto Eventos361
# Uso: ./docker-start.sh [up|down|restart|logs|status]

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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

# Verificar se Docker está instalado
if ! command -v docker &> /dev/null; then
    print_error "Docker não está instalado!"
    print_status "Instale o Docker Desktop: https://www.docker.com/products/docker-desktop"
    exit 1
fi

# Verificar se Docker Compose está disponível
if ! docker compose version &> /dev/null; then
    print_error "Docker Compose não está disponível!"
    exit 1
fi

# Função para verificar se os serviços estão rodando
check_services() {
    print_status "Verificando status dos serviços..."
    
    if docker compose ps | grep -q "eventos361-postgres.*Up"; then
        print_success "PostgreSQL: ✅ Rodando"
    else
        print_warning "PostgreSQL: ❌ Parado"
    fi
    
    if docker compose ps | grep -q "eventos361-redis.*Up"; then
        print_success "Redis: ✅ Rodando"
    else
        print_warning "Redis: ❌ Parado"
    fi
    
    if docker compose ps | grep -q "eventos361-adminer.*Up"; then
        print_success "Adminer: ✅ Rodando (http://localhost:8080)"
    else
        print_warning "Adminer: ❌ Parado"
    fi
}

# Função para aguardar serviços ficarem prontos
wait_for_services() {
    print_status "Aguardando serviços ficarem prontos..."
    
    # Aguardar PostgreSQL
    local max_attempts=30
    local attempt=0
    
    while [ $attempt -lt $max_attempts ]; do
        if docker exec eventos361-postgres pg_isready -U postgres -d controlevacinacaoeco &> /dev/null; then
            print_success "PostgreSQL está pronto!"
            break
        fi
        
        attempt=$((attempt + 1))
        print_status "Aguardando PostgreSQL... ($attempt/$max_attempts)"
        sleep 2
    done
    
    if [ $attempt -eq $max_attempts ]; then
        print_error "PostgreSQL não ficou pronto a tempo!"
        exit 1
    fi
    
    # Aguardar Redis
    if docker exec eventos361-redis redis-cli ping &> /dev/null; then
        print_success "Redis está pronto!"
    else
        print_warning "Redis não está respondendo"
    fi
}

case "${1:-up}" in
    "up")
        print_status "🚀 Iniciando serviços Docker..."
        docker compose up -d
        
        print_status "Aguardando containers iniciarem..."
        sleep 5
        
        wait_for_services
        check_services
        
        echo
        print_success "🎉 Todos os serviços estão rodando!"
        echo
        print_status "Conexões disponíveis:"
        echo -e "  ${GREEN}PostgreSQL:${NC} localhost:5432"
        echo -e "  ${GREEN}Database:${NC} controlevacinacaoeco"
        echo -e "  ${GREEN}User:${NC} postgres"
        echo -e "  ${GREEN}Password:${NC} Kokorozzdew1455"
        echo
        echo -e "  ${GREEN}Redis:${NC} localhost:6379"
        echo
        echo -e "  ${GREEN}Adminer (Web UI):${NC} http://localhost:8080"
        echo -e "    Server: postgres"
        echo -e "    Username: postgres"
        echo -e "    Password: Kokorozzdew1455"
        echo -e "    Database: controlevacinacaoeco"
        echo
        print_status "Para iniciar a aplicação Spring Boot:"
        echo -e "  ${GREEN}./start-project.sh${NC}"
        echo -e "  ${GREEN}mvn spring-boot:run${NC}"
        ;;
        
    "down")
        print_status "🛑 Parando serviços Docker..."
        docker compose down
        print_success "Serviços parados!"
        ;;
        
    "restart")
        print_status "🔄 Reiniciando serviços Docker..."
        docker compose down
        docker compose up -d
        wait_for_services
        check_services
        print_success "Serviços reiniciados!"
        ;;
        
    "logs")
        service="${2:-}"
        if [ -n "$service" ]; then
            print_status "📋 Logs do serviço: $service"
            docker compose logs -f "$service"
        else
            print_status "📋 Logs de todos os serviços"
            docker compose logs -f
        fi
        ;;
        
    "status")
        check_services
        echo
        print_status "Containers ativos:"
        docker compose ps
        ;;
        
    "clean")
        print_warning "🧹 Limpando dados dos volumes (CUIDADO: dados serão perdidos!)"
        read -p "Tem certeza? (y/N): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            docker compose down -v
            docker volume prune -f
            print_success "Volumes limpos!"
        else
            print_status "Operação cancelada"
        fi
        ;;
        
    "shell")
        service="${2:-postgres}"
        case "$service" in
            "postgres"|"db")
                print_status "🐘 Conectando ao PostgreSQL..."
                docker exec -it eventos361-postgres psql -U postgres -d controlevacinacaoeco
                ;;
            "redis")
                print_status "🔴 Conectando ao Redis..."
                docker exec -it eventos361-redis redis-cli
                ;;
            *)
                print_error "Serviço não reconhecido: $service"
                print_status "Serviços disponíveis: postgres, redis"
                ;;
        esac
        ;;
        
    "help"|"--help"|"-h")
        echo "Uso: $0 [comando]"
        echo
        echo "Comandos disponíveis:"
        echo "  up          Iniciar todos os serviços (padrão)"
        echo "  down        Parar todos os serviços"
        echo "  restart     Reiniciar todos os serviços"
        echo "  status      Verificar status dos serviços"
        echo "  logs        Mostrar logs (logs [serviço])"
        echo "  shell       Conectar ao serviço (shell [postgres|redis])"
        echo "  clean       Limpar volumes (remove dados!)"
        echo "  help        Mostrar esta ajuda"
        ;;
        
    *)
        print_error "Comando não reconhecido: $1"
        print_status "Use '$0 help' para ver comandos disponíveis"
        exit 1
        ;;
esac
