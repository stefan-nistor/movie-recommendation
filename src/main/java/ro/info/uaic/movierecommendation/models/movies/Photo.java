package ro.info.uaic.movierecommendation.models.movies;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_generator")
    @SequenceGenerator(name="photo_generator", sequenceName = "photo_seq", allocationSize=1)
    @Column(name = "id", nullable = false)
    private Long id;
    private byte[] imageAsByteArray;
}
