(function () {
    'use strict';

    angular.module('awesomoApp').component('timeTracking', {
        templateUrl: 'app/home/components/time-tracking.directive.html',
        bindings: {
            account: '='
        },
        controller: TimeTrackingController
    });

    TimeTrackingController.$inject = ['User', 'Project', 'WorkLog', 'Subsidiary'];

    function TimeTrackingController(User, Project, WorkLog, Subsidiary) {
        var vm = this;

        vm.success = false;
        vm.newWorkLog = createEmptyWorkLog();

        vm.subsidiary = null;
        vm.user = null;
        vm.useSubsidiary = false;

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        loadData();

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

        function save() {
            if (vm.useSubsidiary) {
                vm.newWorkLog.user = vm.subsidiary.child;
            } else {
                vm.newWorkLog.user = vm.user;
            }

            vm.isSaving = true;
            WorkLog.save(vm.newWorkLog, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess(result) {
            vm.isSaving = false;
            vm.success = true;
        }

        function onSaveError() {
            vm.isSaving = false;
            vm.success = false;
        }

        function loadData() {
            User.get({login: vm.account.login}, function (user) {
                vm.user = user;

                Subsidiary.getByParentId({parentId: user.id}, function (subsidiary) {
                    vm.subsidiary = subsidiary;
                })
            });

            vm.projects = Project.query();
        }

        function createEmptyWorkLog() {
            return {
                approved: false,
                workFrom: null,
                workTo: null,
                id: null,
                user: null,
                project: null,
                timeDiff: function () {
                    return ((this.workTo - this.workFrom) / 3600000).toFixed(2);
                }
            };
        }
    }
})();
