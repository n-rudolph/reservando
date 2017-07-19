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
        if (Local.all().size() != 0) return;
        loadAllDaysIntoDb();
        String relativePath = "app/modules/Utilities/Local List Pilar.txt";
        List<String> restaurants = readFile(relativePath);

        for (String restaurant : restaurants) {
            String[] localInfo = restaurant.split("-/");
            String name = localInfo[0];
            String address = localInfo[1];
            String description = localInfo[2];
            String[] cuisines = localInfo[3].split("-");
            List<Cuisine> cuisinesList = getCuisines(cuisines);
            String[] mealsName = localInfo[4].split("-");
            List<Meal> mealsList = getMeals(mealsName);
            String [] latLngCoordinates = localInfo[6].split(":")[1].split(",");
            double lat = Double.parseDouble(latLngCoordinates[0]);
            double lgn = Double.parseDouble(latLngCoordinates[1]);
            String openingHour = "11:30";
            String closingHour = "12:30";
            List<Day> openingDays = getDefaultOpeningDays();
            int capacityDefault = 100;

            Owner owner = (Owner) Owner.getUserByEmail("owner@gmail.com");

            Local newLocal = new Local(name, description, new Address(address, lat, lgn), openingHour, closingHour, openingDays, cuisinesList, capacityDefault, mealsList, owner, 30);
            /*if(!Local.all().contains(newLocal)) newLocal.save();*/
            newLocal.save();
            newLocal.setPublished(true);
            newLocal.update();
            setRestaurantForMeals(mealsList, newLocal);
        }
    }

    //This method adds defaults deliveries into the database.
    public void populateDeliveries(){
        if (Delivery.all().size() != 0) return;
        String relativePath = "app/modules/Utilities/Delivery List Pilar.txt";
        List<String> restaurants = readFile(relativePath);

        for (String restaurant : restaurants) {
            String[] deliveryInfo = restaurant.split("-/");
            String name = deliveryInfo[0];
            String address = deliveryInfo[1];
            String description = deliveryInfo[2];
            String[] cuisines = deliveryInfo[3].split("-");
            List<Cuisine> cuisinesList = getCuisines(cuisines);
            String[] mealsName = deliveryInfo[4].split("-");
            List<Meal> mealsList = getMeals(mealsName);
            String[] latLngCoordinates = deliveryInfo[6].split(":")[1].split(",");
            double lat = Double.parseDouble(latLngCoordinates[0]);
            double lgn = Double.parseDouble(latLngCoordinates[1]);
            String openingHour = "11:30";
            String closingHour = "12:30";
            List<Day> openingDays = getDefaultOpeningDays();

            Owner owner = (Owner) Owner.getUserByEmail("owner@gmail.com");
            double defaultRadius = 10;
            int defaultResponseTime = 30;

            Delivery newDelivery = new Delivery(name, description, new Address(address, lat, lgn), openingHour, closingHour, openingDays, cuisinesList, defaultRadius, mealsList, owner, defaultResponseTime);
            /*if (!Delivery.all().contains(newDelivery)) newDelivery.save();*/
            newDelivery.save();
            newDelivery.setPublished(true);
            newDelivery.update();
            setRestaurantForMeals(mealsList, newDelivery);
        }
    }

    //This method adds defaults discount codes into the database.
    public void populateDiscountCodes() {
        if (Discount.all().size() == 0) {
            final Discount discount = new Discount("1q2w3e", 30);
            discount.setUsed();
            discount.save();
            final Discount discount2 = new Discount("q1w2e3", 20);
            discount2.save();
            final Discount discount3 = new Discount("123qwe", 50);
            discount3.save();
        }
    }

    //This method adds defaults users into the database.
    public void populatePrimaryUsers(){
        if (Owner.getUserByEmail("owner@gmail.com") == null){
            Owner owner = new Owner("Juan", "Perez", new Address("Av Peron 1500, Pilar, Buenos Aires, Argentina", -34.455880, -58.863923) ,"owner@gmail.com","reservando10", null, null);
            owner.save();
        }
        if (Client.getUserByEmail("client@gmail.com") == null){
            Client client = new Client("Pablo", "Torres", new Address("Mariano Acosta 1650, Pilar, Buenos Aires, Argentina", -34.454581, -58.859759),"client@gmail.com","reservando10", null, null);
            client.save();
        }
    }

    //This method adds some, expired and non expired, orders and reservation into the database.
    public void populateReservationsAndOrders(){
        if (DeliveryOrder.all().size() != 0) return;
        addExpiredOrders();
        addNonExpiredOrders();
        addExpiredReservations();
        addNonExpiredReservations();
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

    //This method returns some defaults meals (List<Meal>), when a an array of 'type of meal' is passed.
    private List<Meal> getMeals(String[] mealsName){
        List<Meal> mealsToReturn = new ArrayList<>();
        String relativePath = "app/modules/Utilities/Meal List.txt";
        List<String> mealsDefault = readFile(relativePath);

        for (String mealName : mealsName){
            for (String mealDefault : mealsDefault){
                String[] mealsDefaultFormat = mealDefault.split("-/");
                String mealDefaultName = mealsDefaultFormat[0];

                if(mealDefaultName.equals(mealName)){
                    String[] meal1Info = mealsDefaultFormat[1].split("-");
                    String meal1Name = meal1Info[0];
                    String meal1Description = meal1Info[1].equals("DESCRIPTION") ? "" : meal1Info[1];
                    String meal1Price = meal1Info[2];

                    Meal meal1 = new Meal(meal1Name, meal1Description, Double.parseDouble(meal1Price));
                    meal1.save();

                    mealsToReturn.add(meal1);

                    String[] meal2Info = mealsDefaultFormat[2].split("-");
                    String meal2Name = meal2Info[0];
                    String meal2Description = meal2Info[1].equals("DESCRIPTION") ? "" : meal2Info[1];
                    String meal2Price = meal2Info[2];

                    Meal meal2 = new Meal(meal2Name, meal2Description, Double.parseDouble(meal2Price));
                    meal2.save();

                    mealsToReturn.add(meal2);
                    break;
                }
            }
        }
        return mealsToReturn;
    }

    //This method sets a given Restaurant, to each Meal pass as a List.
    private void setRestaurantForMeals(List<Meal> meals, Restaurant restaurant){
        for (Meal meal : meals){
            meal.setRestaurant(restaurant);
            meal.save();
        }
    }

    //This method add some non expired reservations to the database.
    private void addNonExpiredReservations(){
        List<Local> locals = Local.all();
        
        for (Local local : locals){
            Reservation reservation = new Reservation();
            reservation.setAmount(4);
            reservation.setClient((Client) Client.getUserByEmail("client@gmail.com"));
            DateTime inAnHour = DateTime.now().plusHours(1);
            reservation.setDate(inAnHour);
            reservation.setLocal(Local.getLocalById(local.getId()));
            reservation.save();
        }
    }

    //This method add some expired reservations to the database.
    private void addExpiredReservations(){
        List<Local> locals = Local.all();

        for (Local local : locals){
            Reservation reservation = new Reservation();
            reservation.setAmount(4);
            reservation.setClient((Client) Client.getUserByEmail("client@gmail.com"));
            DateTime yesterday = DateTime.now().minusDays(1);
            reservation.setDate(yesterday);
            reservation.setLocal(Local.getLocalById(local.getId()));
            reservation.save();
        }
    }

    //This method add some non expired orders to the database.
    private void addNonExpiredOrders(){
        List<Delivery> deliveries = Delivery.all();

        for (Delivery delivery : deliveries){
            Client client = (Client) Client.getUserByEmail("client@gmail.com");
            String address = Client.getClientByEmail("client@gmail.com").getAddress().getCompleteAddress();

            List<MealOrder> meals = getSomeMealOrders(delivery, 3);
            DeliveryOrder order = new DeliveryOrder(client, delivery, meals, address, null);
            order.save();
        }
    }

    //This method add some expired orders to the database.
    private void addExpiredOrders(){
        List<Delivery> deliveries = Delivery.all();

        for (Delivery delivery : deliveries){
            Client client = (Client) Client.getUserByEmail("client@gmail.com");
            String address = Client.getClientByEmail("client@gmail.com").getAddress().getCompleteAddress();

            List<MealOrder> meals = getSomeMealOrders(delivery, 3);
            DeliveryOrder order = new DeliveryOrder(client, delivery, meals, address, null);
            order.setTimePlaced(DateTime.now().minusDays(1));
            order.save();
        }
    }

    //This method returns a list with an specific quantity of MealOrder from an specific delivery.
    private List<MealOrder> getSomeMealOrders(Delivery delivery, int quantity){
        List<Meal> meals = delivery.getMenu();
        List<MealOrder> mealOrders = new ArrayList<>();

        for (int i = 0; i < meals.size() && i < quantity; i++){
            MealOrder mealOrder = new MealOrder(meals.get(i).getId(),2);
            mealOrders.add(mealOrder);
        }

        return mealOrders;
    }

}
