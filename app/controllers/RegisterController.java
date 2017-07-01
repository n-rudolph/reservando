package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
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
}
