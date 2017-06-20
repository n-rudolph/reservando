package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Client;
import models.Local;
import models.Owner;
import models.User;
import models.requestObjects.UserRegisterObject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Collections;

public class RegisterController extends Controller {

    public Result registerUser() {
        final JsonNode jsonNode = request().body().asJson();
        final UserRegisterObject registerObject = Json.fromJson(jsonNode, UserRegisterObject.class);
        if (!uniqueEmail(registerObject.email))
            return badRequest("El email ya se encuentra en uso");
        if (!registerObject.userType) {
            Client aClient = registerObject.toClient();
            aClient.save();
        } else {
            Owner aOwner = registerObject.toOwner();
            aOwner.save();
        }
        session().clear();
        session().put("email", registerObject.email);

        if (!registerObject.userType)
            return ok("/client/home");
        return ok("/owner/home");
    }

    private boolean uniqueEmail(String email) {
        return !User.isEmailInUse(email);
    }

    public Result getUsers() {
        return ok(Json.toJson(User.all()));
    }

    public Result getLocals() {
        return ok(Json.toJson(Local.all()));
    }
}
