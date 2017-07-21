var app = angular.module("reservandoApp");

app.requires.push('ngRateIt');
app.requires.push('ui.materialize');
app.requires.push('ngMap');

app.controller("RestaurantCtrl", function ($scope, $http, $window, $timeout) {

    $scope.getRestaurant = function(){
        var id = $window.location.href.split("id=")[1];
        $http.get("/restaurant/"+ id).then(
            function (response){
                $scope.restaurant = response.data;
                $scope.getMenu();
                $scope.getRestaurantQualification();
                $scope.getUserRestaurantQualification();
            }
        );
    };
    $scope.getRestaurant();

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

    $scope.totalQualification = -1;
    $scope.personalQualification = -1;

    $scope.getRestaurantQualification = function(){
        $http.get("/qualification/"+$scope.restaurant.id).then(function(response) {
            $scope.totalQualification = response.data;
        });
    };

    $scope.getUserRestaurantQualification = function(){
        $http.get("/qualification/"+$scope.restaurant.id).then(function(response) {
            $scope.personalQualification = response.data;
        });
    };

    $scope.qualifyRestaurant = function() {

        if ($scope.personalQualification != -1){
            var data = {
                rid: $scope.restaurant.id,
                qualification: $scope.personalQualification
            };
            $http.put("/qualification", data).then(function(response) {
                $scope.getRestaurantQualification();
                var qualificationSuccess = Messages("success.message.qualification.made");
                Materialize.toast(qualificationSuccess, 2000, "green");
                //Materialize.toast("Se ha calificado al restaurant con éxito.", 2000, "green");
            }, function(){
                $scope.getUserRestaurantQualification();
                var error = Messages("error.message.error.occurs.try.later")
                Materialize.toast(error, 2000, "red");
                //Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
            })
        }
    };

    // Order and Reservation

    $scope.openOrderPage = function(id){
        $window.location.href = "/order?id="+ id;
    };

    $scope.openReservationPage = function(id) {
        $window.location.href = "/reservation?id="+ id;
    };

    $scope.getTranslateDay = function(day){
        var messageId = day.day;
        return Messages(messageId);
    }

});