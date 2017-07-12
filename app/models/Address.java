package models;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Address extends Model {

    @Id
    private Long id;

    private String completeAddress;
    private double lat;
    private double lng;
    private String place;
    private String city;
    private String state;
    private String country;
    private String district;

    public Address(String completeAddress, double lat, double lng) {
        this.completeAddress = completeAddress;
        this.lat = lat;
        this.lng = lng;
    }
    public Address(String completeAddress, double lat, double lng, String place, String city, String state, String country, String district){
        this.completeAddress = completeAddress;
        this.lat = lat;
        this.lng = lng;
        this.place = place;
        this.city = city;
        this.state = state;
        this.country = country;
        this.district = district;
    }

    public long getId() {
        return id;
    }

    public String getCompleteAddress() {
        return completeAddress;
    }

    public Address setCompleteAddress(String completeAddress) {
        this.completeAddress = completeAddress;
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

    public String getPlace() {
        return place;
    }

    public Address setPlace(String place) {
        this.place = place;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Address setCity(String city) {
        this.city = city;
        return this;
    }

    public String getState() {
        return state;
    }

    public Address setState(String state) {
        this.state = state;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Address setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public Address setDistrict(String district) {
        this.district = district;
        return this;
    }

    private static Finder<Long, Address> finder = new Finder<>(Address.class);

    public static List<Address> find(String address) {
        return finder.where().like("address", address).findList();
    }
    public static List<Address> findPlace(String place) {
        return finder.where().or(Expr.like("city", place),
                Expr.or(Expr.like("state", place),
                        Expr.or(Expr.like("country", place),
                                Expr.like("district", place)))).findList();
    }
}
