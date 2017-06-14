var app = angular.module("reservandoApp");

app.requires.push('ui.materialize');
app.requires.push('ngMap');

app.controller("RestaurantCtrl", function ($scope, $http, $window) {

    $scope.days = [];
    $scope.cuisines = [];

    $scope.getRestaurant = function(){
        var id = $window.location.href.split("id=")[1];
        $http.get("/restaurant/"+ id).then(
            function (response){
                $scope.restaurant = response.data;
                $scope.getMenu();
            }
        );
    };
    $scope.getRestaurant();

    $http.get("/all/days").then(
        function(response) {
            $scope.days = response.data;
        }
    );
    $http.get("/all/cuisines").then(
        function(response) {
            $scope.cuisines = response.data;
        }
    );

    $scope.showCuisines = function(){
        var list = $scope.restaurant.cuisines;
        var result = "";
        var token = "";
        for (var i = 0; i < list.length(); i++){
            result += token;
            result += list[i].name;
            token = ", ";
        }
        return result;
    };

    // Meal
    $scope.menu = [];

    $scope.getMenu = function(){
        $http.get("/menu/"+$scope.restaurant.id).then(function(response){
            $scope.menu = response.data;
        });
    };

    // Qualification

    $scope.firstRate = 3;
    $scope.totalQualification = 3;
    $scope.personalQualification = 0;

    $scope.getRestaurantQualification = function(){
        $http.get().then(function(response) {
            $scope.totalQualification = response.data;
        });
    };

    $scope.getUserRestaurantQualification = function(){
        $http.get().then(function(response) {
            $scope.totalQualification = response.data;
        });
    };

});