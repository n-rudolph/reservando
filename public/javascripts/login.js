
var app = angular.module("reservandoApp");
app.requires.push('vsGoogleAutocomplete');

app.controller("loginCtrl", function ($scope, $http) {

    $scope.errors = {
        address: false
    };
    $scope.address = {};

    $scope.bigImageHolder = true;
    $scope.bigContentHolder = false;

    $scope.options = {
        componentRestrictions: { country: 'AR' }
    };

    //Register functionality
    $scope.userType = false;

    $scope.validatePassword = function(){
        $scope.pTouched = true;
        $scope.validPassword =  ($scope.password.length > 5);
    };

    $scope.passwordValidation = function(){
       $scope.p2Touched = true;
       $scope.validPassword2 = $scope.password == $scope.password2;
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
                Materialize.toast(response.data, 3000, "red");
                //Materialize.toast("Ha ocurrido un error. Intentelo más tarde.", 3000, "red");
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
            
    };

    //Assign the enter key to the action of login, when the cursor is on the input loginMail.
    document.getElementById("loginMail").addEventListener("keyup", function(event){
        event.preventDefault();
        if(event.keyCode === 13){
            $scope.login();
        }
    });

    //Assign the enter key to the action of login, when the cursor is on the input loginPassword.
    document.getElementById("loginPassword").addEventListener("keyup", function(event){
        event.preventDefault();
        if(event.keyCode === 13){
            $scope.login();
        }
    });
});
