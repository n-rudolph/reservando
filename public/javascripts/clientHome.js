/**
 * Created by Gustavo on 3/6/17.
 */
var app = angular.module("reservandoApp");

app.controller("ClientHomeCtrl",['$scope', '$http', function($scope, $http){
    this.results = results;

}]);

var results = [
    {
        name: "Result1",
        localization: "Loc.1",
        description: "description1"
    },
    {
        name: "Result2",
        localization: "Loc.2",
        description: "description2"
    },
    {
        name: "Result3",
        localization: "Loc.3",
        description: "description3"
    },
    {
        name: "Result4",
        localization: "Loc.4",
        description: "description4"
    },
    {
        name: "Result5",
        localization: "Loc.5",
        description: "description5"
    }
];