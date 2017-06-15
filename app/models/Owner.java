package models;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("Owner")
public class Owner extends User{

    @OneToMany(cascade = CascadeType.ALL)
    private List<Restaurant> restaurants;

    public static Finder<Long, Owner> find = new Finder<Long,Owner>(Owner.class);

    public Owner() {
    }

    public Owner(String firstName, String lastName, Address address, String email, String password, Photo photo, List<Restaurant> restaurants) {
        super(firstName, lastName, address, email, password, photo);
        this.restaurants = restaurants;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public Owner setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        return this;
    }

    public static Owner getOwnerbyEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }
}
