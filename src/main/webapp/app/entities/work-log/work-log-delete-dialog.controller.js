(function() {
    'use strict';

    angular
        .module('awesomoApp')
        .controller('WorkLogDeleteController',WorkLogDeleteController);

    WorkLogDeleteController.$inject = ['$uibModalInstance', 'entity', 'WorkLog'];

    function WorkLogDeleteController($uibModalInstance, entity, WorkLog) {
        var vm = this;

        vm.workLog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WorkLog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
