(function() {
    'use strict';

    angular
        .module('awesomoApp')
        .controller('WorkLogDialogController', WorkLogDialogController);

    WorkLogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WorkLog', 'User', 'Project'];

    function WorkLogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WorkLog, User, Project) {
        var vm = this;

        vm.workLog = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.projects = Project.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.workLog.id !== null) {
                WorkLog.update(vm.workLog, onSaveSuccess, onSaveError);
            } else {
                WorkLog.save(vm.workLog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('awesomoApp:workLogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.workFrom = false;
        vm.datePickerOpenStatus.workTo = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
