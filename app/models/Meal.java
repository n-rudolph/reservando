package models;


import com.avaje.ebean.Model;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.List;

@Entity
public class Meal extends Model {
    @Id
    private long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private double price;
    @OneToOne
    @Nullable
    private Photo photo;
    private boolean isDeleted;

    @ManyToOne
    private Restaurant restaurant;


    public Meal() {
        isDeleted = false;
    }

    public Meal(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
        isDeleted = false;
    }

    public Meal(long id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        isDeleted = false;
    }

    public long getId() {
        return id;
    }

    public Meal setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Meal setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Meal setDescription(String description) {
        this.description = description;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public Meal setPrice(double price) {
        this.price = price;
        return this;
    }

    public Photo getImage() {
        return photo;
    }

    public Meal setImage(Photo photo) {
        this.photo = photo;
        return this;
    }

    public Meal setDeleted(boolean isDeleted){
        this.isDeleted = isDeleted;
        return this;
    }
    public boolean isDeleted(){
        return isDeleted;
    }

    public Meal setRestaurant(Restaurant restaurant){
        this.restaurant = restaurant;
        return this;
    }

    private static Finder<Long, Meal> finder = new Finder<Long,Meal>(Meal.class);

    public static Meal byId(long id) {
        return finder.byId(id);
    }

    public static List<Meal> getByRestaurant(Restaurant restaurant) {
        return finder.where().eq("restaurant", restaurant).findList();
    }

    public static List<Meal> all(){return finder.all();}
}
