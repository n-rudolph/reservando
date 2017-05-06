package models;

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

    private static Finder<Long, Delivery> finder = new Finder<>(Delivery.class);

    public Delivery() {
    }

    public Delivery(String name, String address){
        super(name, address, false);
    }

    public Delivery(String name, String description, String address, String openingHour,
                    String closingHour, List<Day> openingDays, List<Cuisine> cuisines,
                    double radius, List<Meal> menu, Owner owner) {
        super(name, description, address, openingHour, closingHour, openingDays, cuisines, false, owner);
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

    public boolean addMeal(Meal meal){

        return false;
    }

    public boolean removeMeal(Meal meal) {
        return menu != null && menu.remove(meal);
    }

    public static Delivery byId(long id){
        return finder.byId(id);
    }
}
