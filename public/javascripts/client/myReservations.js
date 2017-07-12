var app = angular.module("reservandoApp");
app.requires.push('ui.materialize');
app.controller("MyReservationsCtrl", function ($scope, $http, $window, $timeout) {

    $scope.reservations = [];
    $scope.minIndex = 0;
    $scope.maxIndex = 8;

    $http.get("/reservation/client").then(function(response){
        $scope.reservations = response.data;
    });

    $scope.toRestaurant = function(reservation) {
        $window.location.href = '/client/restaurant?id='+reservation.local.id;
    };

    $scope.deleteReservation = function(reservation, index) {
        $http.delete("/reservation/"+reservation.id).then(function(response) {
            $scope.reservations.splice(index, 1);
            var reservationCancelled = Messages("success.message.reservation.cancelled");
            Materialize.toast(reservationCancelled, 2000, "green");
            //Materialize.toast("Reservación cancelada con éxito", 2000, "green");
        }, function(){
            var error = Messages("error.message.error.occurs.try.later");
            Materialize.toast(error, 2000, "red");
            //Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
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

    $scope.filterByPage = function(item, index) {
        return index >= $scope.minIndex && index <= $scope.maxIndex
    };

    $scope.changePage = function(page) {
        $scope.minIndex = (page-1) * 9;
        $scope.maxIndex = (page * 9) - 1;
    }
});


