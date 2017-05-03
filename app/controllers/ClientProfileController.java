package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Client;
import models.Owner;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

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

    public Result changeProfileImage(){
        final JsonNode jsonNode = request().body().asJson();
        String imageName = jsonNode.get("data").asText().split(",")[0];
        //String imageEncodedInfo = jsonNode.get("data").asText().split(",")[1];
        String imageEncodedBase64 = jsonNode.get("data").asText().split(",")[2];
        byte[] imageDecodedBytes = Base64.getDecoder().decode(imageEncodedBase64);
        Client client = getCurrentClient();

        //IMPORTANT: this path must be change depending on the computer executed!
        String dirLocation = "/Users/Gustavo/Desktop/laboratorio/reservando/public/images/" + client.getEmail() + "/" ;

        //noinspection Duplicates
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
        }

        //Photo photo = new Photo(dirLocation + new File(imageName));
        //client.setPhoto(photo);
        client.setPhotoPath(client.getEmail() + "/" + new File(imageName)); //The profile image will be in a dir named as the user email.
        client.save();
        return ok();
    }

    private Client getCurrentClient() { return Client.getClientByEmail(session().get("email")); }

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
