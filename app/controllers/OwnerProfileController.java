package controllers;


import models.*;
import play.libs.Json;
import play.mvc.*;
import views.html.*;
import org.joda.time.DateTime;
import javax.inject.Inject;
import play.api.i18n.*;

import java.util.List;


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

    public Result ownerProfile(){
        Messages messages = messagesApi.preferred(request());
        return ok(ownerProfile.render(messages));
    }

    public Result getOwner(){
        Owner owner = getCurrentOwner();
        return ok(Json.toJson(owner));
    }

    private Owner getCurrentOwner(){
        return Owner.getOwnerbyEmail(session().get("email"));
    }

    public Result deleteAccount(){
        Owner owner = getCurrentOwner();
        List<Restaurant> restaurants = owner.getRestaurants();
        for (Restaurant restaurant: restaurants) {
            if(restaurant.isLocal()){
                List<Reservation> reservations = ((Local) restaurant).getReservations();
                for (Reservation reservation: reservations) {
                    DateTime reservationDate = new DateTime(reservation.getDate());
                    DateTime currentDate = new DateTime();
                    //Here is check is all the reservation has finished, or more specific if there is any reservation
                    //for the future. If there is one or more reservations for the future, the owner can not delete his
                    //account.
                    if(reservationDate.isAfter(currentDate)){
                        return internalServerError("No se ha podido eliminar la cuenta, debido a que tiene reservaciones pendientes.");
                    }
                }
            }
            else {
                List<DeliveryOrder> deliveryOrders = DeliveryOrder.getRestaurantOrders((Delivery) restaurant);
                for (DeliveryOrder deliveryOrder: deliveryOrders){
                    DateTime orderDatePlaced = deliveryOrder.getTimePlaced();
                    DateTime orderDateFinished = orderDatePlaced.plusMinutes(deliveryOrder.getDelivery().getResponseTime());
                    DateTime currentDate = new DateTime();
                    //Here is check is all the orders has finished, or more specific if there is any order
                    //for the future. If there is one or more order for the future, the owner can not delete his
                    //account.
                    if(orderDateFinished.isAfter(currentDate)){
                        return internalServerError("No se ha podido eliminar la cuenta debido a que tiene ordenes pendientes.");
                    }
                }
            }
        }
        //This deletes the user and all the restaurants that he owns.
        owner.delete();
        session().clear();
        return ok();
    }
}
