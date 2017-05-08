package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.libs.Json;
import play.mvc.*;
import views.html.*;
import org.joda.time.DateTime;

import java.io.*;
import java.util.Base64;
import java.util.List;


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
        List<Restaurant> restaurants = owner.getRestaurants();
        for (Restaurant restaurant: restaurants) {
            if(restaurant.isLocal()){
                List<Reservation> reservations = ((Local) restaurant).getReservations();
                for (Reservation reservation: reservations) {
                    DateTime reservationDate = reservation.getDate();
                    DateTime currentDate = new DateTime();
                    //Here is check is all the reservation has finished, or more specific if there is any reservation
                    //for the future. If there is one or more reservations for the future, the owner can not delete his
                    //account.
                    if(reservationDate.isAfter(currentDate)){
                        return internalServerError("No se ha podido eliminar la cuenta, debido a que tiene reservaciones pendientes.");
                    }
                }
            }
            //DeliveryOrder model must be change in order to implement some logic for the pending orders and the
            //finished ones.
           /* else {
                List<DeliveryOrder> deliveryOrders = ((Delivery) restaurant).getDeliveryOrders();
                for (DeliveryOrder deliveryOrder: deliveryOrders) {
                    //Here must goes the logic to know if a delivery order has been finished or not.
                }
            }*/
        }
        //This deletes the user and all the restaurants that he owns.
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
