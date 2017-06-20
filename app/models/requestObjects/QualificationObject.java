package models.requestObjects;

import models.Client;
import models.Qualification;
import models.Restaurant;

public class QualificationObject {
    public String rid;
    public String cid;
    public double qualification;

    public Qualification toQualification(){
        final Restaurant restaurant = Restaurant.byId(Long.parseLong(rid));
        final Client client = Client.byId(Long.parseLong(cid));
        return new Qualification(qualification, client, restaurant);
    }
}
