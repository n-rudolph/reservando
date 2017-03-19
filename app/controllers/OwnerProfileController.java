package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import models.Owner;
import play.libs.Json;
import play.mvc.*;
import views.html.*;



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

    public Result changePassword(){
        final JsonNode jsonNode = request().body().asJson();
        String previousPassword = jsonNode.path("previousPassword").asText();
        String newPassword = jsonNode.path("newPassword").asText();

        Owner currentOwner = getCurrentOwner();
        if (currentOwner.getPassword().equals(previousPassword)){
            currentOwner.setPassword(newPassword).save();
            return ok(ownerProfile.render());
        }
        return badRequest("La contraseña no es correcta");
    }

    public Result changeBioInfo(){
        final JsonNode jsonNode = request().body().asJson();
        String firstName = jsonNode.path("firstName").asText();
        String lastName = jsonNode.path("lastName").asText();
        Owner owner = getCurrentOwner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.save();

        return ok();
    }

    public Result changeAddress(){
        final JsonNode jsonNode = request().body().asJson();
        String newAddress = jsonNode.get("address").asText();
        Owner owner = getCurrentOwner();
        owner.setAddress(newAddress);
        owner.save();

        return ok();
    }

    private Owner getCurrentOwner(){
        return Owner.getOwnerbyEmail(session().get("email"));
    }
}
