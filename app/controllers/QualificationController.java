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
        final double qualification = jsonNode.path("qualification").asDouble();
        final Client client = Json.fromJson(jsonNode.path("client"), Client.class);
        final Restaurant restaurant = Json.fromJson(jsonNode.path("restaurant"), Restaurant.class);

        /*final Qualification newQualification = Json.fromJson(jsonNode, QualificationObject.class).toQualification();*/
        final Qualification newQualification = new Qualification(qualification,client,restaurant);

        final Qualification oldQualification = Qualification.getQualification(newQualification.getRestaurant(), newQualification.getClient());

        if (oldQualification == null){
            newQualification.save();
        }else{
            oldQualification.setQualification(newQualification.getQualification());
            oldQualification.update();
        }

        final QualificationResponse response = new QualificationResponse();
        response.clientQual = newQualification.getQualification();
        response.generalQual = Qualification.getRestaurantQualification(newQualification.getRestaurant());
        return ok(Json.toJson(response));
    }

    public Result getRestaurantQualification(String rid){
        final Restaurant restaurant = Restaurant.byId(Long.parseLong(rid));
        return ok(Json.toJson(Qualification.getRestaurantQualification(restaurant)));
    }

    public Result getUserRestaurantQualification(String cid, String rid){
        final Client client = Client.byId(Long.parseLong(cid));
        final Restaurant restaurant = Restaurant.byId(Long.parseLong(rid));
        return ok(Json.toJson(Qualification.getQualification(restaurant, client)));
    }
}
