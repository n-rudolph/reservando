package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import org.joda.time.DateTime;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's clientProfile page.
 */
public class ClientProfileController extends Controller {

    public Result clientProfile(){ return ok(clientProfile.render());}

    public Result getClient(){
        Client client = getCurrentClient();
        return ok(Json.toJson(client));
    }

    private Client getCurrentClient() { return Client.getClientByEmail(session().get("email")); }

    public Result deleteAccount(){
        Client client = getCurrentClient();
        List<Reservation> reservations = client.getReservations();
        List<DeliveryOrder> deliveryOrders =client.getDeliveryOrders();
        for (Reservation reservation:reservations) {
            DateTime reservationDate = reservation.getDate();
            DateTime currentDate = new DateTime();
            //Here is check is all the reservation has finished, or more specific if there is any reservation
            //for the future. If there is one or more reservations for the future, the client can not delete his
            //account.
            if(reservationDate.isAfter(currentDate)){
                return internalServerError("No se ha podido eliminar la cuenta debido a que tiene reservaciones pendientes.");
            }
        }
        //This deletes the user and all the all the orders and reservations.
        client.delete();
        session().clear();
        return ok();
    }
}
