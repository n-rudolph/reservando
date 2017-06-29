var app = angular.module("reservandoApp");

app.controller("MyReservationsCtrl", function ($scope, $http, $window, $timeout) {

    $scope.reservations = [];

    $http.get("/reservation/client").then(function(response){
        $scope.reservations = response.data;
    });


    $scope.toRestaurant = function(reservation) {
        $window.location.href = '/client/restaurant?id='+reservation.local.id;
    };

    $scope.deleteReservation = function(reservation, index) {
        $http.delete("/reservation/"+reservation.id).then(function(response) {
            $scope.reservations.splice(index, 1);
            Materialize.toast("Reservación cancelada con éxito", 2000, "green");
        }, function(response){
            Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
        })
    };

    $scope.getDateTimeFormat = function(timeObject){

        var minute = timeObject.minuteOfHour;
        if (timeObject.minuteOfHour < 10){
            minute = "0"+minute;
        }
        return timeObject.dayOfMonth +"/"+ timeObject.monthOfYear +"/"+ timeObject.yearOfEra + "  " + timeObject.hourOfDay+":"+ minute;
    };

    $scope.getDate = function(dateObject) {
        return dateObject.dayOfMonth +"/"+ dateObject.monthOfYear +"/"+ dateObject.yearOfEra;
    };
    $scope.getTime = function (timeObject) {
        var minute = timeObject.minuteOfHour;
        if (timeObject.minuteOfHour < 10){
            minute = "0"+minute;
        }
        return timeObject.hourOfDay+":"+ minute;
    };

    $scope.isExpire = function(reservation){
        var date = new Date(Date.now());
        var placedDate = new Date(reservation.date.yearOfEra, reservation.date.monthOfYear -1, reservation.date.dayOfMonth, reservation.date.hourOfDay, reservation.date.minuteOfHour, 0, 0);
        return date >= placedDate;
    };

    $scope.qualify = function(reservationID){
        $window.location.href = "client/makeQualification?id=" + reservationID;
    }
});


