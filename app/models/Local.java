package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Local extends Model{

    @Id
    private long id;
    private Restaurant restaurant;
    private int capacity;
    @ManyToMany
    private List<Cuisine> cuisines;
    @ManyToMany
    private List<Meal> menu;

    public static Finder<Long, Local> find = new Finder<Long,Local>(Local.class);
    
    public boolean isLocal(){
        return true;
    }

    public Local() {
    }

    public Local(long id, Restaurant restaurant, int capacity, List<Cuisine> cuisines, List<Meal> menu) {
        this.id = id;
        this.restaurant = restaurant;
        this.capacity = capacity;
        this.cuisines = cuisines;
        this.menu = menu;
    }

    public long getId() {
        return id;
    }

    public Local setId(long id) {
        this.id = id;
        return this;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Local setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    public int getCapacity() {
        return capacity;
    }

    public Local setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public List<Cuisine> getCuisines() {
        return cuisines;
    }

    public Local setCuisines(List<Cuisine> cuisines) {
        this.cuisines = cuisines;
        return this;
    }

    public List<Meal> getMenu() {
        return menu;
    }

    public Local setMenu(List<Meal> menu) {
        this.menu = menu;
        return this;
    }
}
