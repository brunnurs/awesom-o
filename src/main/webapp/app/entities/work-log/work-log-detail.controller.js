(function() {
    'use strict';

    angular
        .module('awesomoApp')
        .controller('WorkLogDetailController', WorkLogDetailController);

    WorkLogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WorkLog', 'User', 'Project'];

    function WorkLogDetailController($scope, $rootScope, $stateParams, previousState, entity, WorkLog, User, Project) {
        var vm = this;

        vm.workLog = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('awesomoApp:workLogUpdate', function(event, result) {
            vm.workLog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
