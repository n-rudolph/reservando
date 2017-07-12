var app = angular.module("reservandoApp");

app.controller("langController", ['$scope', '$http', '$window', function ($scope, $http, $window) {

    $scope.changeToSpanish = function(){
        var postUrl = "/language/change";
        $http({
            method: 'POST',
            url: postUrl,
            data: {lang: "es"}
        }).success(function(){
            $window.location.reload();
        }).error(function (serverErrorResponse) {

        });
    };

    $scope.changeToEnglish = function(){
        var uploadUrl = "/language/change";
        $http({
            method: 'POST',
            url: uploadUrl,
            data: {lang: "en"}
        }).success(function(){
            $window.location.reload();
        }).error(function (serverErrorResponse) {

        });
    };


}]);