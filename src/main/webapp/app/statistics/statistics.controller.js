(function () {
    'use strict';

    angular
        .module('awesomoApp')
        .controller('StatisticsController', StatisticsController);

    StatisticsController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'User', 'Project','WorkLog'];

    function StatisticsController($scope, Principal, LoginService, $state , User, Project, WorkLog) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;

        vm.projects = Project.query();

        vm.newWorkLog = {
            approved: false,
            workFrom: null,
            workTo: null,
            id: null,
            user: null,
            project: null,
            timeDiff : function () {
                return ((this.workTo - this.workFrom) / 3600000).toFixed(2);
            }
        };

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        function save () {
            vm.isSaving = true;
            WorkLog.save(vm.newWorkLog, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }




        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;

                User.get({login:vm.account.login},function (user) {
                    vm.newWorkLog.user = user;
                });
            });
        }


        function register() {
            $state.go('register');
        }
    }
})();
