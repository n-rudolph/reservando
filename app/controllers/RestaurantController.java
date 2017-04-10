package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import models.Response.RedirectResponse;
import models.Response.RestaurantsResponse;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Collections;
import java.util.List;

public class RestaurantController extends Controller {

    public Result addRestaurant(){
        final JsonNode jsonNode = request().body().asJson();
        if (jsonNode.path("isLocal").isMissingNode())
            return badRequest("401");
        if (jsonNode.path("name").isMissingNode())
            return badRequest("402");
        if (jsonNode.path("address").isMissingNode())
            return badRequest("403");

        final String email = session().get("email");
        final Owner ownerbyEmail = Owner.getOwnerbyEmail(email);
        if (jsonNode.path("isLocal").asBoolean()){
            final Local local = new Local(jsonNode.path("name").asText(), jsonNode.path("address").asText());
            ownerbyEmail.addRestaurant(local);
        }else{
            final Delivery delivery = new Delivery(jsonNode.path("name").asText(), jsonNode.path("address").asText());
            ownerbyEmail.addRestaurant(delivery);
        }
        ownerbyEmail.save();
        return ok("Guardado con exito");
    }

    public Result getAll(){
        final String email = session().get("email");
        final Owner ownerbyEmail = Owner.getOwnerbyEmail(email);
        return ok(Json.toJson(ownerbyEmail.getRestaurants()));
    }

    public Result getFirsts(){
        final String email = session().get("email");
        final Owner ownerbyEmail = Owner.getOwnerbyEmail(email);
        final List<Restaurant> restaurants = ownerbyEmail.getRestaurants();
        int max = restaurants.size() < 5 ? restaurants.size() : 5;

        final RestaurantsResponse response = new RestaurantsResponse(200, "ok", restaurants.subList(0, max), restaurants.size() > 5);

        return ok(Json.toJson(response));
    }

    public Result openRestaurantProfile(){
        final JsonNode jsonNode = request().body().asJson();
        final JsonNode restaurantId = jsonNode.path("id");
        String redirectUrl;
        if (jsonNode.path("local").asBoolean())
            redirectUrl = "/local/profile";
        else redirectUrl = "/delivery/profile";
        redirectUrl += "?rid="+restaurantId;
        final RedirectResponse response = new RedirectResponse(200, "ok", redirectUrl);
        return ok(Json.toJson(response));
    }
}
