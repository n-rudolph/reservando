package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Address extends Model {

    @Id
    private Long id;

    private String address;
    private double lat;
    private double lng;

    public Address(String address, double lat, double lng){
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public Address setAddress(String address) {
        this.address = address;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Address setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public Address setLng(double lng) {
        this.lng = lng;
        return this;
    }

    private static Finder<Long, Address> finder = new Finder<>(Address.class);

    public static List<Address> find(String address) {
        return finder.where().like("address", address).findList();
    }
}
