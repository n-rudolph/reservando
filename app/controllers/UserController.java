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

public class UserController extends Controller{

    public Result changePhoto(){
        final JsonNode body = request().body().asJson();
        final PhotoObject photoObject = Json.fromJson(body, PhotoObject.class);

        final User user = User.getUserByEmail(session().get("email"));
        final Photo newPhoto = ImageUtils.saveImage(photoObject);
        if (newPhoto == null){
            return badRequest();
        }
        final Photo oldPhoto = user.getPhoto();
        if (oldPhoto != null){
            if (!ImageUtils.deleteImage("./public/images/imgApp/" + oldPhoto.getName())) {
                return badRequest();
            }
        }
        newPhoto.save();
        user.setPhoto(newPhoto);
        user.update();
        return ok(Json.toJson(newPhoto));
    }

    public Result changeInfo(){
        final JsonNode body = request().body().asJson();
        final UserEditObject userEditObject = Json.fromJson(body, UserEditObject.class);
        final User user = User.getUserByEmail(session().get("email"));

        user.setFirstName(userEditObject.firstName)
                .setLastName(userEditObject.lastName);
        user.getAddress()
                .setAddress(userEditObject.address.addressString)
                .setLat(userEditObject.address.lat)
                .setLng(userEditObject.address.lng);

        if (!userEditObject.email.equals(session().get("email"))){
            final User userByEmail = User.getUserByEmail(userEditObject.email);
            if (userByEmail != null){
                return badRequest("email");
            }else{
                user.setEmail(userEditObject.email);
            }
        }

        user.update();
        return ok(Json.toJson(user));
    }

    public Result changePassword(){
        final JsonNode body = request().body().asJson();
        final ChangePasswordObject passwordObject = Json.fromJson(body, ChangePasswordObject.class);
        final User user = User.getUserByEmail(session().get("email"));

        if (!user.getPassword().equals(passwordObject.oldPassword)){
            return badRequest("oldPassword");
        }

        if (passwordObject.newPassword.equals(passwordObject.checkPassword)){
            user.setPassword(passwordObject.newPassword);
            user.update();
            return ok();
        } else{
            return badRequest("match");
        }

    }
}
