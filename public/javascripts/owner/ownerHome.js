var app = angular.module("reservandoApp");

app.requires.push('ui.materialize');

app.controller("OwnerHomeCtrl", function ($scope, $http, $window) {

    $scope.restaurants = [];
    $scope.news = [];

    $scope.getFirsts = function(){
        $http.get("/restaurants/firsts").then(function(response){
            $scope.restaurants = response.data.list;
            $scope.continues = response.data.continues;
        })
    };

    $scope.openAddRestaurant = function(){
        $window.location.href = "/owner/new-restaurant";
    };

    $scope.openRestaurants = function(){
        $window.location.href = "/owner/restaurants";
    };

    $scope.openRestaurantProfile = function(restaurant){
        $window.location.href = "/owner/restaurant?id=" + restaurant.id;
    };

    $scope.publicateRestaurant = function(restaurant, state) {
        $http.post("/restaurant/state/"+restaurant.id, {state: state}).then(function(response) {
            restaurant.published = state;
            if (state){
                Materialize.toast("El restaurant ha sido publicado", 2000, "green");
            }else{
                Materialize.toast("El restaurant se ha despublicado", 2000, "green");
            }
        }, function(response){
            Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
        })
    };

    $(document).ready(function() {
        $scope.getFirsts();
    });
});