var app = angular.module("reservandoApp");

app.requires.push('ui.materialize');
app.requires.push('ngMap');

app.controller("RestaurantCtrl", function ($scope, $http, $window) {

    $scope.editMode = false;
    $scope.days = [];
    $scope.cuisines = [];
    $scope.selectedDays = [];
    $scope.selectedCuisines = [];
    $scope.restaurantEdit = {};

    $scope.photos = [];
    $scope.loading = false;

    $scope.getRestaurant = function(){
        var id = $window.location.href.split("id=")[1];
        $http.get("/restaurant/"+ id).then(
            function (response){
                $scope.restaurant = response.data;
            }
        );
    };
    $scope.getRestaurant();

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

    $scope.toggleEditMode = function (enable){
        if (!enable){
            $scope.resetErrors();
        } else {
            $scope.setEditModel();
        }
        $scope.editMode = enable;
    };

    $scope.setEditModel = function (){
        $scope.restaurantEdit.name = $scope.restaurant.name;
        $scope.restaurantEdit.address = $scope.restaurant.address;
        $scope.restaurantEdit.description = $scope.restaurant.description;
        $scope.restaurantEdit.isLocal = $scope.restaurant.local;
        $scope.restaurantEdit.radius = $scope.restaurant.radius;
        $scope.restaurantEdit.capacity = $scope.restaurant.capacity;
        $scope.initTime = $scope.restaurant.startTime;
        $scope.endTime = $scope.restaurant.endTime;

        for (var i = 0; i< $scope.restaurant.openingDays.length; i++){
            $scope.selectedDays.push($scope.restaurant.openingDays[i].day);
        }
        for (var j = 0; j< $scope.restaurant.cuisines.length; j++){
            $scope.selectedCuisines.push($scope.restaurant.cuisines[j].name);
        }
    };

    $scope.publicateRestaurant = function(state) {
        $http.post("/restaurant/state/"+$scope.restaurant.id, {state: state}).then(function(response) {
            $scope.restaurant.published = state;
        }, function(response){
            console.log(response);
        })
    };

    $scope.openDelete = function() {
        $('#deleteModal').openModal();
    };

    $scope.deleteRestaurant = function() {
        $http.delete("/restaurant/"+$scope.restaurant.id).then(function(response){
            Materialize.toast("Restaurant eliminado con exito", 2000, "green", $window.location.href = "/owner/home")
        }, function(response){
            Materialize.toast("Ha ocurrido un error", 2000, "red");
            $('#deleteModal').closeModal();
        });
    };

    $scope.saveImage = function(){
        if ($scope.photos[0].size < 2000000){
            $scope.loading = true;
            $http.put("/restaurant/"+$scope.restaurant.id+"/photo", {name: $scope.photos[0].name, src: $scope.photos[0].src})
                .then(function(response){
                    $scope.restaurant.photo = response.data;
                    $scope.photos = [];
                    $scope.loading = false;
                    Materialize.toast("Imagen modificada con éxito.", 2000, "green");
                }, function(){
                    $scope.loading = false;
                    Materialize.toast("Ha ocurrido un erros. Intentelo más tarde.", 2000, "red");
                })
        }else {
            $scope.photoError = true;
            $timeout(function(){
                $scope.photoError = false;
            }, 2000)
        }
    };

    $scope.updateRestaurant = function(){
        $scope.resetErrors();
        if ($scope.checkFields()){
            $http.put("/restaurant/"+$scope.restaurant.id, $scope.restaurantEdit).then(function(response){
                Materialize.toast("Restaurant modificado con éxito", 2000, "green");
                $scope.restaurant = response.data;
                $window.location.href = "#top";
                $scope.editMode = false;
            }, function(response){
                Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
            });
        }else{
            Materialize.toast("Hay campos con errores", 2000, "red");
            $window.location.href = "#top";
        }
    };

    $scope.checkFields = function(){
        var errors = 0;
        if (!$scope.restaurantEdit.name || $scope.restaurantEdit.name.length == 0){
            errors++;
            $scope.errors.name = true;
        }
        if (!$scope.restaurantEdit.address || $scope.restaurantEdit.address.length == 0){
            errors++;
            $scope.errors.address = true;
        }
        if ($scope.restaurantEdit.isLocal){
            if (!$scope.restaurantEdit.capacity || $scope.restaurantEdit.capacity < 0){
                errors++;
                $scope.errors.capacity = true;
            }
        } else {
            $scope.restaurantEdit.isLocal = false;
            if (!$scope.restaurantEdit.radius || $scope.restaurantEdit.radius < 0){
                errors++;
                $scope.errors.radius = true;
            }
        }
        if (!$scope.restaurantEdit.description || $scope.restaurantEdit.description.length == 0){
            errors++;
            $scope.errors.description = true;
        }
        if (!$scope.selectedDays || $scope.selectedDays.length == 0){
            errors++;
            $scope.errors.days = true;
        } else {
            $scope.restaurantEdit.days = [];
            for (var i = 0; i < $scope.selectedDays.length; i++) {
                $scope.addDay($scope.selectedDays[i]);
            }
        }
        if (!$scope.restaurantEdit.startTime || $scope.restaurantEdit.startTime.length == 0 || !$scope.restaurantEdit.endTime || $scope.restaurantEdit.endTime.length == 0 ){
            errors++;
            $scope.errors.time = true;
        } else {
            var splitStartTime = $scope.restaurantEdit.startTime.split(":");
            var splitEndTime = $scope.restaurantEdit.endTime.split(":");

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
            $scope.restaurantEdit.cuisines = [];
            for (var j = 0; j < $scope.selectedCuisines.length; j++) {
                $scope.addCuisine($scope.selectedCuisines[j]);
            }
        }
        return errors == 0;
    };

    $scope.addCuisine = function(cuisine){
        for (var i = 0; i< $scope.cuisines.length; i++){
            if ($scope.cuisines[i].name == cuisine){
                $scope.restaurantEdit.cuisines.push($scope.cuisines[i]);
                return;
            }
        }
    };

    $scope.addDay = function(day){
        for(var i = 0; i < $scope.days.length; i++) {
            if ($scope.days[i].day == day){
                $scope.restaurantEdit.days.push($scope.days[i]);
                return;
            }
        }

    };

});