package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("Client")
public class Client extends User {

    @ManyToMany
    private List<Cuisine> cuisinePreferences;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public static Finder<Long, Client> find = new Finder<Long,Client>(Client.class);

    public Client() {
    }

    public Client(String firstName, String lastName, Address address, String email, String password, Photo photo, List<Cuisine> cuisines) {
        super(firstName, lastName, address, email, password, photo);
        this.cuisinePreferences = cuisines;
    }

    public List<Cuisine> getCuisinePreferences() {
        return cuisinePreferences;
    }

    public Client setCuisinePreferences(List<Cuisine> cuisines) {
        this.cuisinePreferences = cuisines;
        return this;
    }

    public List<Reservation> getReservations(){return reservations;}


    public static Client getClientByEmail(String email){
        return find.where().eq("email", email).findUnique();
    }

    public static Client byId(long id){
        return find.byId(id);
    }

    public static Client byEmail(String email){
        return find.where().eq("email", email).findUnique();
    }

}
