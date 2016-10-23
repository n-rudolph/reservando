package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Delivery extends Model {

    @Id
    private long id;

    private Restaurant restaurant;
    private double radius;
    @ManyToMany
    List<Meal> menu;
    @Lob
    private byte[] image;

    public static Finder<Long, Delivery> find = new Finder<Long,Delivery>(Delivery.class);

    public boolean isLocal(){
        return false;
    }

    public Delivery() {
    }

    public Delivery(long id, Restaurant restaurant, double radius, List<Meal> menu, byte[] image) {
        this.id = id;
        this.restaurant = restaurant;
        this.radius = radius;
        this.menu = menu;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public Delivery setId(long id) {
        this.id = id;
        return this;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Delivery setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    public double getRadius() {
        return radius;
    }

    public Delivery setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public List<Meal> getMenu() {
        return menu;
    }

    public Delivery setMenu(List<Meal> menu) {
        this.menu = menu;
        return this;
    }

    public byte[] getImage() {
        return image;
    }

    public Delivery setImage(byte[] image) {
        this.image = image;
        return this;
    }
}
