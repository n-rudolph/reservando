package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class Order extends Model {
    @Id
    private long id;
    @OneToOne
    private Client client;
    @OneToOne
    private Delivery delivery;
    @ManyToMany
    private List<Meal> meals;
    private String address;
    private double discount;

    public static Finder<Long, Order> find = new Finder<Long,Order>(Order.class);

    public Order() {
    }

    public Order(long id, Client client, Delivery delivery, List<Meal> meals, String address, double discount) {
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

    public String getAddress() {
        return address;
    }

    public Order setAddress(String address) {
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
