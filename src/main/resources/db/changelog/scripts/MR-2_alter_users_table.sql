ALTER TABLE public.users
    ADD COLUMN IF NOT EXISTS firstname character varying(255);

ALTER TABLE public.users
    ADD COLUMN IF NOT EXISTS lastname character varying(255);