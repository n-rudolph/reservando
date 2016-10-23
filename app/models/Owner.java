package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Owner extends Model{

    @Id
    private long id;
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Restaurant> restaurants;

    public static Finder<Long, Owner> find = new Finder<Long,Owner>(Owner.class);

    public Owner() {
    }

    public Owner(long id, User user, List<Restaurant> restaurants) {
        this.id = id;
        this.user = user;
        this.restaurants = restaurants;
    }

    public long getId() {
        return id;
    }

    public Owner setId(long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Owner setUser(User user) {
        this.user = user;
        return this;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public Owner setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        return this;
    }
}
