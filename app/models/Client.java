package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Client extends Model {

    @Id
    private long id;
    private User user;

    @ManyToMany
    private List<Cuisine> cuisines;
    private List<Integer> amountConsumed;


    public static Finder<Long, Client> find = new Finder<Long,Client>(Client.class);

    public Client() {
    }

    public Client(long id, User user, List<Cuisine> cuisines, List<Integer> amountConsumed) {
        this.id = id;
        this.user = user;
        this.cuisines = cuisines;
        this.amountConsumed = amountConsumed;
    }

    public long getId() {
        return id;
    }

    public Client setId(long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Client setUser(User user) {
        this.user = user;
        return this;
    }

    public List<Cuisine> getCuisines() {
        return cuisines;
    }

    public Client setCuisines(List<Cuisine> cuisines) {
        this.cuisines = cuisines;
        return this;
    }

    public List<Integer> getAmountConsumed() {
        return amountConsumed;
    }

    public Client setAmountConsumed(List<Integer> amountConsumed) {
        this.amountConsumed = amountConsumed;
        return this;
    }
}
