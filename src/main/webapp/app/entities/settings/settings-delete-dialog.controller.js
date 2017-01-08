(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('SettingsDeleteController',SettingsDeleteController);

    SettingsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Settings'];

    function SettingsDeleteController($uibModalInstance, entity, Settings) {
        var vm = this;

        vm.settings = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Settings.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
