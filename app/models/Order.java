package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Order extends Model {
    @Id
    private long id;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Delivery delivery;
    @ManyToMany
    private List<Meal> meals;
    private Address address;
    private double discount;

    public static Finder<Long, Order> find = new Finder<Long,Order>(Order.class);

    public Order() {
    }

    public Order(long id, Client client, Delivery delivery, List<Meal> meals, Address address, double discount) {
        this.id = id;
        this.client = client;
        this.delivery = delivery;
        this.meals = meals;
        this.address = address;
        this.discount = discount;
    }

    public long getId() {
        return id;
    }

    public Order setId(long id) {
        this.id = id;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public Order setClient(Client client) {
        this.client = client;
        return this;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public Order setDelivery(Delivery delivery) {
        this.delivery = delivery;
        return this;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public Order setMeals(List<Meal> meals) {
        this.meals = meals;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public Order setAddress(Address address) {
        this.address = address;
        return this;
    }

    public double getDiscount() {
        return discount;
    }

    public Order setDiscount(double discount) {
        this.discount = discount;
        return this;
    }
}
