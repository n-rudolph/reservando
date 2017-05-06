var app = angular.module("reservandoApp", []);

app.requires.push('ui.materialize');

app.controller("OwnerHomeCtrl", function ($scope, $http) {

    $scope.restaurants = [];
    $scope.news = [];

    $scope.days = [
        {
            name: "Todos",
            value: "All"
        },{
            name: "Lunes",
            value: "Monday"
        },{
            name: "Martes",
            value: "Tuesday"
        },{
            name: "Miercoles",
            value: "Wednesday"
        },{
            name: "Jueves",
            value: "Thursday"
        },{
            name: "Viernes",
            value: "Friday"
        },{
            name: "Sabado",
            value: "Saturday"
        },{
            name: "Domingo",
            value: "Sunday"
        }
        ];
    $scope.cuisines = [
        {
            id: 0,
            name: "Asado"
        },{
            id: 1,
            name: "Pastas"
        },{
            id: 2,
            name: "China"
        },{
            id: 3,
            name: "Peruana"
        },{
            id: 4,
            name: "Comida rapida"
        }
    ];

    $scope.newRestaurant = {
    };

    $scope.nameError = false;

    $scope.getFirsts = function(){
        $http.get("/restaurants/firsts").then(function(response){
            $scope.restaurants = response.data.list;
            $scope.continues = response.data.continues;
        })
    };

    $scope.openAddRestaurant = function(){
        $('#addRestaurantModal').openModal();
    };

    $scope.addCuisine = function(){
        if ($scope.newRestaurant.cuisines.length >= 3){
            $scope.selectedCuisine = "";
            return;
        }
        var cuisineObject = JSON.parse($scope.selectedCuisine);
        for (var i = 0; i< $scope.newRestaurant.cuisines.length; i++){
            if ($scope.newRestaurant.cuisines[i].id == cuisineObject.id){
                $scope.selectedCuisine = "";
                return;
            }
        }
        $scope.newRestaurant.cuisines.push(cuisineObject);
        $scope.selectedCuisine = "";
    };

    $scope.removeCuisine = function(index){
        $scope.newRestaurant.cuisines.splice(index, 1);
    };

    $scope.addDay = function(){
        var dayObject = JSON.parse($scope.selectedDay);
        if (dayObject.name == "Todos"){
            $scope.newRestaurant.days = [];
        } else {
            for (var i = 0; i< $scope.newRestaurant.days.length; i++){
                if ($scope.newRestaurant.days[i].name == dayObject.name || $scope.newRestaurant.days[i].name == "Todos")
                    return;
            }
        }
        $scope.newRestaurant.days.push(dayObject);
        $scope.selectedDay = "";
    };

    $scope.removeDay = function(index){
        $scope.newRestaurant.days.splice(index, 1);
    };

    $scope.addRestaurant = function(){
        $scope.newRestaurant.address = $("#address").val();
        if ($scope.newRestaurant.name == undefined || $scope.newRestaurant.name == "")
            $scope.nameError = true;
        if ($scope.newRestaurant.address == undefined || $scope.newRestaurant.address == "")
            $scope.addressError = true;

        var data = {
            name: $scope.newRestaurant.name,
            address: $scope.newRestaurant.address,
            isLocal: $scope.newRestaurant.isLocal == undefined ? false : $scope.newRestaurant.isLocal
        };
        $http.post("/restaurant/add",  data).then($scope.successCallback(response), $scope.errorCallback(response))
    };

    $scope.successCallback = function(response){
        $scope.getFirsts();
    };

    $scope.errorCallback = function(response){
        Materialize.toast("Ha ocurrido un error", 2000, "red");
    };

    $scope.setAddress = function(address){
        $scope.newRestaurant.address = address;
    };

    $scope.openRestaurantProfile = function(restaurant){
        $http.post("/restaurant/view", restaurant).then(function(response){
            $scope.getFirsts();
        }, function(response){
            console.log(response);
        });
    };

    $(document).ready(function() {
        $('select').material_select();
        $scope.getFirsts();
    });
});