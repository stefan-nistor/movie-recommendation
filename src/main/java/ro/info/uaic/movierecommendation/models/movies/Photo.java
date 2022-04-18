package ro.info.uaic.movierecommendation.models.movies;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idPhoto;
    private String imageAsString; //in base64

    public UUID getIdPhoto() {
        return idPhoto;
    }

    public String getImageAsString() {
        return imageAsString;
    }

    public void setIdPhoto(UUID idPhoto) {
        this.idPhoto = idPhoto;
    }

    public void setImageAsString(String imageAsString) {
        this.imageAsString = imageAsString;
    }
}
