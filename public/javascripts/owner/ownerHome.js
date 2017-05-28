var app = angular.module("reservandoApp", []);

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

    $scope.openRestaurantProfile = function(restaurant){
        $http.post("/restaurant/view", restaurant).then(function(response){
            $scope.getFirsts();
        }, function(response){
            console.log(response);
        });
    };

    $(document).ready(function() {
        $scope.getFirsts();
    });
});