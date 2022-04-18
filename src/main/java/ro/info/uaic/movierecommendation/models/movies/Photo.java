package ro.info.uaic.movierecommendation.models.movies;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idPhoto;
    byte[] imageAsByteArray;
}
