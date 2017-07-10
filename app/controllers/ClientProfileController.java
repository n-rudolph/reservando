package controllers;

import authentication.SecuredClient;
import models.*;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

import java.util.Random;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's clientProfile page.
 */
public class ClientProfileController extends Controller {

    @Security.Authenticated(SecuredClient.class)
    public Result clientProfile(){ return ok(clientProfile.render());}

    public Result getClient(){
        Client client = getCurrentClient();
        return ok(Json.toJson(client));
    }

    private Client getCurrentClient() { return Client.getClientByEmail(session().get("email")); }

    public Result deleteAccount(){
        Client client = getCurrentClient();
        client.setEmail(new Random().toString() + new Random().toString()+"@"+new Random().toString());
        client.setActive();
        client.update();
        Reservation.byClient(client).forEach(r -> {
            r.setActive(false);
            r.delete();
        });
        DeliveryOrder.getClientOrders(client).forEach(d -> {
            d.setActive(false);
            d.delete();
        });
        session().clear();
        return ok();
    }
}
