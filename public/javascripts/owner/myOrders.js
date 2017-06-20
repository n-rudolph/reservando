var app = angular.module("reservandoApp");

app.controller("MyOrdersCtrl", function ($scope, $http) {

    $scope.orders = [];

    $http.get("/orders/owner").then(function(response){
        $scope.orders = response.data;
    });

    $(document).ready(function(){
        $('.collapsible').collapsible();
    });

    $scope.calculateSubtotal2 = function(mealOrder){
        var amount = mealOrder.amount;
        if (amount == undefined)
            return 0;
        return amount * mealOrder.meal.price;
    };

    $scope.calculateTotal = function (order) {
        var total = 0;
        for (var i = 0; i < order.meals.length; i++){
            total += $scope.calculateSubtotal2(order.meals[i]);
        }
        if (order.discount)
            total = total * (1 - (order.discount.discount / 100));
        return total;
    };

    $scope.getDateTimeFormat = function(timeObject){

        var minute = timeObject.minuteOfHour;
        if (timeObject.minuteOfHour < 10){
            minute = "0"+minute;
        }
        return timeObject.dayOfMonth +"/"+ timeObject.monthOfYear +"/"+ timeObject.yearOfEra + "  " + timeObject.hourOfDay+":"+ minute;
    };

    $scope.isExpire = function(order){
        var date = Date.now();
        var responseTime = order.delivery.responseTime;

        var placedDate = new Date(order.timePlaced.yearOfEra, order.timePlaced.monthOfYear -1, order.timePlaced.dayOfMonth, order.timePlaced.hourOfDay, order.timePlaced.minuteOfHour, 0, 0);

        placedDate.setMinutes(placedDate.getMinutes() + responseTime);
        return date >= placedDate;
    }
});


