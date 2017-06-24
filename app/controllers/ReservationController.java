package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import models.Response.ReservationResponse;
import models.requestObjects.ReservationObject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationController extends Controller{

    public Result view(){
        return ok(newReservation.render());
    }
    public Result viewClientReservations() {
        return ok(myReservations.render());
    }
    public Result viewOwnerReservations() {
        return ok(ownerMyReservations.apply());
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

    public Result getClientReservations(){
        final String email = session().get("email");
        final Client client = Client.byEmail(email);

        return ok(Json.toJson(Reservation.byClient(client).stream()
                .map(ReservationResponse::new)
                .sorted((r1, r2) -> r1.date.isBefore(r2.date)? 1 : -1)
                .collect(Collectors.toList())));
    }

    public Result getOwnerReservations() {
        final String email = session().get("email");
        final Owner owner = Owner.getOwnerbyEmail(email);

        final List<Local> locals = owner.getRestaurants().stream().filter(Restaurant::isLocal)
                .map(restaurant -> (Local) restaurant).collect(Collectors.toList());

        final List<ReservationResponse> response = locals.stream().map(Reservation::byLocal)
                .flatMap(Collection::stream)
                .map(ReservationResponse::new)
                .sorted((r1, r2) -> r1.date.isBefore(r2.date)? 1 : -1)
                .collect(Collectors.toList());

        return ok(Json.toJson(response));
    }
}
