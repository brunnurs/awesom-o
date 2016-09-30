(function () {
    'use strict';

    angular
        .module('awesomoApp')
        .controller('StatisticsController', StatisticsController);

    StatisticsController.$inject = ['$scope', '$state' ,'WorkLog'];

    function StatisticsController($scope, $state ,WorkLog) {
        var vm = this;

        vm.groupedWorkLogs = [];

        loadAll();

        function loadAll() {
            WorkLog.query(function(result) {
                groupWorkLogs(result);
            });

            //TODO: use a nice helper library (lodash/undersocre.cs) to solve that problem in a more functional way (groupBy)
            var groupWorkLogs = function (workLogs) {
                workLogs.forEach(function (workLog) {
                    var group = null;

                    var existingGroup = vm.groupedWorkLogs.filter(function (group) {
                        return group.project.id === workLog.project.id && group.user.id === workLog.user.id;
                    });

                    if (existingGroup.length > 0) {
                        group = existingGroup[0];
                    } else {
                        group = {
                            project: workLog.project,
                            user: workLog.user,
                            workHours : 0.0
                        };

                        vm.groupedWorkLogs.push(group);
                    }

                    //TODO: use the moment.js library to get the time span easier and saver
                    group.workHours = parseFloat(group.workHours) + parseFloat(calculateTimeDiff(new Date(workLog.workFrom), new Date(workLog.workTo)));

                    function calculateTimeDiff(workFrom, workTo) {
                        return ((workTo - workFrom) / 3600000).toFixed(2);
                    }
                });
            };
        }


    }
})();
