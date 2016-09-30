(function() {
    'use strict';
    angular
        .module('awesomoApp')
        .factory('Subsidiary', Subsidiary);

    Subsidiary.$inject = ['$resource'];

    function Subsidiary ($resource) {
        var resourceUrl =  'api/subsidiaries/:id';

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
            'update': { method:'PUT' },
            'getByParentId' : {
                method: 'GET',
                url: 'api/subsidiaries/parent/:parentId',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            }
        });
    }
})();
