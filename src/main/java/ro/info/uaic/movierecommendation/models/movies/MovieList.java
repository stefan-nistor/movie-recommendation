package ro.info.uaic.movierecommendation.models.movies;
import lombok.*;
import ro.info.uaic.movierecommendation.entites.UserEntity;
import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lists")
public class MovieList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "movielist_movie",
            joinColumns = @JoinColumn(name = "id_list"),
            inverseJoinColumns = @JoinColumn(name = "id_movie"))
    private List<Movie> movies;

    @ManyToOne
    @JoinColumn(name="id_user")
    private UserEntity user;

    @Override
    public String toString() {
        return "MovieList{" + "id=" + id + ", name='" + name + '\'' + ", movies=" + movies + '}';
    }
}
