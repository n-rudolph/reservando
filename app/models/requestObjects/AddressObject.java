package models.requestObjects;

import models.Address;

import java.util.Optional;

public class AddressObject {

    public String completeAddress;
    public double lng;
    public double lat;
    public Optional<String> place;
    public Optional<String> city;
    public Optional<String> state;
    public Optional<String> country;
    public Optional<String> district;


    public Address toAddress(){
        return new Address(
                completeAddress,
                lat,
                lng,
                place!=null ? (place.isPresent()? place.get() : "") : "",
                city!=null  ? (city.isPresent()? city.get() : "") : "",
                state!=null ? (state.isPresent()? state.get() : ""):"",
                country!=null ? (country.isPresent()? country.get():"") : "",
                district!=null ? (district.isPresent()? district.get() : ""):""
        );
    }
}
