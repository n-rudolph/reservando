var app = angular.module("reservandoApp", ['ngRoute']);

app.directive("ngFileModel", [
    '$parse', function($parse) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                var isMultiple, model, modelSetter;
                model = $parse(attrs.ngFileModel);
                isMultiple = attrs.multiple;
                modelSetter = model.assign;
                element.bind('change', function() {
                    var values = [];
                    angular.forEach(element[0].files, function(item) {
                        var reader;
                        reader = new FileReader();
                        reader.onloadend = function() {
                            var value;
                            value = {
                                name: item.name,
                                src: reader.result,
                                size: item.size
                            };
                            if (values.length == 0)
                                values.push(value);
                            return scope.$apply();
                        };
                        if (item) {
                            reader.readAsDataURL(item);
                        }
                    });
                    scope.$apply(function() {
                        if (isMultiple) {
                            modelSetter(scope, values);
                        } else {
                            modelSetter(scope, values[0]);
                        }
                    });
                });
            }
        };
    }
]);