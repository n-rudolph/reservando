/**
 * Created by Gustavo on 3/6/17.
 */
var app = angular.module("reservandoApp", ['ngMap']);

//app.requires.push('ngMap');

app.service('serverCommunication', ['$http','$q', function ($http, $q){
    this.postToUrl = function(data, uploadUrl, successResponse, errorResponse){
        var defered = $q.defer();
        var promise = defered.promise;
        $http({
            method: 'POST',
            url: uploadUrl,
            data: data
        }).success(function(data){
            Materialize.toast(successResponse, 3000, 'green');
            defered.resolve(data);
        }).error(function (serverErrorResponse) {
            //Checks if the server send an error msj.
            if(serverErrorResponse){
                //Materialize.toast(serverErrorResponse, 3000, 'red');
            }
            else {
                Materialize.toast(errorResponse, 3000, 'red');
            }
            defered.reject(serverErrorResponse);
        });
        return promise;
    };

    this.getFromUrl = function (url, successResponse, errorResponse) {
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
        resultsWithFilters: [],
        showSearchWithoutFilters: false,
        showSearchWithFilters: false,
        noResults: false
    };

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

    /*This load the current user data (the main purpose of this is to load the user recommendations)*/
    var loadUserDataAndRecommendations = function(){
        serverCommunication.getFromUrl('/client/profile/user','','')
            .then(function(data){
                $scope.user = data;

                var dataToPost = {
                    quantity: 3,
                    userEmail:$scope.user.email
                };
                serverCommunication.postToUrl(dataToPost,"/client/getRecommendations","","")
                    .then(function (responseData) {
                        $scope.recommendations = responseData;
                        $(document).ready(function(){
                            $('.slider').slider({height: "40%"});
                        });

                    })
                    .catch(function(error){

                    });
            })
            .catch(function (err) {
                //Materialize.toast("No se pudo cargar la información", 3000, "red");
            })
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
        resetPreviousResultsSearched();
        resetPreviousResultsWithFilters();

        var dataToPost = {wordToSearch: $scope.wordToSearch};
        if($scope.wordToSearch.length > 0){
            serverCommunication.postToUrl(dataToPost,"/client/search","","")
                .then(function(responseData){
                    $scope.result.allResults = responseData;
                    initMap();
                    loadMarkers();
                    $scope.result.showSearchWithoutFilters = true;
                    $scope.result.showSearchWithFilters = false;
                    responseData.length === 0 ? $scope.result.noResults = true : $scope.result.noResults = false;
                    loadFilters(responseData);
                })
                .catch(function(error){
                    Materialize.toast("No se pudo realizar la busqueda, intente más tarde.", 3000, "red");
                    console.log(error);
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

    $scope.openLocal = function(restaurantId){
        $window.location.href = "restaurant?id=" + restaurantId;
    };

    $scope.searchForAutocomplete = function(){
        serverCommunication.getFromUrl("/client/searchAll","","")
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

    $scope.applyFilters = function(){

        /*Resets the previous results with filter*/
        $scope.result.resultsWithFilters = [];
        $scope.result.noResults = false;
        
        /*Filters applied force to show restaurants and deliveries*/
        if($scope.filtersApplied.showRestaurants &&  $scope.filtersApplied.showDeliveries){
            $scope.result.resultsWithFilters = $scope.result.allResults;
        }
        /*Filters applied force to show restaurants or deliveries (not both)*/
        else if($scope.filtersApplied.showRestaurants ||  $scope.filtersApplied.showDeliveries){
            for(var i = 0; i < $scope.result.allResults.length; i++){
                var resultWithFiltersLength = $scope.result.resultsWithFilters.length;

                if($scope.result.allResults[i].local){
                    if($scope.filtersApplied.showRestaurants){
                        $scope.result.resultsWithFilters[resultWithFiltersLength] = $scope.result.allResults[i];
                    }
                }
                else{
                    if($scope.filtersApplied.showDeliveries){
                        $scope.result.resultsWithFilters[resultWithFiltersLength] = $scope.result.allResults[i];
                    }
                }
            }
        }

        var auxResults = [];

        /*Filters applied force to show only some cuisines, it filters the previous filter by restaurant or by delivery 
        list*/
        for(var f = 0; f < $scope.filtersApplied.showThisCuisines.length; f++){
            var cuisineSelected = $scope.filtersApplied.showThisCuisines[f];
            if(cuisineSelected.isActive){
                for(var j = 0; j < $scope.result.resultsWithFilters.length; j++){
                    var cuisines = $scope.result.resultsWithFilters[j].cuisines;
                    for(var k = 0; k < cuisines.length; k++){
                        if(cuisineSelected.name === cuisines[k].name){
                            if(!resultAlreadyHere($scope.result.resultsWithFilters[j], auxResults)){
                                auxResults[auxResults.length] = $scope.result.resultsWithFilters[j];
                            }
                            break;
                        }
                    }

                }
            }
        }

        if(auxResults.length === 0) $scope.result.noResults = true;

        $scope.result.resultsWithFilters = auxResults;
        $scope.result.showSearchWithoutFilters = false;
        $scope.result.showSearchWithFilters = true;

        initMap();
        loadMarkers();
    };

    $scope.showInfoMarker = function(evt, result){
        $scope.restaurantSelectedInMap = result;
        $scope.map.showInfoWindow('myInfoWindow', this);
    };

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

    /*This functions is used to not repeat the same results when the filters are applied*/
    var resultAlreadyHere = function (result, listResults) {
        for(var i = 0; i < listResults.length; i++){
            if(result.id === listResults[i].id) return true;
        }
        return false;
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

    /*This function is used to load all the markers of the restaurants in the map*/
    var loadMarkers = function(){
        //Timeout is needed to avoid getting 'map undefined'.
        $timeout(function(){
            var bounds = new google.maps.LatLngBounds();
            var resultsToUse;

            if($scope.result.showSearchWithoutFilters){resultsToUse = $scope.result.allResults;}
            else {resultsToUse = $scope.result.resultsWithFilters;}

            for(var i = 0; i < resultsToUse.length; i++){
                var address = resultsToUse[i].address;
                bounds.extend(new google.maps.LatLng(address.lat, address.lng));
            }

            $scope.map.setCenter(bounds.getCenter());
            $scope.map.fitBounds(bounds);
            //Zoom out a little bit if the result on the map is only one.
            if(resultsToUse.length === 1){
                $scope.map.setZoom(14);
            }
        }, 2000);
    }

}]);



