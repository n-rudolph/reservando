package modules;


import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Utils {

    public static JsonNode generateResponse(String status, String msg, Set<String> errors){
        Map<String, String> map = new HashMap<>();
        map.put("status", status);
        map.put("msg", msg);
        if (errors != null && !errors.isEmpty())
            map.put("errors", Json.toJson(errors).toString());
        return Json.toJson(map);
    }
}
