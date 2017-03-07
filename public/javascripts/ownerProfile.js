var app = angular.module("reservandoApp", []);

app.controller("ProfileCtrl", function ($scope, $http) {

    /*This load the current user data*/
    var loadUserData = function(){
        $http({
            method: 'GET',
            url: '/owner/profile/user'
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
            url: '/owner/changePassword',
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
            url: '/owner/changeBioInfo',
            data: data
        }).then(function () {
            Materialize.toast("Información actualizada", 3000, 'green');
        }, function (response) {
            Materialize.toast(response, 3000, 'red');
        })
    };

    $scope.changeAddress = function () {
        var data = {address: $scope.user.address};

        $http({
            method: 'POST',
            url: '/owner/changeAddress',
            data: data
        }).then(function () {
            Materialize.toast("Ubicación actualizada", 3000, 'green');
        }, function (response) {
            Materialize.toast(response, 3000, 'red');
        })
    };

    $scope.changePhoto = function () {

    };

    $scope.deleteAccount = function () {
      /* Ask if it is necessary to request the username and the password for deleting the account.*/
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
});
