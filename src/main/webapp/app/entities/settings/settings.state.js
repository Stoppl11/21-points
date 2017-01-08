(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('settings', {
            parent: 'entity',
            url: '/settings',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: '21PointsApp.settings.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/settings/settings.html',
                    controller: 'SettingsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('settings');
                    $translatePartialLoader.addPart('units');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('settings-detail', {
            parent: 'entity',
            url: '/settings/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: '21PointsApp.settings.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/settings/settings-detail.html',
                    controller: 'SettingsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('settings');
                    $translatePartialLoader.addPart('units');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Settings', function($stateParams, Settings) {
                    return Settings.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'settings',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('settings-detail.edit', {
            parent: 'settings-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/settings/settings-dialog.html',
                    controller: 'SettingsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Settings', function(Settings) {
                            return Settings.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('settings.new', {
            parent: 'settings',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/settings/settings-dialog.html',
                    controller: 'SettingsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                weeklygoal: null,
                                weightuntits: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('settings', null, { reload: 'settings' });
                }, function() {
                    $state.go('settings');
                });
            }]
        })
        .state('settings.edit', {
            parent: 'settings',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/settings/settings-dialog.html',
                    controller: 'SettingsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Settings', function(Settings) {
                            return Settings.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('settings', null, { reload: 'settings' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('settings.delete', {
            parent: 'settings',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/settings/settings-delete-dialog.html',
                    controller: 'SettingsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Settings', function(Settings) {
                            return Settings.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('settings', null, { reload: 'settings' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
