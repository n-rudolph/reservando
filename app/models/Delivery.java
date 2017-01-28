package models;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("Delivery")
public class Delivery extends Restaurant {

    private double radius;
    @ManyToMany
    private List<Meal> menu;
    @OneToMany(cascade = CascadeType.ALL)
    private List<DeliveryOrder> deliveryOrders;

    public static Finder<Long, Delivery> find = new Finder<Long,Delivery>(Delivery.class);

    public Delivery() {
    }

    public Delivery(long id, String name, String description, Address address, DateTime openingHour, DateTime closingHour, List<Days> openingDays, List<Cuisine> cuisines, double radius, List<Meal> menu) {
        super(id, name, description, address, openingHour, closingHour, openingDays, cuisines);
        this.radius = radius;
        this.menu = menu;
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
}
