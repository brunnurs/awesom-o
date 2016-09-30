(function () {
    'use strict';

    angular
        .module('awesomoApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'User', 'Project','WorkLog','Subsidiary'];

    function HomeController($scope, Principal, LoginService, $state , User, Project, WorkLog, Subsidiary) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.success = null;

        vm.newWorkLog = createEmptyWorkLog();

        vm.subsidiary = null;
        vm.user = null;
        vm.useSubsidiary = false;

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        function save () {
            if(vm.useSubsidiary) {
                vm.newWorkLog.user = vm.subsidiary.child;
            } else {
                vm.newWorkLog.user = vm.user;
            }

            vm.isSaving = true;
            WorkLog.save(vm.newWorkLog, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            vm.success = 'ok';
        }

        function onSaveError () {
            vm.isSaving = false;
            vm.success = null;
        }


        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;

                if(vm.isAuthenticated) {
                    User.get({login:vm.account.login},function (user) {
                        vm.user = user;

                        Subsidiary.getByParentId({parentId:user.id}, function (subsidiary) {
                            vm.subsidiary = subsidiary;
                        })
                    });

                    vm.projects = Project.query();
                }
            });
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

        function register() {
            $state.go('register');
        }
    }
})();
