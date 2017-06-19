var app = angular.module("reservandoApp");

app.controller("MyOrdersCtrl", function ($scope, $http, $window, $timeout) {

    $scope.orders = [];

    $http.get("/orders/client").then(function(response){
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
});


