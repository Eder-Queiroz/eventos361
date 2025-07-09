#!/bin/bash
set -e

# Script de inicialização do banco de dados
echo "Inicializando banco de dados para Eventos361..."

# Configurar locale pt_BR (se necessário)
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- Configurações iniciais do banco
    SET timezone = 'America/Sao_Paulo';
    
    -- Verificar se as tabelas já existem
    DO \$\$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'flyway_schema_history') THEN
            RAISE NOTICE 'Banco inicializado. Flyway será executado pela aplicação.';
        END IF;
    END
    \$\$;
EOSQL

echo "Banco de dados inicializado com sucesso!"
