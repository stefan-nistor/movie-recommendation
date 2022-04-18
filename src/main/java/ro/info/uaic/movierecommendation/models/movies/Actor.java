package ro.info.uaic.movierecommendation.models.movies;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idActor;
    private String name;
    private int numberOfAwards;

    public UUID getIdActor() {
        return idActor;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfAwards() {
        return numberOfAwards;
    }

    public void setIdActor(UUID idActor) {
        this.idActor = idActor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfAwards(int numberOfAwards) {
        this.numberOfAwards = numberOfAwards;
    }
}
