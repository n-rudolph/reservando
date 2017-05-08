package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import models.Owner;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

import java.io.*;
import java.util.Base64;


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

    public Result changeProfileImage(){
        final JsonNode jsonNode = request().body().asJson();
        String imageName = jsonNode.get("data").asText().split(",")[0];
        //String imageEncodedInfo = jsonNode.get("data").asText().split(",")[1];
        String imageEncodedBase64 = jsonNode.get("data").asText().split(",")[2];
        byte[] imageDecodedBytes = Base64.getDecoder().decode(imageEncodedBase64);
        Owner owner = getCurrentOwner();

        String basePath = new File("").getAbsolutePath(); //Base path is the absolute path where the current project is.
        String dirLocation = basePath + "/public/images/userProfileImages/" + owner.getEmail() + "/"; //Path where the image will be save.

        try {
            if (!new File(dirLocation).exists()){
                File file = new File(dirLocation);
                file.mkdirs();
            } else {
                cleanDir(new File(dirLocation)); //Cleans the directory, to not have more than one profile image per user.
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(dirLocation + new File(imageName)));
            bufferedOutputStream.write(imageDecodedBytes);
            bufferedOutputStream.close();
        } catch (IOException e){
            e.printStackTrace();
            return internalServerError();
        }

        //Photo photo = new Photo(dirLocation + new File(imageName));
        //owner.setPhoto(photo);
        owner.setPhotoPath("images/userProfileImages/" + owner.getEmail() + "/" + new File(imageName)); //The profile image will be in a dir named as the user email.
        owner.save();
        return ok();
    }

    private Owner getCurrentOwner(){
        return Owner.getOwnerbyEmail(session().get("email"));
    }

    public Result deleteAccount(){
        Owner owner = getCurrentOwner();
        //This deletes the user and all the restaurants that he owns.
        //It is important to decide what to do if the owner has pending orders or reservations!
        owner.delete();
        session().clear();
        return ok();
    }

    //This method deletes all the files inside a directory.
    private void cleanDir(File file){
        File [] content = file.listFiles();
        if (content != null){
            for (File f: content){
                cleanDir(f);
            }
            return;
        }
        file.delete();
    }
}
