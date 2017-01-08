(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .factory('SettingsSearch', SettingsSearch);

    SettingsSearch.$inject = ['$resource'];

    function SettingsSearch($resource) {
        var resourceUrl =  'api/_search/settings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
