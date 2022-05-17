package ro.info.uaic.movierecommendation.models.movies;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@ToString
@Getter
@Setter
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_generator")
    @SequenceGenerator(name="movie_generator", sequenceName = "movie_seq", allocationSize=1)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    @Lob
    private String description;
    @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(name = "movie_actors", joinColumns = @JoinColumn(name = "id_movie"), inverseJoinColumns = @JoinColumn(name = "id_actor"))
    private List<Actor> actors;
    // Images
    private String posterPath;
    private String backdropPath;
    @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(name = "movie_types", joinColumns = @JoinColumn(name = "id_movie"), inverseJoinColumns = @JoinColumn(name = "id_type"))
    private List<MovieType> type;
    private Long duration;
    private String director;
    private String writer;
    @Column(columnDefinition = "boolean default false")
    private boolean hasCaptions;
    private Date releaseDate;
    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;
    private Integer imdbId;

    // votes
    private Double voteAverage;
    private Long voteCount;
}
