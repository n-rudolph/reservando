
var app = angular.module("reservandoApp");

app.requires.push('ngMap');
app.requires.push('ui.materialize');
app.requires.push('ngAnimate');
app.requires.push('ngRateIt');

app.service('serverCommunication', ['$http','$q', function ($http, $q){
    this.postToUrl = function(data, uploadUrl){
        var defered = $q.defer();
        var promise = defered.promise;
        $http({
            method: 'POST',
            url: uploadUrl,
            data: data
        }).success(function(data){
            defered.resolve(data);
        }).error(function (serverErrorResponse) {
            defered.reject(serverErrorResponse);
        });
        return promise;
    };

    this.getFromUrl = function (url) {
        var defered = $q.defer();
        var promise = defered.promise;
        $http({
            method: 'GET',
            url: url
        }).success(function (data) {
            defered.resolve(data);
        }).error(function (err) {
            defered.reject(err);
        });
        return promise;
    }

}]);



app.controller("ClientHomeCtrl",['$scope', '$http', 'serverCommunication', '$window', 'NgMap','$timeout',
    function($scope, $http, serverCommunication, $window, NgMap, $timeout){
    /*scope needed variables*/
    $scope.result = {
        allResults: [],
        showSearchWithoutFilters: false,
        noResults: true
    };

    $scope.currentPage = 1;
    $scope.minIndex = 0;
    $scope.maxIndex = 11;

    $scope.showMap = false;

    //Var used to avoid initializing the same map more than once.
    var mapLoaded = false;

    $scope.recommendations = {photo: null};

    // To test! It must be implemented when the address change to an object.
    $scope.zones = [
        {zone: "Pilar"},
        {zone: "Capital Federal"}
    ];

    $scope.filtersToShow = {
        thereAreRestaurants: false,
        thereAreDeliveries: false,
        cuisines: []
    };

    $scope.filtersApplied = {
        showRestaurants: true,
        showDeliveries: true,
        showThisCuisines: []
    };

    $scope.filteredResults = [];

    /*This load the current user data (the main purpose of this is to load the user recommendations)*/
    var loadUserDataAndRecommendations = function(){
        serverCommunication.getFromUrl('/client/profile/user')
            .then(function(data){
                $scope.user = data;
            })
            .catch(function (err) {
                //Materialize.toast("No se pudo cargar la información", 3000, "red");
            });
        var dataToPost = {
            quantity: 3,
            userEmail:""
        };
        serverCommunication.postToUrl(dataToPost,"/client/getRecommendations")
            .then(function (responseData) {
                console.log(responseData);
                $scope.recommendations = responseData;
            })
            .catch(function(error){

            });
    };
    loadUserDataAndRecommendations();

    //Assign the enter key to the action of search, when the cursor is on the input search bar.
    document.getElementById("searchInput").addEventListener("keyup", function(event){
            event.preventDefault();
            if(event.keyCode === 13){
                document.getElementById("searchButton").click();
            }
    });

    $scope.search = function(){
        if ($scope.wordToSearch == undefined || $scope.wordToSearch.length == 0)
            return;
        resetPreviousResultsSearched();
        resetPreviousResultsWithFilters();
        var dataToPost = {wordToSearch: $scope.wordToSearch};
        if($scope.wordToSearch.length > 0){
            serverCommunication.postToUrl(dataToPost,"/client/search")
                .then(function(responseData){
                    $scope.currentPage = 1;
                    $scope.result.allResults = responseData;
                    $scope.geoResult = true;
                    //initMap();
                    responseData.length === 0 ? $scope.result.noResults = true : $scope.result.noResults = false;
                    loadFilters(responseData);
                })
                .catch(function(){
                    var error = Message("error.message.search.could.not.performed");
                    Materialize.toast(error, 2000, "red");
                })
        }
    };

    $scope.showResults = function () {
        return ($scope.result.showSearchWithoutFilters || $scope.result.showSearchWithFilters) && !$scope.result.noResults;
    };

    $scope.noResults = function () {
       return ($scope.result.showSearchWithoutFilters || $scope.result.showSearchWithFilters) && $scope.result.noResults;
    };

    $scope.showFilters = function () {
        return ($scope.result.showSearchWithoutFilters || $scope.result.showSearchWithFilters)
    };

    $scope.openRestaurant = function(restaurantId){
        $window.location.href = "restaurant?id=" + restaurantId;
    };

    $scope.searchForAutocomplete = function(){
        serverCommunication.getFromUrl("/client/searchAllRestaurants")
            .then(function (responseData) {
                $(document).ready(function(){
                    $('input.autocomplete').autocomplete({
                        data: getNamesAndCuisines(responseData),
                        limit: 5
                    })
                })
            })
            .catch(function(error){
            })
    };

    $scope.getIndex = function(cuisine){
        for(var i = 0; i < $scope.filtersApplied.showThisCuisines.length; i++){
            if($scope.filtersApplied.showThisCuisines[i].name === cuisine.name){
                return i;
            }
        }
    };

    $scope.applyFilter = function(restaurant) {
        if (!$scope.filtersApplied.showDeliveries && !restaurant.local)
            return false;
        if (!$scope.filtersApplied.showRestaurants && restaurant.local)
            return false;
        for (var i =0; i < restaurant.cuisines.length; i++){
            var cuisine = restaurant.cuisines[i];
            for (var j = 0; j < $scope.filtersApplied.showThisCuisines.length; j++) {
                if ($scope.filtersApplied.showThisCuisines[j].isActive &&
                    $scope.filtersApplied.showThisCuisines[j].name === cuisine.name) {
                    return true;
                }
            }
        }
        return false;
    };

    $scope.showInfoMarker = function(evt, result){
        $scope.restaurantSelectedInMap = result;
        $scope.map.showInfoWindow('myInfoWindow', this);
    };

    $scope.searchNearMe = function() {
        if (navigator.geolocation){
            navigator.geolocation.getCurrentPosition($scope.successGeolocation, $scope.errorGeolocation);
        }
    };

    $scope.successGeolocation = function(position){
        $http.get("/restaurants/nearMe/"+position.coords.latitude+"/"+position.coords.longitude)
            .then(function (response) {
                $scope.currentPage = 1;
                $scope.result.allResults = response.data;
                $scope.geoResult = true;
                initMap();
                response.data.length === 0 ? $scope.result.noResults = true : $scope.result.noResults = false;
                loadFilters(response.data);
                $scope.showMap = true;
                $timeout(function(){
                    $scope.showMap = false;
                }, 50);
            }, function () {
                var error = Messages("error.message.error.occurs.try.later");
                Materialize.toast(error, 2000, "red");
            });
    };
    $scope.errorGeolocation = function(error) {
        switch(error.code) {
            case error.PERMISSION_DENIED:
                var permissionDenied = Messages("error.message.geolocalization.denied.request");
                Materialize.toast(permissionDenied, 2000, "red");
                //Materialize.toast("User denied the request for Geolocation.", 2000, "red");
                break;
            case error.POSITION_UNAVAILABLE:
                var positionUnavailable = Messages("error.message.geolocalization.location.unavailable");
                Materialize.toast(positionUnavailable, 2000, "red");
                //Materialize.toast("Location information is unavailable.", 2000, "red");
                break;
            case error.TIMEOUT:
                var timeOut = Messages("error.message.geolocalization.request.timeout");
                Materialize.toast(timeOut, 2000, "red");
                //Materialize.toast("The request to get user location timed out.", 2000, "red");
                break;
            case error.UNKNOWN_ERROR:
                var unknownError = Messages("error.message.error.unknown");
                Materialize.toast(unknownError, 2000, "red");
                //Materialize.toast("An unknown error occurred.", 2000, "red");
                break;
        }
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

    $scope.latAverage = function(){
        var sum = 0;
        for(var i = 0; i < $scope.filteredResults.length; i++) {
            sum += $scope.filteredResults[i].address.lat;
        }
        if ($scope.filteredResults.length == 0)
            return 0;
        else  return sum/$scope.filteredResults.length;
    };
    $scope.lngAverage = function(){
        var sum = 0;
        for(var i = 0; i < $scope.filteredResults.length; i++) {
            sum += $scope.filteredResults[i].address.lng;
        }
        if ($scope.filteredResults.length == 0)
            return 0;
        else  return sum/$scope.filteredResults.length;
    };

    $scope.filterByPage = function(item, index) {
        return index >= $scope.minIndex && index <= $scope.maxIndex
    };

    $scope.changePage = function(page) {
        $scope.currentPage = page;
        $scope.minIndex = (page-1) * 12;
        $scope.maxIndex = (page * 12) - 1;
    };

    $scope.viewMap =function(){
       initMap();
       this.showMap=true;
    } ;

    /*Useful functions*/

    /*This function is used to load all the filters dynamically*/
    var loadFilters = function(results){
        $scope.filtersToShow.thereAreRestaurants = false;
        $scope.filtersToShow.thereAreDeliveries = false;
        $scope.filtersToShow.cuisines = [];

        for(var i = 0; i < results.length; i++){
            var restaurant = results[i];

            //Loads if there is any restaurant or any delivery on the results.
            if(restaurant.local){
                $scope.filtersToShow.thereAreRestaurants = true;
            }
            else {
                $scope.filtersToShow.thereAreDeliveries = true;
            }

            //Loads all the cuisines which are in the results.
            var cuisines = results[i].cuisines;
            for(var j = 0; j < cuisines.length; j++){
                if(!isThisCuisineHere(cuisines[j], $scope.filtersToShow.cuisines)){
                    var currentIndex = $scope.filtersToShow.cuisines.length;
                    $scope.filtersToShow.cuisines[currentIndex] = {name: cuisines[j].name}
                }
            }
        }

        /*This loads all the cuisines in the filterApplied.cuisines and set the value (isActive) to true */
        for (var k = 0; k < $scope.filtersToShow.cuisines.length; k++){
            $scope.filtersApplied.showThisCuisines[k] = {
                name: $scope.filtersToShow.cuisines[k].name,
                isActive: true
            }
        }
    };

    /*Function used on the load filters function. Used to not repeat the same value in the filters to show*/
    var isThisCuisineHere = function(cuisine, cuisineList){
        for(var i = 0; i < cuisineList.length; i++){
            if((cuisineList[i].name) === cuisine.name) {
                return true;
            }
        }
        return false;
    };

    /*This function is used to load all the restaurants names and cuisines. This is used on the autocomplete*/
    var getNamesAndCuisines = function(restaurants){
        var result = {};
        //Loads all the restaurants names.
        for (var i = 0; i < restaurants.length; i++) {
            var name = restaurants[i].name;
            result[name] = null;

            //Loads all the cuisines of each restaurant.
            var cuisines = restaurants[i].cuisines;
            for(var j = 0; j < cuisines.length; j++){
                result[cuisines[j].name] = null;
            }

        }
        return result;
    };

    /*This function is used to reset all the previous results of old search*/
    var resetPreviousResultsSearched = function(){
        $scope.result.allResults = [];
        $scope.result.resultsWithFilters = [];
        $scope.result.showSearchWithoutFilters = false;
        $scope.result.noResults = false;
    };

    /*This function is used to reset all the previous results with filters of old filtering search*/
    var resetPreviousResultsWithFilters = function(){
        $scope.filtersApplied.showRestaurants = true;
        $scope.filtersApplied.showDeliveries = true;
        for(var i = 0; i < $scope.filtersApplied.showThisCuisines.length; i++){
            $scope.filtersApplied.showThisCuisines[i].isActive = true;
        }
    };

    /*This function is used to init the ng-map, without using this function the map will be all grey
    * unless manually resize.*/
    var initMap = function () {
        if(!mapLoaded){
            NgMap.getMap("results-map").then(function(){
                $scope.map = NgMap.initMap("results-map");
                mapLoaded = true;
            });
        }
    };
}]);



