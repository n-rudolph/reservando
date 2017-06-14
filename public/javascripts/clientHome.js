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



app.controller("ClientHomeCtrl",['$scope', '$http', 'serverCommunication', '$window', function($scope, $http, serverCommunication, $window){

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

    $scope.openLocal = function(local){
        var dataToPost = {localSelectedId: local.id};
        serverCommunication.postToUrl(dataToPost,"/client/changeLocalSelected","","")
            .then(function (responseData){
                $window.location.href = "/client/viewLocal";
            })
            .catch(function (error) {

            });
    };



}]);



