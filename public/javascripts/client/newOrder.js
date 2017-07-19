var app = angular.module("reservandoApp");
app.requires.push('ngMap');
app.requires.push('vsGoogleAutocomplete');

app.controller("NewOrderCtrl", function ($scope, $http, $window, $timeout) {

    $scope.orderObject = {
        menu: []
    };
    $scope.total = 0;
    $scope.coordinates = {};
    $scope.hasDiscount = false;
    $scope.discCode = "";
    $scope.discount = {};
    $scope.address = {};
    $scope.options = {
        componentRestrictions: { country: 'AR' }
    };

    $scope.getClient = function () {
        $http.get("/client/profile/user").then(function(response){
            $scope.client = response.data;
        })
    };
    $scope.getClient();

    $scope.getRestaurant = function(){
        var id = $window.location.href.split("id=")[1];
        $http.get("/restaurant/"+ id).then(
            function (response){
                $scope.restaurant = response.data;
                $scope.orderObject.dId = $scope.restaurant.id;
                //$scope.orderObject.address = $scope.restaurant.address.address;
                $scope.orderObject.address = $scope.client.address;
                $scope.coordinates.lat = $scope.client.address.lat;
                $scope.coordinates.lng = $scope.client.address.lng;
                $scope.getMenu();
            }
        );
    };
    $scope.getRestaurant();

    // Meal

    $scope.getMenu = function(){
        $http.get("/menu/"+$scope.restaurant.id).then(function(response){
            var data = response.data;
            for (var i =0; i< data.length; i++){
                $scope.orderObject.menu.push({
                    meal: data[i],
                    amount: 0
                });
            }
        });
    };

    // Address
    $scope.editAddress = false;

    $scope.toggleEditAddress = function(editAddress){
        $scope.editAddress = editAddress;
        if (!editAddress){
            $scope.orderObject.address = $scope.client.address;
            $scope.coordinates.lat = $scope.client.address.lat;
            $scope.coordinates.lng = $scope.client.address.lng;
        }
    };
    $scope.changeAddress = function(){
        $scope.orderObject.address.completeAddress = $("#address").val();
        $scope.editAddress = false;
    };

    $scope.calculateSubtotal = function(order){
        $scope.calculateTotal();
        return $scope.calculateSubtotal2(order);
    };
    $scope.calculateSubtotal2 = function(order){
        var amount = order.amount;
        if (amount == undefined)
            return 0;
        return amount * order.meal.price;
    };

    $scope.calculateTotal = function () {
      var total = 0;
      for (var i = 0; i < $scope.orderObject.menu.length; i++){
          total += $scope.calculateSubtotal2($scope.orderObject.menu[i]);
      }
      if ($scope.validCode && $scope.hasDiscount)
          total = total * (1 - ($scope.discount.discount / 100));
      $scope.total = total;
    };

    //geolocation

    $scope.getGeolocateAddress = function(){
        navigator.geolocation.getCurrentPosition($scope.successFN)
    };

    $scope.successFN = function(position){
        $scope.coordinates.lat = position.coords.latitude;
        $scope.coordinates.lng = position.coords.longitude;
        var geocoder = new google.maps.Geocoder;
        geocoder.geocode({'location': $scope.coordinates}, function(results, status){
            if (status === 'OK'){
                $scope.orderObject.address.completeAddress = results[0].formatted_address;
            }
        });
    };

    $scope.validateNewAddress = function(){
        $scope.orderObject.address = $('#address').val();
        $scope.geocodeAddress();

    };

    $scope.geocodeAddress = function() {
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({'address': $scope.orderObject.address}, function(results, status) {
            if (status === 'OK') {
                $scope.coordinates.lat = results[0].geometry.location.lat();
                $scope.coordinates.lng = results[0].geometry.location.lng();
            } else {
                var addressNotValid = Messages("error.message.geolocalization.address.not.valid");
                Materialize.toast(addressNotValid, 2000, "red");
                //Materialize.toast("La dirección no es valida", 2000, "red");
            }
        });
    };

    $scope.checkCode = function(){
        $scope.invalidCode = false;
        $scope.validCode = false;
        $http.get("/discount/"+$scope.discCode).then(function(response){
            $scope.discount = response.data;
            $scope.validCode = true;
            $scope.calculateTotal();
        }, function(responseError){
            Materialize.toast(responseError.data, 2000, "red");
            $scope.invalidCode = true;
            $scope.discount = {};
        });
    };
    $scope.resetCode = function(){
        $scope.invalidCode = false;
        $scope.validCode = false;
        $scope.discount = {};
    };

    $scope.saveOrder = function(){
        if ($scope.validCode && $scope.hasDiscount)
            $scope.orderObject.discountCode = $scope.discCode;
        else $scope.orderObject.discountCode = "";

        var dataObject = angular.copy($scope.orderObject);
        dataObject.address = dataObject.address.completeAddress;
        if ($scope.distance($scope.orderObject.address.lat, $scope.orderObject.address.lng, $scope.restaurant.address.lat, $scope.restaurant.address.lng) <= $scope.restaurant.radius){
            $http.post("/order", dataObject).then(function(response){
                var succesfull = Messages("success.message.order.placed");
                Materialize.toast(succesfull, 2000, "green");
                //Materialize.toast("El pedido se ha procesado con éxito.", 2000, "green");
                $timeout(function(){
                    $window.location.href = "/client/restaurant?id="+$scope.restaurant.id;
                }, 1000);
            }, function(){
                var error = Messages("error.message.error.occurs.try.later");
                Materialize.toast(error, 2000, "red");
                //Materialize.toast("Ha ocurrido un error. Intentelo más tarde");
            });
        } else {
            var error = Messages("delivery-to-far");
            Materialize.toast(error, 2000, "red");
        }

    };
    $scope.distance = function(lat1, lon1, lat2, lon2) {
        var p = 0.017453292519943295;    // Math.PI / 180
        var c = Math.cos;
        var a = 0.5 - c((lat2 - lat1) * p)/2 +
            c(lat1 * p) * c(lat2 * p) *
            (1 - c((lon2 - lon1) * p))/2;

        return 12742 * Math.asin(Math.sqrt(a));
    }

});


