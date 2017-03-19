package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Client;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

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

    public Result changePassword(){
        final JsonNode jsonNode = request().body().asJson();
        String previousPassword = jsonNode.path("previousPassword").asText();
        String newPassword = jsonNode.path("newPassword").asText();

        Client client = getCurrentClient();
        if (client.getPassword().equals(previousPassword)){
            client.setPassword(newPassword).save();
            return ok(clientProfile.render());
        }
        return badRequest("La contraseña no es correcta");
    }

    public Result changeBioInfo(){
        final JsonNode jsonNode = request().body().asJson();
        String firstName = jsonNode.path("firstName").asText();
        String lastName = jsonNode.path("lastName").asText();
        Client client = getCurrentClient();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.save();

        return ok();
    }

    public Result changeAddress(){
        final JsonNode jsonNode = request().body().asJson();
        String newAddress = jsonNode.get("address").asText();
        Client client = getCurrentClient();
        client.setAddress(newAddress);
        client.save();

        return ok();
    }


    private Client getCurrentClient(){
        String email = session().get("email");
        return Client.getClientByEmail(email);
    }


}
