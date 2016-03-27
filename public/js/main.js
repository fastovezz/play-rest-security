/*global require, requirejs */

'use strict';

requirejs.config({
    paths: {
        'angular': ['../lib/angularjs/angular'],
        'angular-route': ['../lib/angularjs/angular-route']
    },
    shim: {
        'angular': {
            exports: 'angular'
        },
        'angular-route': {
            deps: ['angular'],
            exports: 'angular'
        }
    }
});

require(['angular', './controllers', './directives', './filters', './services', 'angular-route'],
    function (angular, controllers) {

        // Declare app level module which depends on filters, and services

        angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives', 'ngRoute'])
            .factory('authInterceptor', ['$window', '$q', '$location', function ($window, $q, $location) {
                return {
                    // Add authorization authToken to headers
                    request: function (config) {
                        config.headers = config.headers || {};
                        var authToken = $window.localStorage.getItem('authToken');
                        console.log('interceptor: ' + authToken);
                        if (authToken) {
                            config.headers['X-AUTH-TOKEN'] = authToken;
                        }  else if ($location.path() != '/login' && $location.path() != '/signup') {
                            $location.path('/login');
                        }
                        return config;
                    },
                    responseError: function (rejection) {
                        if (rejection.status === 401) {
                            $location.path('/login');
                        }
                        return $q.reject(rejection);

                    }
                };
            }])
            .config(['$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {
                $routeProvider.when('/', {
                    templateUrl: 'partials/main.html',
                    controller: controllers.MainCtrl,
                    access: {
                        requiresLogin: true
                    }
                });

                $routeProvider.when('/login', {
                    templateUrl: 'partials/login.html',
                    controller: controllers.LoginCtrl
                });

                $routeProvider.when('/signup', {
                    templateUrl: 'partials/signup.html',
                    controller: controllers.LoginCtrl
                });

                $routeProvider.when('/search', {
                    templateUrl: 'partials/search.html',
                    controller: controllers.SearchCtrl,
                    access: {
                        requiresLogin: true
                    }
                });

                $routeProvider.otherwise({redirectTo: '/'});

                $httpProvider.interceptors.push('authInterceptor');
            }]);

        var MenuCtrl = function ($scope, $http, $window, $location) {
            $scope.logout = function () {
                $http.post('/logout')
                    .success(function (data) {
                        $window.localStorage.clear();
                        delete $scope.isLoggedIn
                        $location.path("/login");
                        console.log("logged out!")

                    })
                    .error(function (error) {
                        console.error("error logging out!")
                    });
            }

            $scope.$watch(function () {
                    return $window.localStorage.getItem('email');
                },
                function (newVal) {
                    if (newVal) {
                        $scope.email = newVal;
                    }
                });

            $scope.$watch(function () {
                    return $window.localStorage.getItem('authToken');
                },
                function (newVal) {
                    if (!newVal) {
                        $scope.isLoggedIn = false;
                    } else {
                        $scope.isLoggedIn = true;
                    }
                });
        };
        MenuCtrl.$inject = ['$scope', '$http', '$window', '$location'];
        angular
            .module('myApp')
            .controller('MenuCtrl', MenuCtrl);

        angular.bootstrap(document, ['myApp']);

    });
