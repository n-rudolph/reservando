package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Client;
import models.Owner;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;


public class LoginController extends Controller{

    public Result login(){
        String email, password;
        final JsonNode jsonNode = request().body().asJson();
        if (jsonNode.path("email").isMissingNode())
            return badRequest("Ha occurido un problema con el email");
        if (jsonNode.path("password").isMissingNode())
            return badRequest("Ha occurido un problema con la contraseña");
        email = jsonNode.path("email").asText();
        password = jsonNode.path("password").asText();
        final User userByEmail = User.getUserByEmail(email);
        if (userByEmail == null)
            return badRequest("El email no es valido");
        if (!userByEmail.getPassword().equals(password))
            return badRequest("La contraseña no es correcta");

        session().clear();
        session().put("email", email);

        Client c = Client.getClientByEmail(email);
        if (c != null)
            return redirect(routes.HomeController.clientHome());

        return redirect(routes.HomeController.ownerHome());
    }

    public Result logout(){
        session().clear();
        return redirect(routes.HomeController.index());
    }
}