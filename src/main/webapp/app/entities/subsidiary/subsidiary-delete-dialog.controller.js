(function() {
    'use strict';

    angular
        .module('awesomoApp')
        .controller('SubsidiaryDeleteController',SubsidiaryDeleteController);

    SubsidiaryDeleteController.$inject = ['$uibModalInstance', 'entity', 'Subsidiary'];

    function SubsidiaryDeleteController($uibModalInstance, entity, Subsidiary) {
        var vm = this;

        vm.subsidiary = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Subsidiary.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
