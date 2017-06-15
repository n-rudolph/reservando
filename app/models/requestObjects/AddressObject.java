package models.requestObjects;

import models.Address;

public class AddressObject {

    public String addressString;
    public double lng;
    public double lat;

    public Address toAddress(){return new Address(addressString, lat,lng);}
}
