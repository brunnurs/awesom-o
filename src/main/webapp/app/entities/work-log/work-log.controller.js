(function() {
    'use strict';

    angular
        .module('awesomoApp')
        .controller('WorkLogController', WorkLogController);

    WorkLogController.$inject = ['$scope', '$state', 'WorkLog'];

    function WorkLogController ($scope, $state, WorkLog) {
        var vm = this;
        
        vm.workLogs = [];

        loadAll();

        function loadAll() {
            WorkLog.query(function(result) {
                vm.workLogs = result;
            });
        }
    }
})();
