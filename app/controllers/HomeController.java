package controllers;

import authentication.NonUserSecured;
import authentication.SecuredClient;
import authentication.SecuredOwner;
import models.Cuisine;
import models.Day;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's clientHome page.
 */
public class HomeController extends Controller {

    @Security.Authenticated(NonUserSecured.class)
    public Result index() {
        return ok(login.render());
    }

    @Security.Authenticated(SecuredClient.class)
    public Result clientHome() {return ok(clientHome.render());}
    @Security.Authenticated(SecuredOwner.class)
    public Result ownerHome() {return ok(ownerHome.render());}
    @Security.Authenticated(SecuredOwner.class)
    public Result newRestaurant(){return ok(newRestaurant.render());}
    @Security.Authenticated(SecuredOwner.class)
    public Result ownerRestaurants(){
        return ok(ownerRestaurants.render());
    }
    @Security.Authenticated(SecuredOwner.class)
    public Result ownerRestaurant() { return ok(ownerRestaurantProfile.render()); }
    @Security.Authenticated(SecuredClient.class)
    public Result clientRestaurant() {
        return ok(restaurant.render());
    }

    public Result getDays() {
        return ok(Json.toJson(Day.all()));
    }
    public Result getCuisines() {
        return ok(Json.toJson(Cuisine.all()));
    }

}
