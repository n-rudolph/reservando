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

    $scope.getReservation = function(reservationId){
        serverCommunication.getFromUrl("/reservation/" + reservationId, "", "")
            .then(function(data){
                $scope.reservation = data;
                //$scope.client = $scope.reservation.clientName;
                $scope.client = $scope.reservation.clientName;
            })
            .catch(function(error){
                Materialize.toast("Ocurrio un error, intente mas tarde.")
            });
    };

    $scope.getOrder = function (orderId) {
        serverCommunication.getFromUrl("/order/" + orderId , "", "")
            .then(function(data){
                $scope.order = data;
                //$scope.client = $scope.order.clientName;
            })
            .catch(function(error){
                Materialize.toast("Ocurrio un error, intente mas tarde.")
            })
    };

    $scope.loadOrderOrReservation = function(){
        var restaurantId = $window.location.href.split("/")[5];
        var isLocal = $window.location.href.split("/")[6];

        if(isLocal == "true") {
            $scope.getReservation(restaurantId);
        }

        else $scope.getOrder(restaurantId);
    };

    $scope.loadOrderOrReservation();


    $(document).ready(function() {
        $('select').material_select();
        $('input#input_text, textarea#textarea1').characterCounter();
    });

    $scope.qualify = function(){
        if($scope.validateQualification()){
            var qualification = document.getElementById("qualification").value;
            var comments = $scope.comments;
            var restaurant = $scope.reservation === undefined? $scope.order.delivery : $scope.reservation.local;
            var data = {
                qualification: qualification,
                client: $scope.reservation.client,
                restaurant: restaurant
            };

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