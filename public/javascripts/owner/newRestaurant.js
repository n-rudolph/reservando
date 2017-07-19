var app = angular.module("reservandoApp");

app.requires.push('ui.materialize');
app.requires.push('vsGoogleAutocomplete');

app.controller("NewRestaurantCtrl", function ($scope, $http, $window, $timeout) {

    $scope.days = [];
    $scope.cuisines = [];

    $scope.selectedDays = [];
    $scope.selectedCuisines = [];
    $scope.address = {};
    $scope.options = {
        componentRestrictions: { country: 'AR' }
    };

    $scope.photos = [];

    $scope.donebutton = Messages("clock-picker.button.done");
    $scope.clearTextbutton = Messages("clock-picker.button.clear");
    $scope.cancelTextButton = Messages("clock-picker.button.cancel");


    var internationalizeClockPicker = function(){
        $('.timepicker').pickatime({
            donetext: $scope.donebutton,
            cleartext: $scope.clearTextbutton,
            canceltext: $scope.cancelTextButton
        });
    };
    internationalizeClockPicker();

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

    $scope.resetRestaurant = function(){
        $scope.restaurant = {
            days: [],
            cuisines: [],
            address: {}
        };
    };
    $scope.resetRestaurant();

    $scope.resetErrors = function() {
        $scope.errors = {
            name: false,
            address: false,
            radius: false,
            capacity: false,
            description: false,
            days: false,
            time: false,
            cuisines: false,
            photo: false,
            photoSize: false,
            responseTime: false
        };
    };
    $scope.resetErrors();

    $scope.addCuisine = function(cuisine){
        for (var i = 0; i< $scope.cuisines.length; i++){
            if ($scope.cuisines[i].name == cuisine){
                $scope.restaurant.cuisines.push($scope.cuisines[i]);
                return;
            }
        }
    };

    $scope.addDay = function(day){
        for(var i = 0; i < $scope.days.length; i++) {
            if ($scope.days[i].day == day){
                $scope.restaurant.days.push($scope.days[i]);
                return;
            }
        }

    };

    $scope.restaurantSubmit = function(){
        if ($scope.checkInfo()) {
            $http.post("/restaurant", $scope.restaurant).then($scope.successCallback, $scope.errorCallback);
        } else{
            $window.location.href = "#top";
        }
    };

    $scope.checkInfo = function(){
        var errors = 0;
        if (!$scope.restaurant.name || $scope.restaurant.name.length == 0){
            errors++;
            $scope.errors.name = true;
        }
        if ($scope.restaurant.isLocal){
            if (!$scope.restaurant.capacity || $scope.restaurant.capacity < 0){
                errors++;
                $scope.errors.capacity = true;
            }
        } else {
            $scope.restaurant.isLocal = false;
            if (!$scope.restaurant.radius || $scope.restaurant.radius < 0){
                errors++;
                $scope.errors.radius = true;
            }
            if (!$scope.restaurant.responseTime){
                errors++;
                $scope.errors.responseTime = true;
            }
        }
        if (!$scope.restaurant.description || $scope.restaurant.description.length == 0){
            errors++;
            $scope.errors.description = true;
        }
        if (!$scope.selectedDays || $scope.selectedDays.length == 0){
            errors++;
            $scope.errors.days = true;
        } else {
            $scope.restaurant.days = [];
            for (var i = 0; i < $scope.selectedDays.length; i++) {
                $scope.addDay($scope.selectedDays[i]);
            }
        }
        if (!$scope.restaurant.startTime || $scope.restaurant.startTime.length == 0 || !$scope.restaurant.endTime || $scope.restaurant.endTime.length == 0 ){
            errors++;
            $scope.errors.time = true;
        } else {
            var splitStartTime = $scope.restaurant.startTime.split(":");
            var splitEndTime = $scope.restaurant.endTime.split(":");

            if (Number(splitStartTime[0]) > Number(splitEndTime[0])){
                errors++;
                $scope.errors.time = false;
            }else if (Number(splitStartTime[0]) == Number(splitEndTime[0])){
                if (Number(splitStartTime[1]) >= Number(splitEndTime[1])){
                    errors++;
                    $scope.errors.time = false;
                }
            }
        }
        if (!$scope.selectedCuisines || $scope.selectedCuisines.length == 0){
            errors++;
            $scope.errors.cuisines = true;
        } else {
            $scope.restaurant.cuisines = [];
            for (var j = 0; j < $scope.selectedCuisines.length; j++) {
                $scope.addCuisine($scope.selectedCuisines[j]);
            }
        }

        if ($scope.photos.length == 0){
            errors++;
            $scope.errors.photo = true;
        }else{
            if ($scope.photos[0].size > 2000000){
                errors++;
                $scope.errors.photoSize = true;
            } else {
                $scope.restaurant.photo = $scope.photos[0];
            }
        }
        return errors == 0;
    };

    $scope.successCallback = function(response) {
        var restaurantCreated = Messages("success.message.restaurant.created");
        Materialize.toast(restaurantCreated, 2000, "green");
        //Materialize.toast("Se ha creado el restaurant con éxito", 2000, "green");
        $window.location.href = "#top";
        $timeout(function(){
            $window.location.href = "/owner/home";
        }, 500);$window.location.href = "#top";
    };

    $scope.errorCallback = function(response) {
        $window.location.href = "#top";
        var error = Messages("error.message.error.occurs.try.later")
        Materialize.toast(error, 2000, "red");
        //Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
    };

});