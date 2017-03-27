package models;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("Local")
public class Local extends Restaurant{

    private int capacity;
    @ManyToMany
    private List<Meal> menu;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public static Finder<Long, Local> find = new Finder<Long,Local>(Local.class);

    public Local() {
    }

    public Local(String name, String address){
        super(name, address, true);
    }

    public Local(String name, String description, String address, String openingHour,
                 String closingHour, List<Day> openingDays, List<Cuisine> cuisines,
                 int capacity, List<Meal> menu) {
        super(name, description, address, openingHour, closingHour, openingDays, cuisines, true);
        this.capacity = capacity;
        this.menu = menu;
    }

    public int getCapacity() {
        return capacity;
    }

    public Local setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public List<Meal> getMenu() {
        return menu;
    }

    public Local setMenu(List<Meal> menu) {
        this.menu = menu;
        return this;
    }

    public static List<Local> all() { return find.all(); }
}
