var app = angular.module("reservandoApp");

app.requires.push('ui.materialize');

app.controller("OwnerHomeCtrl", function ($scope, $http, $window) {

    $scope.restaurants = [];
    $scope.news = [];
    $scope.reservations = [];
    $scope.orders = [];

    $scope.getFirsts = function(){
        $http.get("/restaurants/firsts").then(function(response){
            $scope.restaurants = response.data.list;
            $scope.continues = response.data.continues;
        })
    };
    $scope.getFirstReservations = function() {
        $http.get("/reservation/owner/firsts").then(function(response){
            $scope.reservations = response.data;
            console.log(response.data);
        })
    };
    $scope.getFirstOrders = function() {
        $http.get("/orders/owner/firsts").then(function(response){
            $scope.orders = response.data;
            console.log(response.data);
        })
    };

    $scope.openAddRestaurant = function(){
        $window.location.href = "/owner/new-restaurant";
    };

    $scope.openRestaurants = function(){
        $window.location.href = "/owner/restaurants";
    };
    $scope.openOrders = function(){
        $window.location.href = "/owner/my-orders";
    };
    $scope.openReservations = function(){
        $window.location.href = "/owner/my-reservations";
    };

    $scope.openRestaurantProfile = function(restaurant){
        $window.location.href = "/owner/restaurant?id=" + restaurant.id;
    };

    $scope.publicateRestaurant = function(restaurant, state) {
        $http.post("/restaurant/state/"+restaurant.id, {state: state}).then(function(response) {
            restaurant.published = state;
            Materialize.toast(response.data, 2000, "green");
            /*if (state){
                Materialize.toast("El restaurant ha sido publicado", 2000, "green");
            }else{
                Materialize.toast("El restaurant se ha despublicado", 2000, "green");
            }*/
        }, function(){
            var error = Messages("error.message.error.occurs.try.later");
            Materialize.toast(error, 2000, "red");
            //Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
        })
    };

    $scope.getDateTimeFormat = function(timeObject){

        var minute = timeObject.minuteOfHour;
        if (timeObject.minuteOfHour < 10){
            minute = "0"+minute;
        }
        return timeObject.dayOfMonth +"/"+ timeObject.monthOfYear +"/"+ timeObject.yearOfEra + "  " + timeObject.hourOfDay+":"+ minute;
    };

    $(document).ready(function() {
        $scope.getFirsts();
        $scope.getFirstReservations();
        $scope.getFirstOrders();
    });
});