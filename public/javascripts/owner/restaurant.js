var app = angular.module("reservandoApp");

app.requires.push('ngRateIt');
app.requires.push('ui.materialize');
app.requires.push('ngMap');
app.requires.push('vsGoogleAutocomplete');

app.controller("RestaurantCtrl", function ($scope, $http, $window, $timeout) {

    $scope.editMode = false;
    $scope.days = [];
    $scope.cuisines = [];
    $scope.days.selectedDays;
    $scope.cuisines.selectedCuisines;
    $scope.restaurantEdit = {};

    $scope.photos = [];
    $scope.loading = false;

    $scope.address = {};
    $scope.options = {
        componentRestrictions: { country: 'AR' }
    };

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

    $scope.getRestaurant = function(){
        var id = $window.location.href.split("id=")[1];
        $http.get("/restaurant/"+ id).then(
            function (response){
                $scope.restaurant = response.data;
                console.log(response.data);
                $scope.getMenu();
                $scope.getRestaurantQualification();
            }
        );
    };
    $scope.getRestaurant();

    // Qualification

    $scope.totalQualification = -1;

    $scope.getRestaurantQualification = function(){
        $http.get("/qualification/"+$scope.restaurant.id).then(function(response) {
            $scope.totalQualification = response.data;
        });
    };

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
            responseTime: false,
            minsBetweenTurns: false
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
        $scope.restaurantEdit.isLocal = $scope.restaurant.isLocal;
        $scope.restaurantEdit.radius = $scope.restaurant.radius;
        $scope.restaurantEdit.capacity = $scope.restaurant.capacity;
        $scope.restaurantEdit.responseTime = $scope.restaurant.responseTime;
        $scope.restaurantEdit.minsBetweenTurns = $scope.restaurant.minsBetweenTurns;
        $scope.restaurantEdit.cuisines = [];
        $scope.restaurantEdit.days = [];
        $scope.restaurantEdit.startTime = "";
        $scope.restaurantEdit.endTime = "";

        $scope.days.selectedDays = [];
        $scope.cuisines.selectedCuisines = [];
        for (var i = 0; i< $scope.restaurant.openingDays.length; i++){
            $scope.days.selectedDays.push($scope.restaurant.openingDays[i].day);
        }
        for (var j = 0; j< $scope.restaurant.cuisines.length; j++){
            $scope.cuisines.selectedCuisines.push($scope.restaurant.cuisines[j].name);
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
        $('#deleteModal').modal('open');
    };

    $scope.deleteRestaurant = function() {
        $http.delete("/restaurant/"+$scope.restaurant.id).then(function(response){
            Materialize.toast(response.data, 2000, "green", $window.location.href = "/owner/home")
            //Materialize.toast("Restaurant eliminado con exito", 2000, "green", $window.location.href = "/owner/home")
        }, function(response){
            var error = Messages("error.message.error.occurs.try.later");
            Materialize.toast(error, 2000, "red");
            //Materialize.toast("Ha ocurrido un error", 2000, "red");
            $('#deleteModal').modal('close');
            $('#deleteModal').modal('close');
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
                    var imageUpdate = Messages("success.message.image.modify");
                    Materialize.toast(imageUpdate, 2000, "green");
                }, function(responseError){
                    Materialize.toast(responseError.data, 2000,"red");
                })
        }else {
            var imageUpdate = Messages("error.modify.photo.size");
            Materialize.toast(imageUpdate, 2000, "red");
        }
    };

    $scope.restaurantSubmit = function(){
        if ($scope.checkFields()){
            $http.put("/restaurant/"+$scope.restaurant.id, $scope.restaurantEdit).then(function(response){
                var restaurantModify = Messages("success.message.restaurant.modify");
                Materialize.toast(restaurantModify, 2000, "green");
                //Materialize.toast("Restaurant modificado con éxito", 2000, "green");
                $scope.restaurant = response.data;
                $window.location.href = "#top";
                $scope.editMode = false;
            }, function(response){
                var error = Messages("error.message.error.occurs.try.later");
                Materialize.toast(error, 2000, "red");
                //Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
            });
        }else{
            var errorOnFields = Messages("error.message.there.are.error.on.fields");
            Materialize.toast(errorOnFields, 2000, "red");
            //Materialize.toast("Hay campos con errores", 2000, "red");
            $window.location.href = "#top";
        }
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
            if (!$scope.restaurantEdit.minsBetweenTurns || $scope.restaurantEdit.minsBetweenTurns < 0){
                errors++;
                $scope.errors.minsBetweenTurns = true;
            }
        } else {
            $scope.restaurantEdit.isLocal = false;
            if (!$scope.restaurantEdit.radius || $scope.restaurantEdit.radius < 0){
                errors++;
                $scope.errors.radius = true;
            }
            if (!$scope.restaurantEdit.responseTime || $scope.restaurantEdit.responseTime < 0){
                errors++;
                $scope.errors.responseTime = true;
            }
        }
        if (!$scope.restaurantEdit.description || $scope.restaurantEdit.description.length == 0){
            errors++;
            $scope.errors.description = true;
        }
        if (!$scope.days.selectedDays || $scope.days.selectedDays.length == 0){
            errors++;
            $scope.errors.days = true;
        } else {
            $scope.restaurantEdit.days = [];
            for (var i = 0; i < $scope.days.selectedDays.length; i++) {
                $scope.addDay($scope.days.selectedDays[i]);
            }
        }
        if (($scope.restaurantEdit.startTime && $scope.restaurantEdit.startTime.length != 0) && ($scope.restaurantEdit.endTime && $scope.restaurantEdit.endTime.length != 0)) {
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
        } else if ($scope.restaurantEdit.startTime && $scope.restaurantEdit.startTime.length != 0){
            splitStartTime = $scope.restaurantEdit.startTime.split(":");
            splitEndTime = $scope.restaurant.closingHour.split(":");

            if (Number(splitStartTime[0]) > Number(splitEndTime[0])){
                errors++;
                $scope.errors.time = false;
            }else if (Number(splitStartTime[0]) == Number(splitEndTime[0])){
                if (Number(splitStartTime[1]) >= Number(splitEndTime[1])){
                    errors++;
                    $scope.errors.time = false;
                } else {
                    $scope.restaurantEdit.endTime = $scope.restaurant.closingHour;
                }
            } else {
                $scope.restaurantEdit.endTime = $scope.restaurant.closingHour;
            }
        } else if ($scope.restaurantEdit.endTime && $scope.restaurantEdit.endTime.length >= 0){
            splitStartTime = $scope.restaurant.openingHour.split(":");
            splitEndTime = $scope.restaurantEdit.endTime.split(":");

            if (Number(splitStartTime[0]) > Number(splitEndTime[0])){
                errors++;
                $scope.errors.time = false;
            }else if (Number(splitStartTime[0]) == Number(splitEndTime[0])){
                if (Number(splitStartTime[1]) >= Number(splitEndTime[1])){
                    errors++;
                    $scope.errors.time = false;
                }  else {
                    $scope.restaurantEdit.startTime = $scope.restaurant.openingHour;
                }
            } else {
                $scope.restaurantEdit.startTime = $scope.restaurant.openingHour;
            }
        }  else {
            $scope.restaurantEdit.startTime = $scope.restaurant.openingHour;
            $scope.restaurantEdit.endTime = $scope.restaurant.closingHour;
        }
        if (!$scope.cuisines.selectedCuisines || $scope.cuisines.selectedCuisines.length == 0){
            errors++;
            $scope.errors.cuisines = true;
        } else {
            $scope.restaurantEdit.cuisines = [];
            for (var j = 0; j < $scope.cuisines.selectedCuisines.length; j++) {
                $scope.addCuisine($scope.cuisines.selectedCuisines[j]);
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
                if ($scope.restaurantEdit.days.indexOf($scope.days[i]) == -1)
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
        $("#deleteMealModal").modal('open');
    };

    $scope.deleteMeal = function(){
        $http.delete("/meal/"+$scope.menu[$scope.mealToDeleteIndex].id).then(function(response){
            $scope.menu.splice($scope.mealToDeleteIndex, 1);
            $("#deleteMealModal").modal('close');
            Materialize.toast(response.data, 2000, "green");
            //Materialize.toast("Comida eliminada con éxito.", 2000, "green");
        }, function(){
            var error = Messages("error.message.error.occurs.try.later");
            Materialize.toast(error, 2000, "red");
            //Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
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
        $('#newMealModal').modal('open');
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
                var mealSaved = Messages("success.messages.meal.saved");
                Materialize.toast(mealSaved, 2000, "green");
                //Materialize.toast("Se ha guardado con exito", 2000, "green");
                $('#newMealModal').modal('close');
            }, function(response){
                var error = Messages("error.message.error.occurs.try.later");
                Materialize.toast(error, 2000, "red");
                //Materialize.toast("Ha ocurrido un error. Intentelo mas tarde.", 2000, "red");
            });
        }else{
            var errorOnFields = Messages("error.message.there.are.error.on.fields");
            Materialize.toast(errorOnFields, 2000, "red");
            //Materialize.toast("Hay errores en los campos.", 2000, "red");
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
        $("#editMealModal").modal('open');
    };

    $scope.saveEditMeal = function(){
        $scope.resetEditMealErrors();
        var putData = {};
        if ($scope.checkEditMeal(putData)){
            $http.put("/meal/"+ $scope.editMeal.id, putData).then(function(response){
                $scope.menu[$scope.editMeal.index] = response.data;
                var mealEdited = Messages("success.messages.meal.updated");
                Materialize.toast(mealEdited, 2000, "green");
                //Materialize.toast("Se ha editado la comida con éxito.", 2000, "green");
                $("#editMealModal").modal('close');
            }, function(response){
                var error = Messages("error.message.error.occurs.try.later");
                Materialize.toast(error, 2000, "red");
                //Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
            });
        }else{
            var errorOnFields = Messages("error.message.there.are.error.on.fields");
            Materialize.toast(errorOnFields, 2000, "red");
            //Materialize.toast("Hay campos con errores", 2000, "red");
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

    $scope.getTranslateDay = function(day){
        var messageId = day.day;
        return Messages(messageId);
    }
});