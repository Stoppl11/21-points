(function() {
    'use strict';
    angular
        .module('21PointsApp')
        .factory('Settings', Settings);

    Settings.$inject = ['$resource'];

    function Settings ($resource) {
        var resourceUrl =  'api/settings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
