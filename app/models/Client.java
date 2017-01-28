package models;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("Client")
public class Client extends User {

    @ManyToMany
    private List<Cuisine> cuisinePreferences;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DeliveryOrder> deliveryOrders;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public static Finder<Long, Client> find = new Finder<Long,Client>(Client.class);

    public Client() {
    }

    public Client(long id, String name, DateTime birthday, Address address, String email, String password, Photo photo, List<Cuisine> cuisines) {
        super(id, name, birthday, address, email, password, photo);
        this.cuisinePreferences = cuisines;
    }

    public List<Cuisine> getCuisinePreferences() {
        return cuisinePreferences;
    }

    public Client setCuisinePreferences(List<Cuisine> cuisines) {
        this.cuisinePreferences = cuisines;
        return this;
    }

}
