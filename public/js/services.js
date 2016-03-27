/*global define */

'use strict';

define(['angular'], function (angular) {

    /* Services */

// Demonstrate how to register services
// In this case it is a simple value service.
    angular.module('myApp.services', [])
        .value('version', '0.1')
        .factory("FavListsService", ['$http', function ($http) {
            var api = {};

            api.getAllFavLists = function () {
                return $http.get('/favlists')
                    .success(function (data) {
                        return data.data;
                    })
            };

            return api;
        }]);

});