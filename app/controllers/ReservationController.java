package controllers;

import authentication.SecuredClient;
import authentication.SecuredOwner;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import models.Response.ReservationResponse;
import models.requestObjects.ReservationObject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;
import javax.inject.Inject;
import play.api.i18n.*;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationController extends Controller{

    private MessagesApi messagesApi;

    @Inject
    public ReservationController(MessagesApi messagesApi){
        this.messagesApi = messagesApi;
    }

    @Security.Authenticated(SecuredClient.class)
    public Result view(){
        Messages messages = messagesApi.preferred(request());
        return ok(newReservation.render(messages));
    }
    @Security.Authenticated(SecuredClient.class)
    public Result viewClientReservations() {
        Messages messages = messagesApi.preferred(request());
        return ok(myReservations.render(messages));
    }
    @Security.Authenticated(SecuredOwner.class)
    public Result viewOwnerReservations() {
        Messages messages = messagesApi.preferred(request());
        return ok(ownerMyReservations.apply(messages));
    }

    public Result save(){
        final JsonNode body = request().body().asJson();
        final ReservationObject reservationObject = Json.fromJson(body, ReservationObject.class);

        final String email = session().get("email");
        final Client client = Client.byEmail(email);

        final Reservation reservation = reservationObject.toReservation(client);
        reservation.save();
        reservation.getLocal().getCuisines().forEach(cuisine -> {
            final CuisinePreference cuisinePreference = CuisinePreference.byClientCuisine(client.getId(), cuisine.getId());
            if (cuisinePreference == null){
                final CuisinePreference c = new CuisinePreference(cuisine.getId(), client.getId());
                c.save();
            } else {
                cuisinePreference.incrementAmount().update();
            }
        });
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
        final List<ReservationResponse> response = getOwnerReservationsList();
        return ok(Json.toJson(response));
    }

    public Result getOwnerFirsts() {
        final List<ReservationResponse> response = getOwnerReservationsList();
        return ok(Json.toJson(response.subList(0, 3)));
    }
    public Result getReservation(String rId){
        final Reservation reservation = Reservation.byId(Long.parseLong(rId));
        final ReservationResponse response = new ReservationResponse(reservation);
        if(reservation == null) return badRequest("Reservation does not exist");
        return ok(Json.toJson(response));
    }

    private List<ReservationResponse> getOwnerReservationsList() {
        final String email = session().get("email");
        final Owner owner = Owner.getOwnerbyEmail(email);

        final List<Local> locals = owner.getRestaurants().stream().filter(Restaurant::isLocal)
                .map(restaurant -> (Local) restaurant).collect(Collectors.toList());

        return locals.stream().map(Reservation::byLocal)
                .flatMap(Collection::stream)
                .map(ReservationResponse::new)
                .sorted((r1, r2) -> r1.date.isBefore(r2.date)? 1 : -1)
                .collect(Collectors.toList());
    }
}
