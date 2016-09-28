(function() {
    'use strict';

    angular
        .module('awesomoApp')
        .controller('SubsidiaryDetailController', SubsidiaryDetailController);

    SubsidiaryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Subsidiary', 'User'];

    function SubsidiaryDetailController($scope, $rootScope, $stateParams, previousState, entity, Subsidiary, User) {
        var vm = this;

        vm.subsidiary = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('awesomoApp:subsidiaryUpdate', function(event, result) {
            vm.subsidiary = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
