package ro.info.uaic.movierecommendation.models.movies;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MovieType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "type_generator")
    @SequenceGenerator(name="type_generator", sequenceName = "type_seq", allocationSize=1)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(unique = true)
    private String name;
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "type_generator")
    @SequenceGenerator(name="type_generator", sequenceName = "type_seq", allocationSize=1)
    private Type type;

    public MovieType(Type type) {
        this.type = type;
    }
}
