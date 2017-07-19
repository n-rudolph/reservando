var app = angular.module("reservandoApp");
app.requires.push('ngMap');
app.requires.push('vsGoogleAutocomplete');

app.service('serverCommunication', ['$http','$q', function ($http, $q){
    this.postToUrl = function(data, uploadUrl){
        var defered = $q.defer();
        var promise = defered.promise;
        $http({
            method: 'POST',
            url: uploadUrl,
            data: data
        }).success(function(){
            //Materialize.toast(successResponse, 2000, 'green');
            defered.resolve();
        }).error(function (serverErrorResponse) {
            //Materialize.toast(successResponse, 2000, 'red');
            defered.reject();
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

app.controller("ClientProfileCtrl",['$scope', '$http', 'serverCommunication','$window', function ($scope, $http, serverCommunication, $window) {

    $scope.editMode = false;
    $scope.address = {};
    $scope.options = {
        componentRestrictions: { country: 'AR' }
    };

    /*This load the current user data*/
    var loadUserData = function(){
        serverCommunication.getFromUrl('/client/profile/user')
            .then(function(data){
                $scope.user = data;
                console.log($scope.user);
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
        var password = {
            oldPassword: $scope.previousPassword,
            newPassword: $scope.newPassword1,
            checkPassword: $scope.newPassword2
        };
        if (password.oldPassword && password.newPassword && password.checkPassword){
            $http.put("/user/password", password).then(function(response){
                Materialize.toast(response.data,2000,"green");
                $('#changePasswordModal').modal('close');
            }, function(responseError){
                Materialize.toast(responseError.data, 2000, "red");
                /*if (response.data == "oldPassword"){
                    Materialize.toast("La contraseña anterior no es valida", 2000, "red");
                    $("#changePasswordModal").closeModal();
                }else {
                    Materialize.toast("Ha ocurrido un erros. Intentelo más tarde.", 2000, "red");
                }*/
            });
        }else{
            var error = Messages("error.message.there.are.error.on.fields");
            Materialize.toast(error, 2000, "red");
            //Materialize.toast("Hay errores en los campos.", 2000, "red");
        }
    };

    $scope.deleteAccount = function () {
        /* Ask if it is necessary to request the username and the password for deleting the account.*/
        var data = {data: ''};
        serverCommunication.postToUrl(data,'/client/deleteAccount')
            .then(function(){
                //Redirects to the login page.
                $window.location.href = "/";
            })
    };

    $scope.openChangePasswordModal = function() {
        $('#changePasswordModal').modal('open');
    };

    $scope.openDeleteAccountModal = function () {
        $('#deleteAccountModal').modal('open');
    };

    //Edit Mode
    $scope.photos = [];

    $scope.setEditMode = function(editMode){
        if (editMode){
            $scope.resetEditUser();
            $scope.resetErrors();
            $scope.cancelEditPhoto();
        }
        $scope.editMode = editMode;
    };

    $scope.resetEditUser = function(){
        $scope.editUser = {
            firstName: $scope.user.firstName,
            lastName: $scope.user.lastName,
            address: $scope.user.address,
            email: $scope.user.email
        }
    };

    $scope.resetErrors = function(){
        $scope.errors ={
            firstName: false,
            lastName: false,
            email: false,
            address:false
        }
    };

    $scope.cancelEditPhoto = function(){
        $scope.photos = [];
        $("#image-input").val("");
    };

    $scope.savePhoto = function(){
        $scope.errors.photoSize = false;
        if ($scope.photos[0].size < 2000000){
            var photo = {
                name: $scope.photos[0].name,
                src: $scope.photos[0].src
            };
            $http.put("/user/photo", photo).then(function(response){
                $scope.user.photo = response.data;
                var updateSuccessfully = Messages("success.message.photo.update.successfully");
                Materialize.toast(updateSuccessfully, 2000, "green");
                $scope.cancelEditPhoto();
            }, function(responseError){
                Materialize.toast(responseError.data, 2000, "red");
            });
        }else{
            $scope.errors.photoSize = true;
        }
    };

    $scope.saveEditUser = function(){
        $scope.resetErrors();
        $scope.geocodeAddress();
    };

    $scope.submitUser = function(){
        if ($scope.checkEditInfo()){
            $http.put("/user/info", $scope.editUser).then(function(response){
                var data = response.data;
                $scope.user.firstName = data.firstName;
                $scope.user.lastName = data.lastName;
                $scope.user.email = data.email;
                $scope.user.address = data.address;
                var infoUpdate = Messages("success.message.info.update.successfully");
                Materialize.toast(infoUpdate, 2000, "green");
                //Materialize.toast("Se ha actualizado la información con éxito.", 2000, "green");
                $scope.setEditMode(false);
            }, function(responseError){
                Materialize.toast(responseError.data, 2000, "red");
                /*var data = response.data;
                if (data == "email"){
                    Materialize.toast("El email ya esta ocupado", 2000, "red");
                    $scope.errors.email = true;
                } else{
                    Materialize.toast("Ha ocurrido un error. Intentelo más tarde", 2000, "red");
                }*/
            });
        }else{
            var errorOnFields = Messages("error.message.there.are.error.on.fields");
            Materialize.toast(errorOnFields, 2000, "red");
            //Materialize.toast("Hay errores en los campos.", 2000, "red");
        }
    };

    $scope.geocodeAddress = function() {
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({'address': $scope.editUser.address.addressString}, function(results, status) {
            if (status === 'OK') {
                $scope.editUser.address.lat = results[0].geometry.location.lat();
                $scope.editUser.address.lng = results[0].geometry.location.lng();
                $scope.submitUser();
            } else {
                $scope.errors.address = true;
                var addressNotValid = Messages("error.message.geolocalization.address.not.valid");
                Materialize.toast(addressNotValid, 2000, "red");
                //Materialize.toast("La dirección no es valida", 2000, "red");
            }
        });
    };

    $scope.checkEditInfo = function(){
        var errors = 0;
        if ($scope.editUser.firstName.length == 0){
            errors++;
            $scope.errors.firstName = true;
        }
        if ($scope.editUser.lastName.length == 0){
            errors++;
            $scope.errors.lastName = true;
        }
        if ($scope.editUser.email.length == 0){
            errors++;
            $scope.errors.email = true;
        }
        if ($scope.editUser.address.length == 0){
            errors++;
            $scope.errors.address = true;
        }
        return errors == 0;
    };


}]);
