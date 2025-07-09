#!/bin/bash

# Script para parar todos os processos relacionados ao projeto Eventos361

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_status() {
    echo -e "${BLUE}[STOP]${NC} $1"
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

print_status "🛑 Parando processos do projeto Eventos361..."

# Parar processos Spring Boot
SPRING_PIDS=$(pgrep -f "spring-boot:run\|Application" | tr '\n' ' ')
if [ ! -z "$SPRING_PIDS" ]; then
    print_status "Parando aplicação Spring Boot (PIDs: $SPRING_PIDS)..."
    kill $SPRING_PIDS 2>/dev/null || true
    sleep 2
    # Force kill se ainda estiver rodando
    kill -9 $SPRING_PIDS 2>/dev/null || true
    print_success "Aplicação Spring Boot parada"
else
    print_warning "Nenhum processo Spring Boot encontrado"
fi

# Parar processos CSS watch
CSS_PIDS=$(pgrep -f "postcss.*watch" | tr '\n' ' ')
if [ ! -z "$CSS_PIDS" ]; then
    print_status "Parando CSS watch mode (PIDs: $CSS_PIDS)..."
    kill $CSS_PIDS 2>/dev/null || true
    print_success "CSS watch mode parado"
else
    print_warning "Nenhum processo CSS watch encontrado"
fi

# Parar processos Maven que possam estar rodando
MVN_PIDS=$(pgrep -f "mvn.*spring-boot" | tr '\n' ' ')
if [ ! -z "$MVN_PIDS" ]; then
    print_status "Parando processos Maven (PIDs: $MVN_PIDS)..."
    kill $MVN_PIDS 2>/dev/null || true
    sleep 2
    kill -9 $MVN_PIDS 2>/dev/null || true
    print_success "Processos Maven parados"
fi

# Parar processos Node.js relacionados ao projeto
NODE_PIDS=$(pgrep -f "node.*postcss\|pnpm.*watch" | tr '\n' ' ')
if [ ! -z "$NODE_PIDS" ]; then
    print_status "Parando processos Node.js (PIDs: $NODE_PIDS)..."
    kill $NODE_PIDS 2>/dev/null || true
    print_success "Processos Node.js parados"
fi

print_success "🎉 Todos os processos foram parados!"

# Verificar se ainda há processos rodando na porta 8443
if lsof -ti:8443 >/dev/null 2>&1; then
    print_warning "Ainda há processos usando a porta 8443"
    print_status "Para forçar a parada: sudo lsof -ti:8443 | xargs kill -9"
fi

# Verificar se ainda há processos rodando na porta 8080
if lsof -ti:8080 >/dev/null 2>&1; then
    print_warning "Ainda há processos usando a porta 8080"
    print_status "Para forçar a parada: sudo lsof -ti:8080 | xargs kill -9"
fi
