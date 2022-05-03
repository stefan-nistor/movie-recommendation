package ro.info.uaic.movierecommendation.models.lists;

import lombok.Getter;
import lombok.Setter;
import ro.info.uaic.movierecommendation.models.movies.Movie;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "lists")
public class MovieList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "movielist_movie", joinColumns = @JoinColumn(name = "id_list"), inverseJoinColumns = @JoinColumn(name = "id_movie"))
    private List<Movie> movies;


}
