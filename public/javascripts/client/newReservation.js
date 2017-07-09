var app = angular.module("reservandoApp");
app.requires.push('ngMap');
app.requires.push('ui.materialize');

app.controller("NewReservationCtrl", function ($scope, $http, $window, $timeout) {

    $scope.reservation = {};
    $scope.hasDiscount = false;
    $scope.discCode = "";
    $scope.discount = {};
    $scope.coordinates = {};

    $scope.month = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    $scope.monthShort = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
    $scope.weekdaysFull = ['Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'];
    $scope.weekdaysLetter = ['D', 'L', 'M', 'M', 'J', 'V', 'S'];
    $scope.disable = [true];

    $scope.getRestaurant = function(){
        var id = $window.location.href.split("id=")[1];
        $http.get("/restaurant/"+ id).then(
            function (response){
                $scope.restaurant = response.data;
                var disable =[true];
                for(var i = 0; i < $scope.restaurant.openingDays.length; i++) {
                    if ($scope.restaurant.openingDays[i].id == 7){
                        disable.push(1);
                    }else{
                        disable.push($scope.restaurant.openingDays[i].id + 1)
                    }
                }
                $scope.disable = disable;
                $scope.defineTurns();
                $scope.coordinates.lat = $scope.restaurant.address.lat;
                $scope.coordinates.lng = $scope.restaurant.address.lng;
                $scope.getMenu();
            }
        );
    };
    $scope.getRestaurant();


    // Turnos

    $scope.defineTurns = function(){
        $scope.turns = [];
        var currentHour = Number($scope.restaurant.openingHour.split(":")[0]);
        var currentMin = Number($scope.restaurant.openingHour.split(":")[1]);

        var closingHour = Number($scope.restaurant.closingHour.split(":")[0]);
        var closingMin = Number($scope.restaurant.closingHour.split(":")[1]);

        var minsBetweenTurns = $scope.restaurant.minsBetweenTurns;

        while(!$scope.checkIfFinishTurn(currentHour, currentMin, closingHour, closingMin)){
            var hour = ""+ currentHour;
            if (currentHour < 10)
                hour = "0"+ hour;
            var minutes = ""+ currentMin;
            if (currentMin < 10)
                minutes = "0"+ minutes;
            $scope.turns.push(hour+":"+minutes);

            currentMin += minsBetweenTurns;
            if (currentMin >= 60){
                currentHour++;
                if (currentHour>=24)
                    currentHour = 0;
                currentMin-=60;
            }
        }
    };

    $scope.checkIfFinishTurn = function(currentHour, currentMin, closingHour, closingMin){
        if (currentHour > closingHour)
            return true;
        else if(currentHour < closingHour){
            return false;
        } else {
            return currentMin >= closingMin;
        }
    };

    $scope.selectTurn = function(index){
        if($scope.selectedTurnIndex == index)
            $scope.selectedTurnIndex = undefined;
        else {
            $scope.selectedTurnIndex = index;
        }
        $scope.checkComplete();
    };

    // Meal

    $scope.getMenu = function(){
        $http.get("/menu/"+$scope.restaurant.id).then(function(response){
            $scope.menu = response.data;
        });
    };

    $scope.checkCode = function(){
        $scope.invalidCode = false;
        $scope.validCode = false;
        $http.get("/discount/"+$scope.discCode).then(function(response){
            $scope.discount = response.data;
            $scope.validCode = true;
            /*if (response.data === "1" || response.data === "2"){
                Materialize.toast("El codigo no es valido", 2000, "red");
                $scope.invalidCode = true;
                $scope.discount = {};
            } else {
                $scope.discount = response.data;
                $scope.validCode = true;
            }*/
        }, function(responseError){
            Materialize.toast(responseError.data, 2000, "red");
            $scope.invalidCode = true;
            $scope.discount = {};
        });
    };
    $scope.resetCode = function(){
        $scope.invalidCode = false;
        $scope.validCode = false;
        $scope.discount = {};
    };

    $scope.saveReservation = function(){
        if ($scope.validCode && $scope.hasDiscount)
            $scope.reservation.discountCode = $scope.discCode;
        else $scope.reservation.discountCode = "";
        $scope.reservation.localId = $scope.restaurant.id;
        $scope.reservation.turn = $scope.turns[$scope.selectedTurnIndex];

        var dateSplit = $scope.reservation.date2.split(" ");
        var timeSplit = $scope.reservation.turn.split(":");
        var month = dateSplit[1].substring(0, dateSplit[1].length -1);
        var date = new Date(Number(dateSplit[2]), $scope.month.indexOf(month) + 1, Number(dateSplit[0]), Number(timeSplit[0]), Number(timeSplit[1]), 0, 0);
        $scope.reservation.date = date.getTime();

        $http.post("/reservation", $scope.reservation).then(function(response){
            Materialize.toast("La reserva se ha procesado con éxito.", 2000, "green");
            $timeout(function(){
                $window.location.href = "/client/restaurant?id="+$scope.restaurant.id;
            }, 1000);
        }, function(response){
            //Materialize.toast("Ha ocurrido un error. Intentelo más tarde", 2000, "red");
        });
    };

    $scope.checkComplete = function(){
        if ($scope.reservation.date2 == undefined || $scope.reservation.date2.length == 0) {
            $scope.reservationComplete = false;
            return;
        }
        if ($scope.reservation.amount == undefined || $scope.reservation.amount < 1 || $scope.reservation.amount > $scope.restaurant.capacity){
            $scope.reservationComplete = false;
            return;
        }
        $scope.reservationComplete = !($scope.selectedTurnIndex == undefined);
    }

});


