package ro.info.uaic.movierecommendation.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class User {
    private String userName;
    private String password;
    private Email eMail;
    private UUID id;

    public User(@JsonProperty("userName") String userName, @JsonProperty("password") String password,
                @JsonProperty("email") Email eMail, @JsonProperty("id") UUID id)
    {
        System.out.println("Constructor called!");
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Email geteMail() {
        return eMail;
    }

    public void seteMail(Email eMail) {
        this.eMail = eMail;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
