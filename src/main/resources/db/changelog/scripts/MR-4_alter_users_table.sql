ALTER TABLE public.users
    ADD COLUMN IF NOT EXISTS password_token character varying(255);
