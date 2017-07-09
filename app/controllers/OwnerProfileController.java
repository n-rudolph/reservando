package controllers;


import models.*;
import org.springframework.core.annotation.Order;
import play.libs.Json;
import play.mvc.*;
import views.html.*;
import org.joda.time.DateTime;
import javax.inject.Inject;
import play.api.i18n.*;

import java.util.List;
import java.util.Random;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's ownerProfile page .
 */
public class OwnerProfileController extends Controller {

    private MessagesApi messagesApi;

    @Inject
    public OwnerProfileController(MessagesApi messagesApi){
        this.messagesApi = messagesApi;
    }

    /*public Result ownerProfile(){
        Messages messages = messagesApi.preferred(request());
        return ok(ownerProfile.render(messages));
    }*/

    public Result getOwner(){
        Owner owner = getCurrentOwner();
        return ok(Json.toJson(owner));
    }

    private Owner getCurrentOwner(){
        return Owner.getOwnerbyEmail(session().get("email"));
    }

    public Result deleteAccount(){
        Owner owner = getCurrentOwner();
        owner.setEmail("o" + new Random().toString() + new Random().toString() + new Random().toString() + "@p"+new Random().toString()+new Random().toString()+new Random().toString()+".com");
        owner.setActive();
        owner.update();
        owner.getRestaurants().forEach(restaurant -> {
            restaurant.setDeleted(true);
            restaurant.update();
            if (restaurant.isLocal()) {
                Reservation.byLocal(Local.getLocalById(restaurant.getId())).forEach(reservation -> {
                    reservation.setActive(false);
                    reservation.delete();
                });
            } else {
                DeliveryOrder.getRestaurantOrders(Delivery.byId(restaurant.getId())).forEach(deliveryOrder -> {
                    deliveryOrder.setActive(false);
                    deliveryOrder.delete();
                });
            }
        });
        session().clear();
        return ok();
    }
}
