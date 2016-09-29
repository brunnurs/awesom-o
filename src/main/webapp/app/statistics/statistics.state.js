(function() {
    'use strict';

    angular
        .module('awesomoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('statistics', {
            parent: 'app',
            url: '/statistics',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/statistics/statistics.html',
                    controller: 'StatisticsController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();
