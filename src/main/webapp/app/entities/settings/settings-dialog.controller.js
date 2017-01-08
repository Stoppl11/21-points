(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('SettingsDialogController', SettingsDialogController);

    SettingsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Settings', 'User'];

    function SettingsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Settings, User) {
        var vm = this;

        vm.settings = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.settings.id !== null) {
                Settings.update(vm.settings, onSaveSuccess, onSaveError);
            } else {
                Settings.save(vm.settings, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('21PointsApp:settingsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
