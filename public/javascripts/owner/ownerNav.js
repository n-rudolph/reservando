var app = angular.module("reservandoApp");

app.controller("OwnerNavCtrl", function ($scope, $http) {

    var loadUserData = function(){
        $http({
            method: 'GET',
            url: '/owner/profile/user'
        }).success(function(data) {
            $scope.user = data;
        });
    };
    loadUserData();


});