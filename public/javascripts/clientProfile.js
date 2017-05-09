var app = angular.module("reservandoApp", []);

app.directive('fileModel', ['$parse', function($parse){
    return{
        restrict: 'A',
        link: function (scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                })
            })
        }
    }
}]);

app.service('serverCommunication', ['$http','$q', function ($http, $q){
    this.postToUrl = function(data, uploadUrl, successResponse, errorResponse){
        var defered = $q.defer();
        var promise = defered.promise;
        $http({
            method: 'POST',
            url: uploadUrl,
            data: data
        }).success(function(){
            Materialize.toast(successResponse, 3000, 'green');
            defered.resolve();
        }).error(function (serverErrorResponse) {
            //Checks if the server send an error msj.
            if(serverErrorResponse){
                Materialize.toast(serverErrorResponse, 3000, 'red');
            }
            else {
                Materialize.toast(errorResponse, 3000, 'red');
            }
            defered.reject();
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

app.controller("ClientProfileCtrl",['$scope', '$http', 'serverCommunication','$window', function ($scope, $http, serverCommunication, $window) {

    /*This load the current user data*/
    var loadUserData = function(){
        serverCommunication.getFromUrl('/client/profile/user','','')
            .then(function(data){
                $scope.user = data;
            })
            .catch(function (err) {
                Materialize.toast("No se pudo cargar la información", 3000, "red");
            })
    };
    loadUserData();

    $scope.validatePassword1 = function () {
        $scope.input1Touched = true;
        $scope.validPassword = ($scope.newPassword1.length > 6 && $scope.newPassword1.match(/\d+/g) != null);
    };

    $scope.validatePassword2 = function () {
        $scope.input2Touched = true;
        $scope.equalPasswords = $scope.newPassword1 == $scope.newPassword2;
    };

    $scope.changePassword = function () {
        var userData = {
            email: $scope.user.email,
            previousPassword: $scope.previousPassword,
            newPassword: $scope.newPassword1
        };
        serverCommunication.postToUrl(userData,'/client/changePassword','Contraseña cambiada satisfactoriamente','Contraseña no cambiada');
    };

    $scope.changeBioInfo = function () {
        var data = {
            firstName: $scope.user.firstName,
            lastName: $scope.user.lastName
        };
        serverCommunication.postToUrl(data,'/client/changeBioInfo','Información actualizada','');
    };

    $scope.changeAddress = function () {
        var data = {address: $scope.address = $("#clientAddress").val()};
        serverCommunication.postToUrl(data,'/client/changeAddress','Ubicación actualizada','');
    };

    $scope.changeProfileImage = function () {
        //Checks for a valid image.
        if($scope.imageFile){
            //The code below converts the image to base 64 encoding and send it (as string) to the server.
            var reader = new FileReader();
            reader.readAsDataURL($scope.imageFile);
            reader.onload = function(){
                var fileEncodedBase64 = $scope.imageFile.name + "," + reader.result;
                var data = {data: fileEncodedBase64};
                //The encoded image is sent to the server.
                serverCommunication.postToUrl(data, '/client/changeProfileImage', 'La foto se cargo exitosamente', 'La foto no pudo ser cargada')
                    .then(function(){
                        //After some seconds load the new image. Angular won't find the image (404 error), the problem
                        //is related to how often play refresh the public Assets (where the current profile image is).
                        /*setTimeout(function () {
                         loadUserData();
                         },1000);*/
                    });
            };
            reader.onerror = function () {
                //var fileEncodingBase64Error = reader.error;
                Materialize.toast('La foto no pudo ser cargada', 3000, 'red');
            }
        } else {
            Materialize.toast("Debe seleccionar una foto", 3000, 'red');
        }
    };

    $scope.deleteAccount = function () {
        /* Ask if it is necessary to request the username and the password for deleting the account.*/
        var data = {data: ''};
        serverCommunication.postToUrl(data,'/client/deleteAccount','','')
            .then(function(){
                //Redirects to the login page.
                $window.location.href = "http://localhost:9000/";
            })
            .catch(function (err) {
            })
    };

    $scope.openChangePasswordModal = function() {
        $('#changePasswordModal').openModal();
    };

    $scope.openDeleteAccountModal = function () {
        $('#deleteAccountModal').openModal();
    };

    $scope.openChangeProfileImageModal = function () {
        $('#changeProfileImageModal').openModal();
    };


}]);
