package models.requestObjects;

import models.Client;
import models.Owner;

import java.util.Collections;

public class UserRegisterObject {
    public String firstName;
    public String lastName;
    public AddressObject address;
    public String email;
    public String password;

    public boolean userType;

    public Client toClient(){
        return new Client(firstName, lastName, address.toAddress(), email, password, null, Collections.emptyList());
    }

    public Owner toOwner(){
        return new Owner(firstName, lastName, address.toAddress(), email, password, null, Collections.emptyList());
    }

}
