package ro.info.uaic.movierecommendation.models.movies;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ToString
@Getter
@Setter
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;
    private String name;
    private String description;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "movie_actors", joinColumns = @JoinColumn(name = "id_movie"), inverseJoinColumns = @JoinColumn(name = "id_actor"))
    private List<Actor> actors;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "movie_photos", joinColumns = @JoinColumn(name = "id_movie"), inverseJoinColumns = @JoinColumn(name = "id_photo"))
    private List<Photo> photos;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "movie_types", joinColumns = @JoinColumn(name = "id_movie"), inverseJoinColumns = @JoinColumn(name = "id_type"))
    private List<MovieType> type;
    private String duration;
    private String director;
    private String writer;
    private boolean hasCaptions;
    private Date releaseDate;

}
