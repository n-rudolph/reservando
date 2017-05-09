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

app.service('fileUpload', ['$http','$q', function ($http, $q){
    this.uploadFileToUrl = function(file, uploadUrl, successResponse, errorResponse){
        var defered = $q.defer();
        var promise = defered.promise;

        var data = {data: file};
        $http({
            method: 'POST',
            url: uploadUrl,
            data: data
        }).success(function(){
            Materialize.toast(successResponse, 3000, 'green');
            defered.resolve();
        }.error(function () {
            Materialize.toast(errorResponse, 3000, 'red');
            defered.reject();
        }));
        return promise;
    };

}]);

app.controller("ClientProfileCtrl",['$scope', '$http', 'fileUpload','$window', function ($scope, $http, fileUpload, $window) {

    /*This load the current user data*/
    var loadUserData = function(){
        $http({
            method: 'GET',
            url: '/client/profile/user'
        }).success(function(data) {
            $scope.user = data;
        });
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
        $http({
            method: 'POST',
            url: '/client/changePassword',
            data: userData
        }).then(function () {
            Materialize.toast("Contraseña cambiada satisfactoriamente", 3000, "green");
        }, function (response) {
            Materialize.toast(response.data, 3000, "red");
        })
    };

    $scope.changeBioInfo = function () {
        var data = {
            firstName: $scope.user.firstName,
            lastName: $scope.user.lastName
        };

        $http({
            method: 'POST',
            url: '/client/changeBioInfo',
            data: data
        }).then(function () {
            Materialize.toast("Información actualizada", 3000, 'green');
        }, function (response) {
            Materialize.toast(response, 3000, 'red');
        })
    };

    $scope.changeAddress = function () {
        var data = {address: $scope.address = $("#clientAddress").val()};

        $http({
            method: 'POST',
            url: '/client/changeAddress',
            data: data
        }).then(function () {
            Materialize.toast("Ubicación actualizada", 3000, 'green');
        }, function (response) {
            Materialize.toast(response, 3000, 'red');
        })
    };

    $scope.changeProfileImage = function () {
        var uploadUrl = '/client/changeProfileImage';
        uploadFile(uploadUrl, $scope.imageFile, 'La foto se cargo exitosamente', 'La foto no pudo ser cargada');
    };

    var uploadFile = function (uploadUrl, file, successResponse, errorResponse) {
        //The code below converts the image to base 64 encoding and send it (as string) to the server.
        var reader = new FileReader();
        reader.readAsDataURL(file);

        reader.onload = function(){
            var fileEncodedBase64 = file.name + "," + reader.result;
            fileUpload.uploadFileToUrl(fileEncodedBase64, uploadUrl, successResponse, errorResponse)
                .then(function(){
                    //After a second load the new image. Without this code, angular won't find the image (404 error);
                    /*setTimeout(function () {
                        loadUserData();
                    },1000);*/
                })
        };
        reader.onerror = function () {
            //var fileEncodingBase64Error = reader.error;
            Materialize.toast(errorResponse, 2000, 'red');
        }
    };

    $scope.changePhoto = function () {

    };

    $scope.deleteAccount = function () {
        /* Ask if it is necessary to request the username and the password for deleting the account.*/
        $http({
            method: 'POST',
            url: '/client/deleteAccount'
        }).then(function () {
            //Redirects to the login page.
            $window.location.href = "http://localhost:9000/";
        }, function (response) {
            Materialize.toast(response.data, 5000, "red");
            //console.log(response.data)
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
