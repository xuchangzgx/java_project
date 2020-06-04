customerApp.controller('listController',function($scope, $route,$http,data){
	$http({
		url:config.url+"/customer/listSample",           //请求的url路径
		method:"GET",    //GET/DELETE/HEAD/JSONP/POST/PUT
		//params:params ,   //转为  ?param1=xx1¶m2=xx2的形式
		data: {}        //包含了将被当做消息体发送给服务器的数据，通常在POST请求时使用
	}).success(function(response, status, header, config, statusText){
		// response     ---  响应体，即：要请求的数据,status       ---  HTTP状态码
		// header      ---  头信息,config       ---  用来生成原始请求的完整设置对象
		// statusText   ---  相应的HTTP状态文本
		//成功处理
		if (response.code == 10000){
			$scope.customers = response.data;
		}else{
			alert(response.message);
		}
	}).error(function(data,header,config,status){
		//错误处理
		alert("网络异常，请稍后再试");
	});
	$scope.name = "controller1";
	// console.info( data.itemName );
});