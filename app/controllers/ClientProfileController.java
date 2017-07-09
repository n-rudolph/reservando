package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import org.joda.time.DateTime;
import play.libs.Json;
import play.mvc.*;
import views.html.*;
import javax.inject.Inject;
import play.api.i18n.*;

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

    private MessagesApi messagesApi;

    @Inject
    public ClientProfileController(MessagesApi messagesApi){
        this.messagesApi = messagesApi;
    }

    public Result clientProfile(){
        Messages messages = messagesApi.preferred(request());
        return ok(clientProfile.render(messages));
    }

    public Result getClient(){
        Client client = getCurrentClient();
        return ok(Json.toJson(client));
    }

    private Client getCurrentClient() { return Client.getClientByEmail(session().get("email")); }

    public Result deleteAccount(){
        //I18N
        Messages messages = messagesApi.preferred(request());

        Client client = getCurrentClient();
        List<Reservation> reservations = client.getReservations();
        List<DeliveryOrder> deliveryOrders = DeliveryOrder.getClientOrders(client);
        for (Reservation reservation:reservations) {
            DateTime reservationDate = new DateTime(reservation.getDate());
            DateTime currentDate = new DateTime();
            //Here is check is all the reservation has finished, or more specific if there is any reservation
            //for the future. If there is one or more reservations for the future, the client can not delete his
            //account.
            if(reservationDate.isAfter(currentDate)){
                //String error = messages.at("client.profile.server.response.not.available.to.delete.account.pending.reservations","");
                //return badRequest(error);
                return internalServerError("No se ha podido eliminar la cuenta debido a que tiene reservaciones pendientes.");
            }
        }

        for (DeliveryOrder deliveryOrder: deliveryOrders){
            DateTime orderDatePlaced = deliveryOrder.getTimePlaced();
            DateTime orderDateFinished = orderDatePlaced.plusMinutes(deliveryOrder.getDelivery().getResponseTime());
            DateTime currentDate = new DateTime();
            //Here is check is all the orders has finished, or more specific if there is any order
            //for the future. If there is one or more order for the future, the client can not delete his
            //account.
            if(orderDateFinished.isAfter(currentDate)){
                //String error = messages.at("client.profile.server.response.not.available.to.delete.account.pending.orders","");
                //return badRequest(error);
                return internalServerError("No se ha podido eliminar la cuenta debido a que tiene ordenes pendientes.");
            }
        }

        //This deletes the user and all the all the orders and reservations.
        client.delete();
        session().clear();
        return ok();
    }
}
