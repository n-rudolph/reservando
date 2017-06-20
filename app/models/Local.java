package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Local")
public class Local extends Restaurant{

    private int capacity;
    private int minsBetweenTurns;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public static Finder<Long, Local> find = new Finder<Long,Local>(Local.class);

    public Local() {
    }

    public Local(String name, Address address){
        super(name, address, true);
        this.reservations = new ArrayList<>();
    }

    public Local(String name, String description, Address address, String openingHour,
                 String closingHour, List<Day> openingDays, List<Cuisine> cuisines,
                 int capacity, List<Meal> menu, Owner owner, int minsBetweenTurns) {
        super(name, description, address, openingHour, closingHour, openingDays, cuisines, true, owner, menu);
        this.capacity = capacity;
        this.reservations = new ArrayList<>();
        this.minsBetweenTurns = minsBetweenTurns;
    }

    public int getCapacity() {
        return capacity;
    }

    public Local setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public int getMinsBetweenTurns() {
        return minsBetweenTurns;
    }

    public Local setMinsBetweenTurns(int minsBetweenTurns) {
        this.minsBetweenTurns = minsBetweenTurns;
        return this;
    }

    public void setReservation(Reservation reservation){
        reservations.add(reservation);
    }

    public List<Reservation> getReservations(){return reservations;}

    public static List<Local> all() { return find.all(); }

    public static Local getLocalById(long id){return find.where().eq("id", id).findUnique();}
}
