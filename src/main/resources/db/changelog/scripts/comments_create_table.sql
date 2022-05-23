CREATE TABLE IF NOT EXISTS public.comments(
    id serial NOT NULL,
    id_user integer NOT NULL,
    id_movie integer NOT NULL,
    content varchar(1000) NOT NULL,
    foreign key (id_user) references users(id),
    foreign key (id_movie) references movies(id),
    PRIMARY KEY (id)
)