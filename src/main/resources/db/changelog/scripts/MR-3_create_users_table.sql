CREATE TABLE IF NOT EXISTS public.users(
    id serial unique,
    username varchar(255) unique,
    password varchar(255),
    email varchar(255)
)