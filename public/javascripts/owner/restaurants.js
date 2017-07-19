var app = angular.module("reservandoApp");
app.requires.push('ui.materialize');
app.requires.push('ngRateIt');

app.controller("OwnerRestaurantsCtrl", function ($scope, $http, $window) {

    $scope.restaurants = [];
    $scope.loading = false;
    $scope.showContent = false;
    $scope.filterTerm = "";
    $scope.filterTerm2 = "";

    $scope.currentPage = 1;
    $scope.minIndex = 0;
    $scope.maxIndex = 8;

    //Var used when the restaurant has not been rated yet.
    $scope.nonQualification = 0;

    $scope.getRestaurants = function(){
        $scope.loading = true;
        $http.get('/restaurants/of-owner').then(function(response){
            $scope.restaurants.push.apply($scope.restaurants, response.data.restaurants);
            $scope.hasNext = response.data.hasNext;
            $scope.loading = false;
        }, function(response) { $scope.loading = false;});
    };
    $scope.getRestaurants();

    $scope.openRestaurant = function(rid) {
        $window.location.href = "/owner/restaurant?id=" + rid;
    };

    $scope.filter = function(restaurant) {
        if ($scope.filterTerm === ""){
            return true;
        }
        if (restaurant === undefined) return false;
        return restaurant.name.toLowerCase().includes($scope.filterTerm.toLowerCase());
    };

    $scope.filterByPage = function(item, index) {
        return index >= $scope.minIndex && index <= $scope.maxIndex
    };

    $scope.changePage = function(page) {
        $scope.currentPage = page;
        $scope.minIndex = (page-1) * 9;
        $scope.maxIndex = (page * 9) - 1;
    };

    $scope.resetPage = function() {
        $scope.currentPage = 1;
        $scope.filterTerm = $scope.filterTerm2;
    };

    $scope.printCuisines = function(restaurant) {
        if (restaurant == null || restaurant == undefined)
            return "";
        var cuisineString = "";
        var tokken = "";
        for (var i = 0; i < restaurant.cuisines.length; i++){
            cuisineString += tokken;
            cuisineString += restaurant.cuisines[i].name;
            tokken = " - ";
        }
        return cuisineString;
    };

    $scope.truncateText = function(text, length){
        if (text == undefined || length == undefined)
            return "";
        if (text.length > length){
            return text.substr(0, length -1) + "...";
        } else return text;
    };
});
