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
            // $scope.resetEditInfo();
            // $scope.resetErrors();
        } else {
            $scope.setEditModel();
        }
        this.editMode = enable;
    };

    $scope.setEditModel = function (){
        $scope.restaurantEdit.name = $scope.restaurant.name;
        $scope.restaurantEdit.address = $scope.restaurant.address;
        $scope.restaurantEdit.description = $scope.restaurant.description;
        $scope.restaurantEdit.isLocal = $scope.restaurant.isLocal;
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

});