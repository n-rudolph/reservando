package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class CuisinePreference extends Model{
    @Id
    private long id;
    private long cuisineId;
    private long clientId;
    private int amount;

    private static Finder<Long, CuisinePreference> finder = new Finder<>(CuisinePreference.class);

    public CuisinePreference(long cuisineId, long clientId){
        this.cuisineId = cuisineId;
        this.clientId = clientId;
        this.amount = 1;
    }

    public long getId() {
        return id;
    }

    public long getCuisineId() {
        return cuisineId;
    }

    public CuisinePreference setCuisineId(long cuisineId) {
        this.cuisineId = cuisineId;
        return this;
    }

    public long getClientId() {
        return clientId;
    }

    public CuisinePreference setClientId(long clientId) {
        this.clientId = clientId;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public CuisinePreference incrementAmount() {
        this.amount++;
        return this;
    }

    public static CuisinePreference byId(long id){
        return finder.byId(id);
    }

    public static List<CuisinePreference> byClientId(long id){
        return finder.where().eq("clientId", id).findList();
    }

    public static CuisinePreference byClientCuisine(long clientId, long cuisineId) {
        return finder.where().eq("clientId", clientId).eq("cuisineId", cuisineId).findUnique();
    }
}
