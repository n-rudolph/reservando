
var app = angular.module("reservandoApp", []);

app.controller("loginCtrl", function ($scope, $http) {

    $(document).ready(function(){
        $('.tooltipped').tooltip({delay: 50});
    });

    $scope.errors = {
        address: false
    };
    $scope.address = {};

    $scope.bigImageHolder = true;
    $scope.bigContentHolder = false;

    $scope.initTabs = function(){
        $('ul.tabs').tabs({swipeable: true, responsiveThreshold: 1000});
    };

    $scope.bigContent = function(){
        $scope.bigImageHolder = false;
        $scope.bigContentHolder = true;
    };
    $scope.smallContent = function(){
        $scope.bigImageHolder = true;
        $scope.bigContentHolder = false;
    };

    //Register functionality
    $scope.userType = false;

    $scope.validatePassword = function(){
        $scope.pTouched = true;
        $scope.validPassword =  ($scope.password.length > 5 && $scope.password.match(/\d+/g) != null);
    };

    $scope.passwordValidation = function(){
       $scope.p2Touched = true;
       $scope.validPassword2 = $scope.password == $scope.password2;
    };

    $scope.register = function(){
        $scope.address.addressString = $("#address").val();
        $scope.geocodeAddress()
    };

    $scope.registerUser = function(){
        if ($scope.validateRegister()){
            var data = {
                firstName : $scope.firstName,
                lastName : $scope.lastName,
                email : $scope.email,
                password : $scope.password,
                address : $scope.address,
                userType : $scope.userType == undefined ? false : $scope.userType
            };
            $http.post("/register", data).then(function(response) {
                window.location.href = response.data;
            },function(response){
                Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 3000, "red");
            });
        }
    };

    $scope.validateRegister = function(){
        var error = false;
       if (!$scope.firstName){
           $scope.firstNameError = true;
           error = true;
       }
       if (!$scope.lastName){
            $scope.lastNameError = true;
            error = true;
       }
       if (!$scope.email){
           $scope.emailError = true;
           error = true;
       }
       if (!$scope.validPassword)
           error = true;
       if (!$scope.validPassword2)
           error = true;
       if (!$scope.address) {
           $scope.addressError = true;
           error = true;
       }
       return !error;
   };

    $scope.geocodeAddress = function() {
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({'address': $scope.address.addressString}, function(results, status) {
            if (status === 'OK') {
                $scope.address.lat = results[0].geometry.location.lat();
                $scope.address.lng = results[0].geometry.location.lng();
                $scope.registerUser();
            } else {
                $scope.error = true;
                Materialize.toast("La dirección no es valida", 2000, "red");
                return false;
            }
        });
    };
   
   //login functionality
    
    $scope.login = function(){
        var error = false;
        if (!$scope.loginEmail){
            $scope.loginEmailError = true;
            error = true;
        }
        if (!$scope.loginPassword){
            $scope.loginPasswordError = true;
            error = true;
        } 
        if (error) return;
        var data = {
            email: $scope.loginEmail,
            password: $scope.loginPassword
        };
        $http.post("/login", data).then(function(response){
            window.location.href = response.data;
        }, function(response){
            Materialize.toast(response.data, 3000, "red");
        });
            
    }
});
