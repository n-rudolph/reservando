/**
 * Created by Gustavo on 3/6/17.
 */
var app = angular.module("reservandoApp");

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



app.controller("ClientHomeCtrl",['$scope', '$http', 'serverCommunication', function($scope, $http, serverCommunication){

    $scope.obj = {
        results: null,
        showSearch: false,
        noResult: false
    };

    $scope.search = function(){
        var dataToPost = {wordToSearch: $scope.wordToSearch,
            filterByName: $scope.filterByName,
            filterByCuisine: $scope.filterByCuisine,
            filterByLocalization: $scope.filterByLocalization,
            filterByRestaurant: $scope.filterByRestaurant,
            filterByDelivery: $scope.filterByDelivery
        };
        if($scope.wordToSearch.length > 0){
            serverCommunication.postToUrl(dataToPost,"/client/search","","")
                .then(function(responseData){
                    $scope.obj.results = responseData;
                    $scope.obj.showSearch = true;
                    responseData.length == 0 ? $scope.obj.noResult = true : $scope.obj.noResult = false;
                })
                .catch(function(error){
                    Materialize.toast("No se pudo realizar la busqueda.", 3000, "red");
                })
        }
    };

    $scope.showResults = function () {
        return $scope.obj.showSearch && !$scope.obj.noResult;
    };

    $scope.noResults = function () {
       return $scope.obj.showSearch && $scope.obj.noResult;
    };


}]);

//TO TEST!
/*
var defaultDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras fermentum nec elit at ultricies. Sed libero odio, lacinia nec diam eget, facilisis aliquam sapien. Mauris non ultricies nibh. Nullam ex augue, pellentesque id mattis vitae, pharetra nec eros. Mauris ac libero metus. Aenean dignissim tellus rutrum sem viverra viverra. Integer dapibus enim ac fermentum eleifend. Fusce mollis lectus in justo ultricies, et aliquet ligula semper. Donec ultrices consequat tincidunt. Duis fringilla justo orci, id volutpat augue venenatis id. Aliquam elementum neque eu nibh sagittis, non luctus purus laoreet. Quisque ut velit dignissim, condimentum velit sit amet, egestas ligula. In blandit lobortis lectus volutpat volutpat. Donec cursus at leo sit amet aliquet. Phasellus pulvinar facilisis condimentum. Nulla vel risus placerat, cursus ipsum et, varius magna.";
var showSearch = true;
var results = [
    {
        name: "Result1",
        localization: "Loc.1",
        description: defaultDescription
    },
    {
        name: "Result2",
        localization: "Loc.2",
        description: defaultDescription
    },
    {
        name: "Result3",
        localization: "Loc.3",
        description: defaultDescription
    },
    {
        name: "Result4",
        localization: "Loc.4",
        description: defaultDescription
    },
    {
        name: "Result5",
        localization: "Loc.5",
        description: defaultDescription
    }
];*/



