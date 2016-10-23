package models;


import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Meal extends Model {
    @Id
    private long id;
    private String name;
    private String description;
    private double price;
    @Lob
    private byte[] image;

    public static Finder<Long, Meal> find = new Finder<Long,Meal>(Meal.class);

    public Meal() {
    }

    public Meal(long id, String name, String description, double price, byte[] image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
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

    public byte[] getImage() {
        return image;
    }

    public Meal setImage(byte[] image) {
        this.image = image;
        return this;
    }
}
