var app = angular.module("reservandoApp");
app.requires.push('ngMap');

app.service('serverCommunication', ['$http','$q', function ($http, $q){
    this.postToUrl = function(data, url, successResponse, errorResponse){
        var defered = $q.defer();
        var promise = defered.promise;
        $http({
            method: 'POST',
            url: url,
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

app.controller("OwnerProfileCtrl",['$scope', '$http', 'serverCommunication','$window', function ($scope, $http, serverCommunication, $window) {

    $scope.editMode = false;

    /*This load the current user data*/
    var loadUserData = function(){
        serverCommunication.getFromUrl('/owner/profile/user','','')
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
        var password = {
            oldPassword: $scope.previousPassword,
            newPassword: $scope.newPassword1,
            checkPassword: $scope.newPassword2
        };
        if (password.oldPassword && password.newPassword && password.checkPassword){
            $http.put("/user/password", password).then(function(response){
                Materialize.toast("La contraseña se ha cambiado con éxito", 2000, "green");
            }, function(response){
                if (response.data == "oldPassword"){
                    Materialize.toast("La contraseña anterior no es valida", 2000, "red");
                    $("#changePasswordModal").closeModal();
                }else {
                    Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
                }
            });
        }else{
            Materialize.toast("Hay errores en los campos.", 2000, "red");
        }
    };

    $scope.deleteAccount = function () {
      /* Ask if it is necessary to request the username and the password for deleting the account.*/
      var data = {data:''};
      serverCommunication.postToUrl(data,'/owner/deleteAccount','','')
          .then(function () {
              //Redirects to the login page.
              $window.location.href = "/";
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
            address: {
                addressString: $scope.user.address.address,
                lat: $scope.user.address.lat,
                lng: $scope.user.address.lng
            },
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
                Materialize.toast("Se ha actualizado la foto con éxito", 2000, "green");
                $scope.cancelEditPhoto();
            }, function(){
                Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 2000, "red");
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
                Materialize.toast("Se ha actualizado la información con éxito.", 2000, "green");
                $scope.setEditMode(false);
            }, function(response){
                var data = response.data;
                if (data == "email"){
                    Materialize.toast("El email ya esta ocupado", 2000, "red");
                    $scope.errors.email = true;
                } else{
                    Materialize.toast("Ha ocurrido un error. Intentelo más tarde", 2000, "red");
                }
            });
        }else{
            Materialize.toast("Hay errores en los campos.", 2000, "red");
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
                Materialize.toast("La dirección no es valida", 2000, "red");
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


