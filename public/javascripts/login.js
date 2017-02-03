
var app = angular.module("reservandoApp", []);

app.controller("loginCtrl", function ($scope, $http) {

    $(document).ready(function(){
        $('.tooltipped').tooltip({delay: 50});
        $('#tabs').tabs({swipeable: true, responsiveThreshold: 1000});
    });

    //Register functionality
    $scope.userType = false;

    $scope.validatePassword = function(){
        $scope.pTouched = true;
        $scope.validPassword =  ($scope.password.length > 6 && $scope.password.match(/\d+/g) != null);
    };

    $scope.passwordValidation = function(){
       $scope.p2Touched = true;
       $scope.validPassword2 = $scope.password == $scope.password2;
    };

    $scope.register = function(){
       $scope.address = $("#address").val();

       if ($scope.validateRegister()){
           var data = {
               firstName : $scope.firstName,
               lastName : $scope.lastName,
               email : $scope.email,
               password : $scope.password,
               address : $scope.address,
               userType : $scope.userType
           };
           $http.post("/register", data).then(function(response) {
               Materialize.toast("Se ha registrado con éxito", 3000, "green");
               $scope.firstName = "";
               $scope.lastName = "";
               $scope.email = "";
               $scope.password = "";
               $scope.password2 = "";
               $scope.address = "";
               $scope.userType = false;
               $scope.pTouched = false;
               $scope.p2Touched = false;
               $("#address").val("")
           },function(response){
               Materialize.toast(response.data, 3000, "red");
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
   }
   
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
