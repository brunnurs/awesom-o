(function () {
    'use strict';

    angular
        .module('awesomoApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'User'];

    function HomeController($scope, Principal, LoginService, $state, User) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;

        vm.newWorkLog = function () {
            return {
                approved: false,
                workFrom: null,
                workTo: null,
                id: null,
                user: null,
                project: null
            }
        };

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
