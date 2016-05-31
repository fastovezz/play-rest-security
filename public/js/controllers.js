/*global define */

'use strict';

define(['angular'], function (angular) {



    /* Controllers */
    var controllers = {};

    controllers.LoginCtrl = function ($scope, $http, $window, $location) {

        $scope.login = function () {
            $http.post('/login', {emailAddress: $scope.email, password: $scope.password})
                .success(function (data) {
                    $window.localStorage.clear();
                    $window.localStorage.setItem('authToken', data.authToken);
                    $window.localStorage.setItem('email', $scope.email);
                    $location.path('/');
                    console.log('logged in!');
                })
                .error(function (error) {
                    console.error('error logging in!');
                });

        };
    };
    controllers.LoginCtrl.$inject = ['$scope', '$http', '$window', '$location'];

    controllers.MainCtrl = function ($scope, $http, $window, $location, FavListsService) {
        FavListsService.getAllFavLists().then(
            function (data) {
                $scope.favlists = data.data;
            },
            function (error) {
                console.error(error);
            });

        $scope.createFavList = function () {
            $http.post('/favlists', {name: $scope.newFavListName})
                .success(function (data) {
                    $scope.favlists = $scope.favlists || [];
                    $scope.favlists.push(data);
                })
                .error(function (error) {
                    console.error(error);
                });
        };

        $scope.deleteList = function (list) {
            $http.delete('/favlists/' + list.id)
                .success(function (deletedId) {
                    var indToDelete = -1;
                    for (var i = 0; i < $scope.favlists.length; i++) {
                        if ($scope.favlists[i].id == deletedId) {
                            indToDelete = i;
                            break;
                        }
                    }
                    if (indToDelete >= 0) {
                        $scope.favlists.splice(indToDelete, 1);
                        delete $scope.movies;
                    }
                    console.log(deletedId);
                })
                .error(function (error) {
                    console.error(error);
                });
        }

        $scope.getMoviesByListId = function (list) {
            $http.get('/favlists/' + list.id)
                .success(function (data) {
                    $scope.movies = data;
                    $scope.selectedList = list;
                    console.log(data);
                })
                .error(function (error) {
                    console.log(error);

                });
        };

        $scope.deleteMovieFromList = function (movie) {
            $http.delete('/favlists/' + $scope.selectedList.id + '/movies/' + movie.id)
                .success(function (data) {
                    var indToDelete = -1;
                    for (var i = 0; i < $scope.movies.length; i++) {
                        if ($scope.movies[i].id == movie.id) {
                            indToDelete = i;
                            break;
                        }
                    }
                    if (indToDelete >= 0) {
                        $scope.movies.splice(indToDelete, 1);
                    }
                    console.log(data);
                })
                .error(function (error) {
                    console.error(error);
                });
        }
    };
    controllers.MainCtrl.$inject = ['$scope', '$http', '$window', '$location', 'FavListsService'];

    controllers.SearchCtrl = function ($scope, $http, FavListsService) {
        $scope.search = function () {
            $http.get('/movies', {
                    params: {
                        searchText: $scope.searchText
                    }
                })
                .success(function (data) {
                    $scope.movies = data['results'];
                    $scope.movies.sort(function (movie1, movie2) {
                        return movie1.release_date.localeCompare(movie2.release_date);
                    });

                    if (!$scope.favLists) {
                        FavListsService.getAllFavLists().then(
                            function (data) {
                                $scope.favLists = data.data;
                            },
                            function (error) {
                                console.error(error);
                            }
                        );
                    }
                })
                .error(function (error) {
                    console.error(error);
                })
        };

        $scope.model = {};

        $scope.onChange = function () {
            console.log($scope.model.favListId);
        };

        $scope.addMovieToList = function (movie) {
            $http.post('/favlists/' + $scope.model.favListId + '/movies', {
                    title: movie.title,
                    thumbnailUrl: movie.poster_path
                        ? "http://image.tmdb.org/t/p/w300" + movie.poster_path
                        : null,
                    themoviedbId: movie.id

                })
                .success(function (data) {
                    console.log(data);
                })
                .error(function (error) {
                    console.error(error);
                });
        }
    };
    controllers.SearchCtrl.$inject = ['$scope', '$http', 'FavListsService'];

    return controllers;

});