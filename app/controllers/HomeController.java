package controllers;

import play.api.i18n.*;
import play.mvc.*;
import views.html.*;
import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's clientHome page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */

    private MessagesApi messagesApi;

    @Inject
    public HomeController(MessagesApi messagesApi){
        this.messagesApi = messagesApi;
    }

    public Result index() {
        play.api.i18n.Messages messages = messagesApi.preferred(request());
        return ok(login.render(messages));
    }

    public Result clientHome() {return ok(clientHome.render());}
    public Result ownerHome() {return ok(ownerHome.render());}
    public Result newRestaurant(){return ok(newRestaurant.render());}
    public Result ownerRestaurants(){
        return ok(ownerRestaurants.render());
    }
    public Result ownerRestaurant() { return ok(ownerRestaurantProfile.render()); }
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
