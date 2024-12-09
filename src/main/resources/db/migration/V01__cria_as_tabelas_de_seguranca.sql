BEGIN;

CREATE TABLE public.usuario
(
    codigo bigserial NOT NULL,
    nome text,
    email text,
    nome_usuario text,
    senha text,
    data_nascimento date,
    ativo boolean,
    PRIMARY KEY (codigo)
);

END;
