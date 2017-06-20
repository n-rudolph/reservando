package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class Qualification extends Model {

    @Id
    private long id;
    private double qualification;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Restaurant restaurant;

    public Qualification(double qualification, Client client, Restaurant restaurant){
        this.qualification = qualification;
        this.client = client;
        this.restaurant = restaurant;
    }

    public long getId() {
        return id;
    }

    public double getQualification() {
        return qualification;
    }

    public Qualification setQualification(double qualification) {
        this.qualification = qualification;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public Qualification setClient(Client client) {
        this.client = client;
        return this;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Qualification setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    private static Finder<Long, Qualification> finder = new Finder<>(Qualification.class);

    public static Qualification getQualification(Restaurant restaurant, Client client){
        return finder.where().eq("restaurant", restaurant).eq("client", client).findUnique();
    }

    public static double getRestaurantQualification(Restaurant restaurant) {
        double result = 0;
        final List<Qualification> rest = finder.where().eq("restaurant", restaurant).findList();
        for (Qualification qualification : rest) {
            result += qualification.getQualification();
        }
        if (rest.size() == 0)
            return 0;
        return result/rest.size();
    }
}
