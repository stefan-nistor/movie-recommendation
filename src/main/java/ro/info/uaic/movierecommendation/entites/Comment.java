package ro.info.uaic.movierecommendation.entites;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.info.uaic.movierecommendation.models.movies.Movie;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_generator")
    @SequenceGenerator(name="comments_generator", sequenceName = "comments_seq", allocationSize=1)
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToOne
    @JoinColumn(name = "id_movie", referencedColumnName = "id")
    private Movie movie;
    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private UserEntity user;
    private String content;
}
