BEGIN;

    CREATE TABLE public.evento (
       codigo BIGSERIAL PRIMARY KEY,
       nome TEXT NOT NULL,
       data_evento DATE NOT NULL,
       local TEXT NOT NULL,
       capacidade BIGINT NOT NULL,
       finalizou_em DATE,
       codigo_usuario BIGINT NOT NULL
    );

    ALTER TABLE public.evento
    ADD CONSTRAINT fk_evento_usuario
    FOREIGN KEY (codigo_usuario)
    REFERENCES public.usuario (codigo);

END;