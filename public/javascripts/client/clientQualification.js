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

app.controller("ClientQualificationCtrl",['$scope', 'serverCommunication', '$window','$timeout',
    function($scope, serverCommunication, $window, $timeout){

    $scope.getReservation = function(){
        var reservationId = $window.location.href.split("id=")[1];
        serverCommunication.getFromUrl("/reservationById/" + reservationId, "", "")
            .then(function(data){
                $scope.reservation = data;
            })
            .catch(function(error){
                Materialize.toast("Ocurrio un error, intente mas tarde.")
            });
    };
    $scope.getReservation();

    $(document).ready(function() {
        $('select').material_select();
        $('input#input_text, textarea#textarea1').characterCounter();
    });

    $scope.qualify = function(){
        if($scope.validateQualification()){
            var qualification = document.getElementById("qualification").value;
            var comments = $scope.comments;
            var data = {qualification: qualification,
                        client: $scope.reservation.client,
                        restaurant: $scope.reservation.local};
            serverCommunication.postToUrl(data,"/qualification","Calificación realizada","Ha ocurrido un problema, intente mas tarde")
                .then(function (response) {
                    $timeout(function () {
                        $window.location.href = "/my-reservations";
                    }, 3000);
                })
                .catch(function(error){

                });

        }


    };

    $scope.validateQualification = function () {
        var error = false;
        $scope.commentError = false;
        $scope.qualificationError = false;

        if(!$scope.comments){
            $scope.commentError = true;
            error = true;
        }
        if(document.getElementById("qualification").value == -1){
            $scope.qualificationError = true;
            error = true;
        }

        return !error;
    }


}]);