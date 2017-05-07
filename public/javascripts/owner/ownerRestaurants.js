var app = angular.module("reservandoApp", []);

app.controller("OwnerRestaurantCtrl", function ($scope, $http) {

    $scope.restaurants = [];

    $http.get('/owner/all').then(function(response){
        $scope.restaurants = response.data;
    })
});