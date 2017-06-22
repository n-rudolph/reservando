package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Client;
import models.Reservation;
import models.requestObjects.ReservationObject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class ReservationController extends Controller{

    public Result view(){
        return ok(newReservation.render());
    }

    public Result save(){
        final JsonNode body = request().body().asJson();
        final ReservationObject reservationObject = Json.fromJson(body, ReservationObject.class);

        final String email = session().get("email");
        final Client client = Client.byEmail(email);

        final Reservation reservation = reservationObject.toReservation(client);
        reservation.save();
        return ok(Json.toJson(reservation));
    }

    public Result delete(String id){
        final long l = Long.parseLong(id);
        final Reservation reservation = Reservation.byId(l);
        reservation.delete();
        return ok("deleted successfully");
    }
}
