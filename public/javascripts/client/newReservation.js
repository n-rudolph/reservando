var app = angular.module("reservandoApp");
app.requires.push('ngMap');
app.requires.push('ui.materialize');

app.controller("NewReservationCtrl", function ($scope, $http, $window, $timeout) {

    $scope.reservation = {};
    $scope.hasDiscount = false;
    $scope.discCode = "";
    $scope.discount = {};
    $scope.coordinates = {};


    /*I18N date-picker*/
    $scope.month = [Messages("date-picker.months.1"), Messages("date-picker.months.2"), Messages("date-picker.months.3"), Messages("date-picker.months.4"), Messages("date-picker.months.5"), Messages("date-picker.months.6"), Messages("date-picker.months.7"), Messages("date-picker.months.8"), Messages("date-picker.months.9"), Messages("date-picker.months.10"), Messages("date-picker.months.11"), Messages("date-picker.months.12")];
    $scope.monthShort = [Messages("date-picker.months.short.1"), Messages("date-picker.months.short.2"), Messages("date-picker.months.short.3"), Messages("date-picker.months.short.4"), Messages("date-picker.months.short.5"), Messages("date-picker.months.short.6"), Messages("date-picker.months.short.7"), Messages("date-picker.months.short.8"), Messages("date-picker.months.short.9"), Messages("date-picker.months.short.10"), Messages("date-picker.months.short.11"), Messages("date-picker.months.short.12")];
    $scope.weekdaysFull = [Messages("date-picker.days.full.1"), Messages("date-picker.days.full.2"), Messages("date-picker.days.full.3"), Messages("date-picker.days.full.4"), Messages("date-picker.days.full.5"), Messages("date-picker.days.full.6"), Messages("date-picker.days.full.7")];
    $scope.weekdaysShort = [Messages("date-picker.days.short.1"),Messages("date-picker.days.short.2"), Messages("date-picker.days.short.3"), Messages("date-picker.days.short.4"), Messages("date-picker.days.short.5"), Messages("date-picker.days.short.6"), Messages("date-picker.days.short.7")];
    $scope.weekdaysLetter = [Messages("date-picker.days.letter.1"), Messages("date-picker.days.letter.2"), Messages("date-picker.days.letter.3"), Messages("date-picker.days.letter.4"), Messages("date-picker.days.letter.5"), Messages("date-picker.days.letter.6"), Messages("date-picker.days.letter.7")];

    $scope.todayButton = Messages("date-picker.button.today");
    $scope.clearButton = Messages("date-picker.button.clear");
    $scope.closeButton = Messages("date-picker.button.close");

    $scope.nextMonthLabel = Messages("date-picker.label.nextMonth");
    $scope.prevMonthLabel = Messages("date-picker.label.prevMonth");

    $scope.disable = [];

    var yesterday = [new Date().getFullYear(),new Date().getMonth(),new Date().getDate()-1];

    $scope.getRestaurant = function(){
        var id = $window.location.href.split("id=")[1];
        $http.get("/restaurant/"+ id).then(
            function (response){
                $scope.restaurant = response.data;
                var disable =[];
                var disableAux = [];
                if($scope.restaurant.openingDays.length !== 7){
                    for(var i = 0; i < $scope.restaurant.openingDays.length; i++) {
                        if ($scope.restaurant.openingDays[i].id == 7){
                            disableAux.push(1);
                        }else{
                            disableAux.push($scope.restaurant.openingDays[i].id + 1);
                        }
                    }
                    for (var j = 1; j <= 7; j++){
                        if(!contains(disableAux,j)){
                            disable.push(j);
                        }
                    }
                }

                disable.push({from: [0,0,0], to: yesterday  });
                $scope.disable = disable;
                $scope.defineTurns();
                $scope.coordinates.lat = $scope.restaurant.address.lat;
                $scope.coordinates.lng = $scope.restaurant.address.lng;
                $scope.getMenu();
            }
        );
    };
    $scope.getRestaurant();

    var internationalizeDatePicker = function(){
        $('.datepicker').pickadate({
            monthsFull: $scope.month,
            monthsShort: $scope.monthShort,
            weekdaysFull: $scope.weekdaysFull,
            weekdaysShort: $scope.weekdaysShort,
            weekdaysLetter: $scope.weekdaysLetter,
            today: $scope.todayButton,
            clear: $scope.clearButton,
            close: $scope.closeButton,
            labelMonthNext: $scope.nextMonthLabel,
            labelMonthPrev: $scope.prevMonthLabel
        });
    };
    internationalizeDatePicker();


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
            var reservationSuccess = Messages("success.message.reservation.placed");
            Materialize.toast(reservationSuccess, 2000, "green");
            //Materialize.toast("La reserva se ha procesado con éxito.", 2000, "green");
            $timeout(function(){
                $window.location.href = "/client/restaurant?id="+$scope.restaurant.id;
            }, 1000);
        }, function(response){
            var error = Messages("error.message.error.occurs.try.later");
            Materialize.toast(error, 2000, "red");
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
    };

    var contains = function (array, e) {
        for (var i = 0; i < array.length; i++){
            if(array[i] === e)return true;
        }
        return false;
    }

});


