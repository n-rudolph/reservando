package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import models.Owner;
import models.User;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

import java.util.Objects;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's ownerProfile page .
 */
public class OwnerProfileController extends Controller {


    public Result ownerProfile(){ return ok(ownerProfile.render());}

    public Result getUser(){
        String email = session().get("email");
        Owner owner = Owner.getOwnerbyEmail(email);
        return ok(Json.toJson(owner));
    }

    public Result changePassword(){
        final JsonNode jsonNode = request().body().asJson();
        String email = jsonNode.path("email").asText();
        String previousPassword = jsonNode.path("previousPassword").asText();
        String newPassword = jsonNode.path("newPassword").asText();

        User currentUser = User.getUserByEmail(email);
        if (currentUser.getPassword().equals(previousPassword)){
            User.getUserByEmail(email).setPassword(newPassword).save();
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
