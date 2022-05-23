package ro.info.uaic.movierecommendation.models.movies;


import lombok.Getter;
import lombok.Setter;
import ro.info.uaic.movierecommendation.entites.UserEntity;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_generator")
    @SequenceGenerator(name="comments_generator", sequenceName = "comments_seq", allocationSize=1)
    @Column(name = "id", nullable = false)
    private long id;
    @JoinColumn(name = "id_movie", referencedColumnName = "id")
    private Movie movie;
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private UserEntity user;
    private String content;
}
