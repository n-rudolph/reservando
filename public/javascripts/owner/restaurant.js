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
                $scope.getMenu();
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
        $scope.restaurantEdit.address = {
            addressString: $scope.restaurant.address.address,
            lat: $scope.restaurant.address.lat,
            lng: $scope.restaurant.address.lng
        };
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
        $scope.restaurantEdit.address.addressString = $(".addressUnique").val();
        $scope.geocodeAddress();
    };

    $scope.restaurantSubmit = function(){
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

    $scope.geocodeAddress = function() {
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({'address': $scope.restaurantEdit.address.addressString}, function(results, status) {
            if (status === 'OK') {
                $scope.restaurantEdit.address.lat = results[0].geometry.location.lat();
                $scope.restaurantEdit.address.lng = results[0].geometry.location.lng();
                $scope.restaurantSubmit();
            } else {
                $scope.errors.address = true;
                $scope.checkInfo();
                Materialize.toast("La dirección no es valida", 2000, "red");
            }
        });
    };

    $scope.checkFields = function(){
        var errors = 0;
        if (!$scope.restaurantEdit.name || $scope.restaurantEdit.name.length == 0){
            errors++;
            $scope.errors.name = true;
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

    // Meal
    $scope.menu = [];
    $scope.mealToDeleteIndex = 0;

    $scope.getMenu = function(){
        $http.get("/menu/"+$scope.restaurant.id).then(function(response){
            $scope.menu = response.data;
        });
    };

    $scope.openDeleteMeal = function(index){
        $scope.mealToDeleteIndex = index;
        $("#deleteMealModal").openModal();
    };

    $scope.deleteMeal = function(){
        $http.delete("/meal/"+$scope.menu[$scope.mealToDeleteIndex].id).then(function(response){
            $scope.menu.splice($scope.mealToDeleteIndex, 1);
            $("#deleteMealModal").closeModal();
            Materialize.toast("Comida eliminada con éxito.", 2000, "green");
        }, function(response){
            Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
        });
    };

    // New Meal
    $scope.newMeal = {};
    $scope.newMealPhotos = [];
    $scope.resetNewMealErrors = function() {
        $scope.newMealErrors = {
            name: false,
            description: false,
            price: false,
            photo: false,
            photoSize: false
        }
    };

    $scope.openNewMealModal = function(){
        $scope.newMeal = {};
        $scope.newMealPhotos = [];
        $scope.resetNewMealErrors();
        $("#meal-image-input").val("");
        $('#newMealModal').openModal();
    };

    $scope.saveNewMeal = function(){
        $scope.resetNewMealErrors();
        if ($scope.checkNewMeal()){
            $scope.newMeal.photo = {
                name: $scope.newMealPhotos[0].name,
                src: $scope.newMealPhotos[0].src
            };
            $http.post("/meal/"+$scope.restaurant.id, $scope.newMeal).then(function(response){
                $scope.menu.push(response.data);
                Materialize.toast("Se ha guardado con exito", 2000, "green");
                $('#newMealModal').closeModal();
            }, function(response){
                Materialize.toast("Ha ocurrido un error. Intentelo mas tarde.", 2000, "red");
            });
        }else{
            Materialize.toast("Hay errores en los campos.", 2000, "red");
        }
    };

    $scope.checkNewMeal = function(){
        var errors = 0;
        if (!$scope.newMeal.name || $scope.newMeal.name.length == 0){
            errors++;
            $scope.newMealErrors.name = true;
        }
        if (!$scope.newMeal.description || $scope.newMeal.description.length == 0){
            errors++;
            $scope.newMealErrors.description = true;
        }
        if (!$scope.newMeal.price || $scope.newMeal.price < 0){
            errors++;
            $scope.newMealErrors.name = true;
        }
        if ($scope.newMealPhotos.length == 0){
            errors++;
            $scope.newMealErrors.photo = true;
        }else{
            if ($scope.newMealPhotos[0].size > 2000000){
                errors++;
                $scope.newMealErrors.photoSize = true;
            } else {
                $scope.newMeal.photo = $scope.newMealPhotos[0];
            }
        }
        return errors == 0;
    };

    //Edit Meal
    $scope.editMeal = {};
    $scope.editMealPhotos = [];
    $scope.resetEditMealErrors = function() {
        $scope.editMealErrors = {
            name: false,
            description: false,
            price: false,
            photoSize: false
        }
    };

    $scope.openEditMeal = function(meal, index){
        $scope.editMealPhotos = [];
        $scope.editMeal = {
            index: index,
            id: meal.id,
            name: meal.name,
            price: meal.price,
            description: meal.description,
            photo: meal.photo
        };
        $("#edit-meal-image-input").val("");
        $("#editMealModal").openModal();
    };

    $scope.saveEditMeal = function(){
        $scope.resetEditMealErrors();
        var putData = {};
        if ($scope.checkEditMeal(putData)){
            $http.put("/meal/"+ $scope.editMeal.id, putData).then(function(response){
                $scope.menu[$scope.editMeal.index] = response.data;
                Materialize.toast("Se ha editado la comida con éxito.", 2000, "green");
                $("#editMealModal").closeModal();
            }, function(response){
                Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
            });
        }else{
            Materialize.toast("Hay campos con errores", 2000, "red");
        }
    };

    $scope.checkEditMeal = function(putData){
        var errors = 0;
        if ($scope.editMeal.name.length == 0){
            errors++;
            $scope.editMealErrors.name = true;
        } else {
            putData.name = $scope.editMeal.name;
        }
        if ($scope.editMeal.description.length == 0){
            errors++;
            $scope.editMealErrors.description = true;
        } else {
            putData.description = $scope.editMeal.description;
        }
        if ($scope.editMeal.price < 0){
            errors++;
            $scope.editMealErrors.name = true;
        } else {
            putData.price = $scope.editMeal.price;
        }
        if ($scope.editMealPhotos.length != 0){
            if ($scope.editMealPhotos[0].size > 2000000){
                errors++;
                $scope.editMealErrors.photoSize = true;
            } else {
                putData.photo = $scope.editMealPhotos[0];
            }
        }
        return errors == 0;
    };



});