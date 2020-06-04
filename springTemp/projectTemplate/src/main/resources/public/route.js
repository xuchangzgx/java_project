app.config(function($urlRouterProvider, $stateProvider) {
	$urlRouterProvider.otherwise("/main");
	$stateProvider.state("main", {
			url: "/main",
			templateUrl: "index/main.html",
			controller: 'indexController'
		});
})

app.controller('indexController',function($scope, $location, $rootScope, appData){
	appData = {};
	$scope.goToApp = function(address) {
		if(address.indexOf("http") != -1) {
			window.location.href = address;
		} else {
			window.location.href = address;
		}
	};
})