CREATE TABLE IF NOT EXISTS public.users(
    id serial NOT NULL,
    username varchar(255) unique,
    password varchar(255),
    email varchar(255) unique,
    PRIMARY KEY (id)
)