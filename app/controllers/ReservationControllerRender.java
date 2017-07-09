package controllers;

import play.api.i18n.Messages;
import play.api.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.myReservations;
import views.html.newReservation;
import views.html.ownerMyReservations;

import javax.inject.Inject;

public class ReservationControllerRender extends Controller {

    private MessagesApi messagesApi;

    @Inject
    public ReservationControllerRender(MessagesApi messagesApi){
        this.messagesApi = messagesApi;
    }

    public Result view(){
        Messages messages = messagesApi.preferred(request());
        return ok(newReservation.render(messages));
    }
    public Result viewClientReservations() {
        Messages messages = messagesApi.preferred(request());
        return ok(myReservations.render(messages));
    }
    public Result viewOwnerReservations() {
        Messages messages = messagesApi.preferred(request());
        return ok(ownerMyReservations.render(messages));
       /* return ok(ownerMyReservations.apply());*/
    }
}
