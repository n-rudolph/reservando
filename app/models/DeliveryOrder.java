package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class DeliveryOrder extends Model {
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

    public static Finder<Long, DeliveryOrder> find = new Finder<Long, DeliveryOrder>(DeliveryOrder.class);

    public DeliveryOrder() {
    }

    public DeliveryOrder(long id, Client client, Delivery delivery, List<Meal> meals, Address address, double discount) {
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

    public DeliveryOrder setId(long id) {
        this.id = id;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public DeliveryOrder setClient(Client client) {
        this.client = client;
        return this;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public DeliveryOrder setDelivery(Delivery delivery) {
        this.delivery = delivery;
        return this;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public DeliveryOrder setMeals(List<Meal> meals) {
        this.meals = meals;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public DeliveryOrder setAddress(Address address) {
        this.address = address;
        return this;
    }

    public double getDiscount() {
        return discount;
    }

    public DeliveryOrder setDiscount(double discount) {
        this.discount = discount;
        return this;
    }
}
