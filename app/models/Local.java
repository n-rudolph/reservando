package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Local")
public class Local extends Restaurant{

    private int capacity;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public static Finder<Long, Local> find = new Finder<Long,Local>(Local.class);

    public Local() {
    }

    public Local(String name, String address){
        super(name, address, true);
        this.reservations = new ArrayList<>();
    }

    public Local(String name, String description, String address, String openingHour,
                 String closingHour, List<Day> openingDays, List<Cuisine> cuisines,
                 int capacity, List<Meal> menu, Owner owner) {
        super(name, description, address, openingHour, closingHour, openingDays, cuisines, true, owner, menu);
        this.capacity = capacity;
        this.reservations = new ArrayList<>();
    }

    public int getCapacity() {
        return capacity;
    }

    public Local setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public void setReservation(Reservation reservation){
        reservations.add(reservation);
    }

    public List<Reservation> getReservations(){return reservations;}

    public static List<Local> all() { return find.all(); }

    public static Local getLocalById(long id){return find.where().eq("id", id).findUnique();}
}
