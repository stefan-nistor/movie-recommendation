package ro.info.uaic.movierecommendation.models.movies;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "actors")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actor_generator")
    @SequenceGenerator(name="actor_generator", sequenceName = "actor_seq", allocationSize=1)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private int numberOfAwards;

}
