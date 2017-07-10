var app = angular.module("reservandoApp", []);

app.controller("languageCtrl", function ($scope, $http) {

    $scope.changeToSpanish = function(){
        console.log("OK !");
    };

    $scope.changeToEnglish = function(){
        console.log("OK !");
    };

});