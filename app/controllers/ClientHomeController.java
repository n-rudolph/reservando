package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.org.apache.regexp.internal.RE;
import models.Cuisine;
import models.Restaurant;
import play.libs.Json;
import play.mvc.*;
import views.html.localView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gustavo on 4/6/17.
 */
public class ClientHomeController extends Controller {

    private Long localSelectedId;

    public Result search(){
        JsonNode jsonNode = request().body().asJson();

        String wordToSearch = jsonNode.path("wordToSearch").asText();
        boolean searchByName = jsonNode.path("filterByName").asBoolean();
        boolean searchByCuisine = jsonNode.path("filterByCuisine").asBoolean();
        boolean searchByLocalization = jsonNode.path("filterByLocalization").asBoolean();
        boolean searchByRestaurant = jsonNode.path("filterByRestaurant").asBoolean();
        boolean searchByDelivery = jsonNode.path("filterByDelivery").asBoolean();

        List<Restaurant> results = new ArrayList<>();

        if(searchByName){
            List<Restaurant> aux = Restaurant.finder.where()
                    .ieq("name", wordToSearch).findList();
            results.addAll(aux);
        }
        if(searchByCuisine){
            //It must be change if a better (efficient) way of search is found.
            List<Restaurant> aux = Restaurant.finder.findList();
            results.addAll(filterByCuisine(aux, wordToSearch));
        }
        /*
        if(searchByLocalization){
            List<Restaurant> aux = Restaurant.finder.where()
                    .ieq("name", wordToSearch).findList();
        }*/
        if(searchByRestaurant){
            List<Restaurant> aux = Restaurant.finder.where()
                    .ieq("name", wordToSearch)
                    .eq("isLocal", true)
                    .findList();
            results.addAll(aux);
        }
        if(searchByDelivery){
            List<Restaurant> aux = Restaurant.finder.where()
                    .ieq("name", wordToSearch)
                    .eq("isLocal", false)
                    .findList();
            results.addAll(aux);
        }

        return ok(Json.toJson(results));
    }

    public Result openLocal(){
        return ok(localView.render());
    }

    public Result setLocalSelected(){
        JsonNode jsonNode = request().body().asJson();
        localSelectedId = jsonNode.path("localSelectedId").asLong();
        return ok();
    }

    public Result getLocalSelected(){
        Restaurant restaurantSelected = Restaurant.finder.byId(localSelectedId);
        return ok(Json.toJson(restaurantSelected));
    }

    private List<Restaurant> filterByCuisine(List<Restaurant> restaurants, String cuisine){
        List<Restaurant> result = new ArrayList<>();
        for (Restaurant restaurant: restaurants) {
            List<Cuisine> cuisines = restaurant.getCuisines();
            Cuisine cuisineToSearch = Cuisine.getCuisine(getCorrectCuisineFormat(cuisine));
            if(cuisines.contains(cuisineToSearch)) result.add(restaurant);
        }
        return result;
    }

    //This method is use to get the correct format of the cuisines, the same format as it was saved.
    private String getCorrectCuisineFormat(String cuisine){
        String first = cuisine.substring(0,1).toUpperCase();
        String second = cuisine.substring(1, cuisine.length()).toLowerCase();
        return first+second;
    }
}