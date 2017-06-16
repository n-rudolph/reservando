var app = angular.module("reservandoApp");

app.requires.push('ui.materialize');

app.controller("NewRestaurantCtrl", function ($scope, $http, $window, $timeout) {

    $scope.days = [];
    $scope.cuisines = [];

    $scope.selectedDays = [];
    $scope.selectedCuisines = [];

    $scope.photos = [];

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
            photoSize: false
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

    $scope.submitRestaurant = function(){
        $scope.restaurant.address.addressString = $("#address").val();
        $scope.resetErrors();
        $scope.geocodeAddress();
    };

    $scope.restaurantSubmit = function(){
        if ($scope.checkInfo()) {
            $http.post("/restaurant", $scope.restaurant).then($scope.successCallback, $scope.errorCallback);
        } else{
            $window.location.href = "#top";
        }
    };

    $scope.geocodeAddress = function() {
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({'address': $scope.restaurant.address.addressString}, function(results, status) {
            if (status === 'OK') {
                $scope.restaurant.address.lat = results[0].geometry.location.lat();
                $scope.restaurant.address.lng = results[0].geometry.location.lng();
                $scope.restaurantSubmit();
            } else {
                $scope.errors.address = true;
                $scope.checkInfo();
                Materialize.toast("La dirección no es valida", 2000, "red");
            }
        });
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
        Materialize.toast("Se ha creado el restaurant con éxito", 2000, "green");
        $window.location.href = "#top";
        $timeout(function(){
            $window.location.href = "/owner/home";
        }, 1000);$window.location.href = "#top";
    };

    $scope.errorCallback = function(response) {
        $window.location.href = "#top";
        Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
    };

});