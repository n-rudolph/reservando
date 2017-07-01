package models.requestObjects;

import models.Client;
import models.Qualification;
import models.Restaurant;

public class QualificationObject {
    public String rid;
    public double qualification;

    public Qualification toQualification(Client client){
        final Restaurant restaurant = Restaurant.byId(Long.parseLong(rid));
        return new Qualification(qualification, client.getId(), restaurant.getId());
    }
}
