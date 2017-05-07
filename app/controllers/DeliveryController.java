package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Day;
import models.Delivery;
import modules.Utils;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.rmi.CORBA.Util;
import java.util.*;

public class DeliveryController  extends Controller{

    public Result editDelivery(){
        final JsonNode body = request().body().asJson();
        //name, address, description, openingHour, closinghour, days, cuisines, radius, menu
        final long id = body.get("id").asLong();
        Set<String> errors = new TreeSet<>();
        final Delivery delivery = Delivery.byId(id);
        if (delivery == null)
            return badRequest(Utils.generateResponse("400", "Can't find delivery", null));
        if (isValidStringProperty(body, "name", errors)){
            delivery.setName(body.path("name").asText());
        }
        if (isValidStringProperty(body, "address", errors)){
            delivery.setAddress(body.path("address").asText());
        }
        if (isValidStringProperty(body, "description", errors)){
            delivery.setDescription(body.path("description").asText());
        }
        if (!body.path("radius").isMissingNode()){
            delivery.setRadius(body.path("radius").asDouble());
        }
        if (errors.isEmpty()) {
            delivery.update();
            return ok(Utils.generateResponse("200", "Update successful", null));
        }else{
            return badRequest(Utils.generateResponse("400", "Can't update model, errors in data", errors));
        }
    }

    public Result editWorkingHours(){
        final JsonNode body = request().body().asJson();
        if (body.path("id").isMissingNode())
            return badRequest(Utils.generateResponse("400", "Delivery id missing", null));
        final Delivery delivery = Delivery.byId(body.path("id").asLong());
        final JsonNode opening = body.path("opening");
        final JsonNode closing = body.path("closing");
        if (!opening.isMissingNode())
            delivery.setOpeningHour(opening.asText());
        if(!closing.isMissingNode())
            delivery.setClosingHour(closing.asText());
        return ok(Utils.generateResponse("200", "update successful", null));
    }



    private boolean isValidStringProperty(JsonNode body, String property, Set<String> errors){
        if (body.path(property).isMissingNode()) {
            errors.add(property);
            return false;
        }
        final String value = body.path(property).asText();
        if (value != null && value.trim().length() > 0)
            return true;
        errors.add(property);
        return false;
    }
}
