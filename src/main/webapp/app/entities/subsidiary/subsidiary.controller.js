(function() {
    'use strict';

    angular
        .module('awesomoApp')
        .controller('SubsidiaryController', SubsidiaryController);

    SubsidiaryController.$inject = ['$scope', '$state', 'Subsidiary'];

    function SubsidiaryController ($scope, $state, Subsidiary) {
        var vm = this;
        
        vm.subsidiaries = [];

        loadAll();

        function loadAll() {
            Subsidiary.query(function(result) {
                vm.subsidiaries = result;
            });
        }
    }
})();
