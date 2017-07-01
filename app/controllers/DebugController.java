package controllers;

import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class DebugController extends Controller {

    public Result getUsers() {
        return ok(Json.toJson(User.all()));
    }

    public Result getRestaurants() { return ok(Json.toJson(Restaurant.allRestaurants())); }

    public Result getDays() {
        return ok(Json.toJson(Day.all()));
    }

    public Result getCuisines() {
        return ok(Json.toJson(Cuisine.all()));
    }

    public Result getMeals(){ return ok(Json.toJson(Meal.all())); }


}
