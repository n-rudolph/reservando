package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Qualification extends Model {

    @Id
    private long id;
    private double qualification;
    private long clientId;
    private long restaurantId;

    public Qualification(double qualification, long clientId, long restaurantId){
        this.qualification = qualification;
        this.clientId = clientId;
        this.restaurantId = restaurantId;
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

    public long getClientId() {
        return clientId;
    }

    public Qualification setClient(long clientId) {
        this.clientId = clientId;
        return this;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public Qualification setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
        return this;
    }

    private static Finder<Long, Qualification> finder = new Finder<>(Qualification.class);

    public static Qualification getQualification(long restaurantId, long clientId){
        return finder.where().eq("restaurantId", restaurantId).eq("clientId", clientId).findUnique();
    }

    public static double getRestaurantQualification(long restaurantId) {
        double result = 0;
        final List<Qualification> rest = finder.where().eq("restaurantId", restaurantId).findList();
        for (Qualification qualification : rest) {
            result += qualification.getQualification();
        }
        if (rest.size() == 0)
            return -1;
        return result/rest.size();
    }
}
