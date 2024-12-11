BEGIN;

    CREATE TABLE public.participante (
        codigo BIGSERIAL PRIMARY KEY,
        codigo_usuario BIGINT NOT NULL,
        codigo_evento BIGINT NOT NULL
    );

    ALTER TABLE public.participante
    ADD CONSTRAINT fk_participante_usuario
    FOREIGN KEY (codigo_usuario)
    REFERENCES public.usuario (codigo);

    ALTER TABLE public.participante
    ADD CONSTRAINT fk_participante_evento
    FOREIGN KEY (codigo_evento)
    REFERENCES public.evento (codigo);

END;