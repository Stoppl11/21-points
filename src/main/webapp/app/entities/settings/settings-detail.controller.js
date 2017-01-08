(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('SettingsDetailController', SettingsDetailController);

    SettingsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Settings', 'User'];

    function SettingsDetailController($scope, $rootScope, $stateParams, previousState, entity, Settings, User) {
        var vm = this;

        vm.settings = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('21PointsApp:settingsUpdate', function(event, result) {
            vm.settings = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
