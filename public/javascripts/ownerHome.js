var app = angular.module("reservandoApp", []);

app.controller("OwnerHomeCtrl", function ($scope, $http) {

    $scope.restaurants = [];
    $scope.news = [];

    $scope.days = [
        {
            name: "Lunes",
            value: "1"
        },{
            name: "Martes",
            value: "2"
        },{
            name: "Miercoles",
            value: "3"
        },{
            name: "Jueves",
            value: "4"
        },{
            name: "Viernes",
            value: "5"
        },{
            name: "Sabado",
            value: "6"
        },{
            name: "Domingo",
            value: "7"
        }
        ];

    $scope.openAddRestaurant = function(){
        $('#addRestaurantModal').modal('open');
    };

    $(document).ready(function(){
        // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
        $('.modal').modal();
    });

});