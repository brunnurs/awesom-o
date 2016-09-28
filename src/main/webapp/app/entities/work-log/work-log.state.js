(function() {
    'use strict';

    angular
        .module('awesomoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('work-log', {
            parent: 'entity',
            url: '/work-log',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WorkLogs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-log/work-logs.html',
                    controller: 'WorkLogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('work-log-detail', {
            parent: 'entity',
            url: '/work-log/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WorkLog'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-log/work-log-detail.html',
                    controller: 'WorkLogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'WorkLog', function($stateParams, WorkLog) {
                    return WorkLog.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'work-log',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('work-log-detail.edit', {
            parent: 'work-log-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-log/work-log-dialog.html',
                    controller: 'WorkLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WorkLog', function(WorkLog) {
                            return WorkLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('work-log.new', {
            parent: 'work-log',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-log/work-log-dialog.html',
                    controller: 'WorkLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                approved: false,
                                workFrom: null,
                                workTo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('work-log', null, { reload: 'work-log' });
                }, function() {
                    $state.go('work-log');
                });
            }]
        })
        .state('work-log.edit', {
            parent: 'work-log',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-log/work-log-dialog.html',
                    controller: 'WorkLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WorkLog', function(WorkLog) {
                            return WorkLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('work-log', null, { reload: 'work-log' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('work-log.delete', {
            parent: 'work-log',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-log/work-log-delete-dialog.html',
                    controller: 'WorkLogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WorkLog', function(WorkLog) {
                            return WorkLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('work-log', null, { reload: 'work-log' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
