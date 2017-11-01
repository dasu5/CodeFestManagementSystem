(function() {
    'use strict';
    angular
        .module('codeFestManagementSystemV1App')
        .factory('ApplicationForm', ApplicationForm);

    ApplicationForm.$inject = ['$resource', 'DateUtils'];

    function ApplicationForm ($resource, DateUtils) {
        var resourceUrl =  'api/application-forms/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dob = DateUtils.convertLocalDateFromServer(data.dob);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dob = DateUtils.convertLocalDateToServer(copy.dob);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dob = DateUtils.convertLocalDateToServer(copy.dob);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
