package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Client;
import models.Qualification;
import models.Response.QualificationResponse;
import models.Restaurant;
import models.requestObjects.QualificationObject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class QualificationController extends Controller {

    public Result addQualification(){
        final JsonNode jsonNode = request().body().asJson();
        final String email = session().get("email");
        final Client client = Client.byEmail(email);
        final Qualification newQualification = Json.fromJson(jsonNode, QualificationObject.class).toQualification(client);

        final Qualification oldQualification = Qualification.getQualification(newQualification.getRestaurantId(), newQualification.getClientId());

        if (oldQualification == null){
            newQualification.save();
        }else{
            oldQualification.setQualification(newQualification.getQualification());
            oldQualification.update();
        }

        final QualificationResponse response = new QualificationResponse();
        response.clientQual = newQualification.getQualification();
        response.generalQual = Qualification.getRestaurantQualification(newQualification.getRestaurantId());
        return ok(Json.toJson(response));
    }

    public Result getRestaurantQualification(String rid){
        final Restaurant restaurant = Restaurant.byId(Long.parseLong(rid));
        return ok(Json.toJson(Qualification.getRestaurantQualification(restaurant.getId())));
    }

    public Result getUserRestaurantQualification(String rid){
        final Client client = Client.getClientByEmail(session().get("email"));
        final Restaurant restaurant = Restaurant.byId(Long.parseLong(rid));
        return ok(Json.toJson(Qualification.getQualification(restaurant.getId(), client.getId())));
    }
}
