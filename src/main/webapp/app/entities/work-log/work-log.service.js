(function() {
    'use strict';
    angular
        .module('awesomoApp')
        .factory('WorkLog', WorkLog);

    WorkLog.$inject = ['$resource', 'DateUtils'];

    function WorkLog ($resource, DateUtils) {
        var resourceUrl =  'api/work-logs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.workFrom = DateUtils.convertDateTimeFromServer(data.workFrom);
                        data.workTo = DateUtils.convertDateTimeFromServer(data.workTo);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
