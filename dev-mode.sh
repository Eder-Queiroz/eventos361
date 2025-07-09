#!/bin/bash

# Script para modo de desenvolvimento do projeto Eventos361
# Executa a aplicação com hot reload e watch mode para CSS

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_status() {
    echo -e "${BLUE}[DEV]${NC} $1"
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

print_status "🚀 Iniciando modo de desenvolvimento..."

# Função para cleanup quando o script for interrompido
cleanup() {
    print_status "Parando processos..."
    kill $CSS_PID $SPRING_PID 2>/dev/null || true
    exit 0
}

trap cleanup SIGINT SIGTERM

# Verificar se as dependências estão instaladas
if [ ! -d "node_modules" ]; then
    print_warning "Dependências do Node.js não encontradas. Instalando..."
    pnpm install
fi

# Iniciar watch mode para CSS em background
print_status "Iniciando watch mode para Tailwind CSS..."
pnpm run watch &
CSS_PID=$!
print_success "CSS watch mode iniciado (PID: $CSS_PID)"

# Aguardar um pouco para o CSS ser compilado
sleep 2

# Iniciar aplicação Spring Boot com DevTools
print_status "Iniciando aplicação Spring Boot..."
mvn spring-boot:run -Dspring-boot.run.fork=false &
SPRING_PID=$!

print_success "🎉 Modo de desenvolvimento ativo!"
echo
print_status "Aplicação disponível em:"
echo -e "${GREEN}  https://localhost:8443${NC}"
echo
print_status "Recursos ativos:"
echo -e "${GREEN}  ✓${NC} Hot reload (Spring Boot DevTools)"
echo -e "${GREEN}  ✓${NC} CSS watch mode (Tailwind)"
echo -e "${GREEN}  ✓${NC} Auto-recompilação"
echo
print_warning "Pressione Ctrl+C para parar todos os processos"

# Aguardar até que o usuário interrompa
wait $SPRING_PID
