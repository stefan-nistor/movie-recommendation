package ro.info.uaic.movierecommendation.models.movies;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter
@Entity
public class MovieType {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    private Type type;

}
