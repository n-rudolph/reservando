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

    $(document).ready(function() {
        $scope.getFirsts();
    });
});