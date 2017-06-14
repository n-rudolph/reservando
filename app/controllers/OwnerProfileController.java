package controllers;


import models.*;
import play.libs.Json;
import play.mvc.*;
import views.html.*;
import org.joda.time.DateTime;

import java.util.List;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's ownerProfile page .
 */
public class OwnerProfileController extends Controller {


    public Result ownerProfile(){ return ok(ownerProfile.render());}

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
                    DateTime reservationDate = reservation.getDate();
                    DateTime currentDate = new DateTime();
                    //Here is check is all the reservation has finished, or more specific if there is any reservation
                    //for the future. If there is one or more reservations for the future, the owner can not delete his
                    //account.
                    if(reservationDate.isAfter(currentDate)){
                        return internalServerError("No se ha podido eliminar la cuenta, debido a que tiene reservaciones pendientes.");
                    }
                }
            }
            //DeliveryOrder model must be change in order to implement some logic for the pending orders and the
            //finished ones.
           /* else {
                List<DeliveryOrder> deliveryOrders = ((Delivery) restaurant).getDeliveryOrders();
                for (DeliveryOrder deliveryOrder: deliveryOrders) {
                    //Here must goes the logic to know if a delivery order has been finished or not.
                }
            }*/
        }
        //This deletes the user and all the restaurants that he owns.
        owner.delete();
        session().clear();
        return ok();
    }
}
