(function() {
    'use strict';

    angular
        .module('awesomoApp')
        .controller('SubsidiaryDialogController', SubsidiaryDialogController);

    SubsidiaryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Subsidiary', 'User'];

    function SubsidiaryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Subsidiary, User) {
        var vm = this;

        vm.subsidiary = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.subsidiary.id !== null) {
                Subsidiary.update(vm.subsidiary, onSaveSuccess, onSaveError);
            } else {
                Subsidiary.save(vm.subsidiary, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('awesomoApp:subsidiaryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
