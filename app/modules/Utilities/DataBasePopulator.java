package modules.Utilities;

import models.*;
import org.joda.time.DateTime;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DataBasePopulator {


    //This method add defaults locals into the database.
    public void populateLocals(){
        if (Restaurant.allRestaurants().size() != 0)
            return;
        loadAllDaysIntoDb();
        //loadDefaultCuisinesIntoDb();
        String relativePath = "app/modules/Utilities/Local List Pilar.txt";
        List<String> restaurants = readFile(relativePath);

        for (String restaurant : restaurants) {
            String[] localInfo = restaurant.split("-/");
            String name = localInfo[0];
            String address = localInfo[1];
            String description = localInfo[2];
            String[] cuisines = localInfo[3].split("-");
            List<Cuisine> cuisinesList = getCuisines(cuisines);
            String openingHour = "11:30";
            String closingHour = "12:30";
            List<Day> openingDays = getDefaultOpeningDays();
            int capacityDefault = 100;
            List<Meal> meals = new ArrayList<>();

            Owner owner = (Owner) Owner.getUserByEmail("owner@gmail.com");

            Local newLocal = new Local(name, "Test Description it must be change (description too long)", address, openingHour, closingHour, openingDays, cuisinesList, capacityDefault, meals, owner);
            if(!Local.all().contains(newLocal)) newLocal.save();

            long localId = newLocal.getId();

            //Creates a reservation to test the owner delete account.
            Reservation testReservation = new Reservation();
            testReservation.setAmount(4);
            testReservation.setClient((Client) Client.getUserByEmail("client@gmail.com"));
            DateTime dateTime = new DateTime(2017,6,12,12,30);
            testReservation.setDate(dateTime);
            testReservation.setLocal(Local.getLocalById(localId));
            testReservation.save();
        }
    }

    //This method add defaults users into the database.
    public void populatePrimaryUsers(){
        if (Owner.getUserByEmail("owner@gmail.com") == null){
            Owner owner = new Owner("Juan", "Perez", new Address("Av Peron 1500, Pilar, Buenos Aires, Argentina", 43, 43) ,"owner@gmail.com","reservando10", null, null);
            owner.save();
        }
        if (Client.getUserByEmail("client@gmail.com") == null){
            Client client = new Client("Pablo", "Torres", new Address("Av Peron 1500, Pilar, Buenos Aires, Argentina", 43, 43),"client@gmail.com","reservando10", null, null);
            client.save();
        }
    }

    //This method reads a file and returns a list<String> of the file line's.
    private List<String> readFile(String relativePath) {
        ArrayList<String> list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(relativePath))){
            stream.forEach(list::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    //This method return a list with some defaults days.
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

    //This method creates all the days of a week and save them into the database.
    private void loadAllDaysIntoDb(){
        if (Day.getDay("Lunes") == null){
            Day lunes = new Day("Lunes");
            lunes.save();
        }
        if (Day.getDay("Martes") == null){
            Day martes = new Day("Martes");
            martes.save();
        }
        if(Day.getDay("Miercoles") == null){
            Day miercoles = new Day("Miercoles");
            miercoles.save();
        }
        if(Day.getDay("Jueves") == null){
            Day jueves = new Day("Jueves");
            jueves.save();
        }
        if(Day.getDay("Viernes") == null){
            Day viernes = new Day("Viernes");
            viernes.save();
        }
        if(Day.getDay("Sabado") == null){
            Day sabado = new Day("Sabado");
            sabado.save();
        }
        if(Day.getDay("Domingo") == null){
            Day domingo = new Day("Domingo");
            domingo.save();
        }
    }

    //This method creates and saves (if necessary) all the cuisines and return a list of them.
    private List<Cuisine> getCuisines(String[] cuisines){
        List<Cuisine> cuisinesList = new ArrayList<>();

        for (String cuisine : cuisines) {
            if (Cuisine.getCuisine(cuisine) == null) {
                Cuisine newCuisine = new Cuisine();
                newCuisine.setName(cuisine);
                newCuisine.save();
                cuisinesList.add(newCuisine);
            } else cuisinesList.add(Cuisine.getCuisine(cuisine));
        }
        return cuisinesList;
    }
}
