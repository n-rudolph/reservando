package models;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("Delivery")
public class Delivery extends Restaurant {

    private double radius;
    private int responseTime;

    private static Finder<Long, Delivery> finder = new Finder<>(Delivery.class);

    public Delivery() {
    }

    public Delivery(String name, Address address){
        super(name, address, false);
    }

    public Delivery(String name, String description, Address address, String openingHour,
                    String closingHour, List<Day> openingDays, List<Cuisine> cuisines,
                    double radius, List<Meal> menu, Owner owner, int responseTime) {
        super(name, description, address, openingHour, closingHour, openingDays, cuisines, false, owner, menu);
        this.radius = radius;
        this.responseTime = responseTime;
    }

    public double getRadius() {
        return radius;
    }

    public Delivery setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public Delivery setResponseTime(int responseTime) {
        this.responseTime = responseTime;
        return this;
    }

    public static List<Delivery> all(){return finder.all();}

    public static Delivery byId(long id){
        return finder.byId(id);
    }
}
