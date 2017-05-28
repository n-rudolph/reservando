var app = angular.module("reservandoApp", []);

app.requires.push('ui.materialize');

app.directive('fileModel', ['$parse', function($parse){
    return{
        restrict: 'A',
        link: function (scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                })
            })
        }
    }
}]);

app.controller("NewRestaurantCtrl", function ($scope, $http) {

    $scope.days = [
        {
            name: "Lunes",
            value: "MONDAY"
        },{
            name: "Martes",
            value: "TUESDAY"
        },{
            name: "Miercoles",
            value: "WEDNESDAY"
        },{
            name: "Jueves",
            value: "THURSDAY"
        },{
            name: "Viernes",
            value: "FRIDAY"
        },{
            name: "Sabado",
            value: "SATURDAY"
        },{
            name: "Domingo",
            value: "SUNDAY"
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

    $scope.selectedDays = [];
    $scope.selectedCuisines = [];

    $('.dropify').dropify();

    $scope.reserRestaurant = function(){
        $scope.restaurant = {
            days: [],
            cuisines: []
        };
    };
    $scope.reserRestaurant();

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
            photo: false
        };
    };
    $scope.resetErrors();

    $scope.addCuisine = function(cuisine){
        $scope.restaurant.cuisines = [];
        for (var i = 0; i< $scope.cuisines.length; i++){
            if ($scope.cuisines[i].name == cuisine){
                $scope.restaurant.cuisines.push($scope.cuisines[i]);
                return;
            }
        }
    };

    $scope.addDay = function(day){
        $scope.restaurant.days = [];
        for(var i = 0; i < $scope.days.length; i++) {
            if ($scope.days[i].name == day){
                $scope.restaurant.days.push($scope.days[i].value);
                return;
            }
        }

    };

    $scope.submitRestaurant = function(){
        $scope.restaurant.address = $("#address").val();
        if ($scope.checkInfo()) {
            $http.post("/restaurant", $scope.restaurant).then($scope.successCallback(response), $scope.errorCallback(response));
        }
    };

    $scope.checkInfo = function(){
        var errors = 0;
        if (!$scope.restaurant.name || $scope.restaurant.name.length == 0){
            errors++;
            $scope.errors.name = true;
        }
        if (!$scope.restaurant.address || $scope.restaurant.address.length == 0){
            errors++;
            $scope.errors.address = true;
        }
        if ($scope.restaurant.isLocal){
            if ($scope.restaurant.capacity < 0){
                errors++;
                $scope.errors.capacity = true;
            }
        } else {
            $scope.restaurant.isLocal = false
            if ($scope.restaurant.radius < 0){
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
            for (var j = 0; j < $scope.selectedCuisines.length; j++) {
                $scope.addCuisine($scope.selectedCuisines[j]);
            }
        }
        return errors == 0;
    };

});