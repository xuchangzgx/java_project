customerApp.config(['$routeProvider',function($routeProvider){
    $routeProvider 
        .when('/list',{templateUrl:'views/list.html', controller:'listController'})
		.when('/detail',{templateUrl:'views/detail.html', controller:'detailController'})
		.otherwise({
			redirectTo: '/list'
		})
}]);