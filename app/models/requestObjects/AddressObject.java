package models.requestObjects;

import models.Address;

import java.util.Optional;

public class AddressObject {

    public String name;
    public double lng;
    public double lat;
    public Optional<String> place;
    public Optional<String> city;
    public Optional<String> state;
    public Optional<String> country;
    public Optional<String> district;


    public Address toAddress(){
        return new Address(
                name,
                lat,
                lng,
                place.isPresent()? place.get() : "",
                city.isPresent()? place.get() : "",
                state.isPresent()? place.get() : "",
                country.isPresent()? place.get() : "",
                district.isPresent()? place.get() : ""
        );
    }
}
