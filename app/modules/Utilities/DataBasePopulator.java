package modules.Utilities;

import models.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataBasePopulator {


    //This method add defaults locals into the database.
    public void populateLocals(){
        loadAllDaysIntoDb();
        String relativePath = "modules/Utilities/Local List Pilar.txt";
        List<String> restaurants = readFile(relativePath);


        for (String restaurant : restaurants) {
            String[] localInfo = restaurant.split("-/");
            String name = localInfo[0];
            String address = localInfo[1];
            String description = localInfo[2];
            String[] cuisines = localInfo[3].split("-");
            List<Cuisine> cuisinesList = getCuisines(cuisines);
            String openingHour = "11:30 am";
            String closingHour = "12:30 pm";
            List<Day> openingDays = getDefaultOpeningDays();
            int capacityDefault = 100;
            List<Meal> meals = new ArrayList<>();

            Local newLocal = new Local(name, description, address, openingHour, closingHour, openingDays, cuisinesList, capacityDefault, meals);
            newLocal.save();
        }

    }

    //This method add defaults users into the database.
    public void populatePrimaryUsers(){
        Owner owner = new Owner("Juan", "Perez", "Av Peron 1500, Pilar, Buenos Aires, Argentina" ,"owner@gmail.com","reservando10", null, null);
        Client client = new Client("Pablo", "Torres", "Av Peron 1400, Pilar, Buenos Aires, Argentina" ,"client@gmail.com","reservando10", null, null);
        owner.save();
        client.save();
    }

    private List<String> readFile(String relativePath) {
        String textAux;
        ArrayList<String> list = new ArrayList<String>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(new File(relativePath).getAbsolutePath()));
            while ((textAux = br.readLine()) != null) list.add(textAux);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    private List<Day> getDefaultOpeningDays(){
        List<Day> openingDays = new ArrayList<>();

        openingDays.add(Day.getDay("Martes"));
        openingDays.add(Day.getDay("Miercoles"));
        openingDays.add(Day.getDay("Jueves"));
        openingDays.add(Day.getDay("Viernes"));
        openingDays.add(Day.getDay("Sabado"));
        openingDays.add(Day.getDay("Domingo"));

        return openingDays;
    }

    private void loadAllDaysIntoDb(){
        Day lunes = new Day("Lunes");
        Day martes = new Day("Martes");
        Day miercoles = new Day("Miercoles");
        Day jueves = new Day("Jueves");
        Day viernes = new Day("Viernes");
        Day sabado = new Day("Sabado");
        Day domingo = new Day("Domingo");

        lunes.save();
        martes.save();
        miercoles.save();
        jueves.save();
        viernes.save();
        sabado.save();
        domingo.save();

    }

    private List<Cuisine> getCuisines(String[] cuisines){
        List<Cuisine> cuisinesList = new ArrayList<>();

        for (String cuisine : cuisines) {
            if (Cuisine.getCuisine(cuisine) == null) {
                Cuisine newCuisine = new Cuisine();
                newCuisine.setName(cuisine);
                cuisinesList.add(newCuisine);
            } else cuisinesList.add(Cuisine.getCuisine(cuisine));
        }
        return cuisinesList;
    }

    private void loadDefaultCuisinesIntoDb(){
        Cuisine  cuisine1 = new Cuisine();
        Cuisine  cuisine2 = new Cuisine();
        Cuisine  cuisine3 = new Cuisine();
        Cuisine  cuisine4 = new Cuisine();
        Cuisine  cuisine5 = new Cuisine();
        Cuisine  cuisine6 = new Cuisine();
        Cuisine  cuisine7 = new Cuisine();
        Cuisine  cuisine8 = new Cuisine();

        String type1 = "Argentina";
        String type2 = "Japonesa";
        String type3 = "Italiana";
        String type4 = "Peruana";
        String type5 = "Varias";
        String type6 = "Norteamericana";
        String type7 = "Mexicana";
        String type8 = "Francesa";

        cuisine1.setName(type1);
        cuisine2.setName(type2);
        cuisine3.setName(type3);
        cuisine4.setName(type4);
        cuisine5.setName(type5);
        cuisine6.setName(type6);
        cuisine7.setName(type7);
        cuisine8.setName(type8);

        cuisine1.save();
        cuisine2.save();
        cuisine3.save();
        cuisine4.save();
        cuisine5.save();
        cuisine6.save();
        cuisine7.save();
        cuisine8.save();
    }


    
}
