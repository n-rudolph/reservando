/**
 * Created by Gustavo on 11/6/17.
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

app.controller("LocalViewCtrl", ["$scope", "$http", "serverCommunication", function($scope, $http, serverCommunication){
    var defaultText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras fermentum nec elit at ultricies. Sed libero odio, lacinia nec diam eget, facilisis aliquam sapien. Mauris non ultricies nibh. Nullam ex augue, pellentesque id mattis vitae, pharetra nec eros. Mauris ac libero metus. Aenean dignissim tellus rutrum sem viverra viverra. Integer dapibus enim ac fermentum eleifend. Fusce mollis lectus in justo ultricies, et aliquet ligula semper. Donec ultrices consequat tincidunt. Duis fringilla justo orci, id volutpat augue venenatis id. Aliquam elementum neque eu nibh sagittis, non luctus purus laoreet. Quisque ut velit dignissim, condimentum velit sit amet, egestas ligula. In blandit lobortis lectus volutpat volutpat. Donec cursus at leo sit amet aliquet. Phasellus pulvinar facilisis condimentum. Nulla vel risus placerat, cursus ipsum et, varius magna.";

    var loadCurrentLocal = function () {
        serverCommunication.getFromUrl("/client/getLocalSelected")
            .then(function (data) {
                $scope.local = data;
                $scope.local.description = defaultText; //TO TEST ONLY!
            })
            .catch(function(error){});
    };
    loadCurrentLocal();

}]);

