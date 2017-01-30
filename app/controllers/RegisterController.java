package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Client;
import models.Owner;
import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Collections;

public class RegisterController extends Controller{

    public Result registerUser(){
        final JsonNode jsonNode = request().body().asJson();
        if (validateRegisterInfo(jsonNode)){
            if (!uniqueEmail(jsonNode.path("email").asText()))
                return badRequest("El email ya se encuentra en uso");
            if (jsonNode.path("userType").isMissingNode() || !jsonNode.path("userType").asBoolean()){
                Client aClient = new Client(jsonNode.path("firstName").asText(),jsonNode.path("lastName").asText(),jsonNode.path("address").asText(), jsonNode.path("email").asText(), jsonNode.path("password").asText(), null, Collections.emptyList());
                aClient.save();
            }else{
                Owner aOwner = new Owner(jsonNode.path("firstName").asText(),jsonNode.path("lastName").asText(),jsonNode.path("address").asText(), jsonNode.path("email").asText(), jsonNode.path("password").asText(), null, Collections.emptyList());
                aOwner.save();
            }
            return ok();
        }
        return badRequest("Ha ocurrido un problema.");
    }

    private boolean uniqueEmail(String email) {
        return !User.isEmailInUse(email);
    }

    public Result getUsers(){
        return ok(Json.toJson(User.all()));
    }

    private boolean validateRegisterInfo(JsonNode jsonNode){
        return (hasValue(jsonNode, "firstName") && hasValue(jsonNode, "lastName")
                && hasValue(jsonNode, "password") && hasValue(jsonNode, "email")
                && hasValue(jsonNode, "address"));
    }

    private boolean hasValue(JsonNode node, String field){
        return !node.path(field).isMissingNode();
    }
}
