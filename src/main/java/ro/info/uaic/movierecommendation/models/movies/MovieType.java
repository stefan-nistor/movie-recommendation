package ro.info.uaic.movierecommendation.models.movies;


import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
public class MovieType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "type_generator")
    @SequenceGenerator(name="type_generator", sequenceName = "type_seq", allocationSize=1)
    @Column(name = "id", nullable = false)
    private Long id;
    private Type type;
}
