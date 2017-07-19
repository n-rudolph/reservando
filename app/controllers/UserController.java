package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import models.Photo;
import models.User;
import models.requestObjects.ChangePasswordObject;
import models.requestObjects.PhotoObject;
import models.requestObjects.UserEditObject;
import modules.ImageUtils;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import play.i18n.*;

public class UserController extends Controller{

    private MessagesApi messagesApi;

    @Inject
    public UserController(MessagesApi messagesApi){
        this.messagesApi = messagesApi;
    }

    public Result changePhoto(){
        //I18N
        Messages messages = messagesApi.preferred(request());

        final JsonNode body = request().body().asJson();
        final PhotoObject photoObject = Json.fromJson(body, PhotoObject.class);

        final User user = User.getUserByEmail(session().get("email"));
        final Photo newPhoto = ImageUtils.saveImage(photoObject);
        if (newPhoto == null){
            String error = messages.at("user.server.response.an.error.occurred","");
            return badRequest(error);
        }
        final Photo oldPhoto = user.getPhoto();
        if (oldPhoto != null){
            if (!ImageUtils.deleteImage("./public/images/imgApp/" + oldPhoto.getName())) {
                String error = messages.at("user.server.response.an.error.occurred","");
                return badRequest(error);
            }
        }
        newPhoto.save();
        user.setPhoto(newPhoto);
        user.update();
        return ok(Json.toJson(newPhoto));
    }

    public Result changeInfo(){
        //I18N
        Messages messages = messagesApi.preferred(request());

        final JsonNode body = request().body().asJson();
        final UserEditObject userEditObject = Json.fromJson(body, UserEditObject.class);
        final User user = User.getUserByEmail(session().get("email"));

        user.setFirstName(userEditObject.firstName)
                .setLastName(userEditObject.lastName);
        if (!user.getAddress().getCompleteAddress().equals(userEditObject.address.completeAddress)){
            user.getAddress()
                    .setCompleteAddress(userEditObject.address.completeAddress)
                    .setLat(userEditObject.address.lat)
                    .setLng(userEditObject.address.lng);

            if (userEditObject.address.place != null)
                userEditObject.address.place.ifPresent(s -> user.getAddress().setPlace(s));
            if (userEditObject.address.city != null)
                userEditObject.address.city.ifPresent(s -> user.getAddress().setCity(s));
            if (userEditObject.address.state != null)
                userEditObject.address.state.ifPresent(s -> user.getAddress().setState(s));
            if (userEditObject.address.country != null)
                userEditObject.address.country.ifPresent(s -> user.getAddress().setCountry(s));
            if (userEditObject.address.district != null)
                userEditObject.address.district.ifPresent(s -> user.getAddress().setDistrict(s));
        }

        if (!userEditObject.email.equals(session().get("email"))){
            final User userByEmail = User.getUserByEmail(userEditObject.email);
            if (userByEmail != null){
                String emailInUse = messages.at("user.server.response.email.in.use", "");
                return badRequest(emailInUse);
                //return badRequest("email");
            }else{
                user.setEmail(userEditObject.email);
            }
        }

        user.update();
        return ok(Json.toJson(user));
    }

    public Result changePassword(){
        //I18N
        Messages messages = messagesApi.preferred(request());

        final JsonNode body = request().body().asJson();
        final ChangePasswordObject passwordObject = Json.fromJson(body, ChangePasswordObject.class);
        final User user = User.getUserByEmail(session().get("email"));

        if (!user.getPassword().equals(passwordObject.oldPassword)){
            String oldPasswordNotCorrect = messages.at("user.server.response.previous.password.not.valid","");
            return badRequest(oldPasswordNotCorrect);
            //return badRequest("oldPassword");
        }

        if (passwordObject.newPassword.equals(passwordObject.checkPassword)){
            user.setPassword(passwordObject.newPassword);
            user.update();
            String passwordChange = messages.at("user.server.response.password.change.successfully","");
            return ok(passwordChange);
            //return ok();
        } else{
            String passwordNotMatch = messages.at("user.server.response.new.passwords.not.match","");
            return badRequest(passwordNotMatch);
            //return badRequest("match");
        }

    }


}
