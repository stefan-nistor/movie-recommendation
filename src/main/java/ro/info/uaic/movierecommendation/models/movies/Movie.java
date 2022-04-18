package ro.info.uaic.movierecommendation.models.movies;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idMovie;
    private String name;
    private String description;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "movie_actors", joinColumns = @JoinColumn(name = "id_movie"), inverseJoinColumns = @JoinColumn(name = "id_actor"))
    private List<Actor> actors;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "movie_photos", joinColumns = @JoinColumn(name = "id_movie"), inverseJoinColumns = @JoinColumn(name = "id_photo"))
    private List<Photo> photos;
    @Enumerated(EnumType.STRING)
    private MovieType type;
    private String duration;
    private String director;
    private String writer;
    private boolean hasCaptions;
    private Date releaseDate;

}
