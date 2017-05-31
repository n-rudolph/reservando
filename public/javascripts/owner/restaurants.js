var app = angular.module("reservandoApp");

app.controller("OwnerRestaurantsCtrl", function ($scope, $http, $window) {

    $scope.restaurants = [];
    $scope.page = 0;
    $scope.pageSize = 8;
    $scope.hasNext = false;
    $scope.seed = Math.floor(Math.random() * 100);
    $scope.loading = false;
    $scope.showContent = false;

    $scope.getRestaurants = function(){
        $scope.loading = true;
        $http.get('/restaurants/of-owner/'+$scope.page+'/'+$scope.pageSize+'/'+$scope.seed).then(function(response){
            $scope.restaurants.push.apply($scope.restaurants, response.data.restaurants);
            $scope.hasNext = response.data.hasNext;
            $scope.loading = false;
        }, function(response) { $scope.loading = false;});
    };
    $scope.getRestaurants();


    $scope.nextPage = function() {
        if ($scope.hasNext){
            $scope.page++;
            $scope.getRestaurants();
        }
    };

    $scope.openRestaurant = function(rid) {
        $window.location.href = "/owner/restaurant?id=" + rid;
    };

    window.addEventListener('scroll', function(e) {
        $scope.showContent = $(window).scrollTop() > 100;
        $scope.$apply();
    });
});
