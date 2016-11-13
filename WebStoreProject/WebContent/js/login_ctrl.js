


authentication.controller('logctrl',
    ['$scope', '$rootScope', '$state','$timeout', 'AuthenticationService', 
    function ($scope, $rootScope, $state, $timeout, AuthenticationService) {
	    $scope.searchstate = "Search";
        $scope.login = function () {
			$scope.searchstate = "Searching";
            $scope.dataLoading = true;
            AuthenticationService.Login($scope.username, $scope.password, function (response) {
   				$scope.searchstate = "Search";
            	if (response.HTTP_CODE==200) {
                    AuthenticationService.SetCredentials($scope.username, $scope.password, response.USR_ID);
			    	  $scope.verify = "You have been logged in. You will be redirected automatically.";
			    	  localStorage.setItem("notifications", response.UNREAD);
			    	  $rootScope.show_res='0';
			    	  $timeout(function() {
			       		  $state.go('home-user');
			            }, 1500);

                } else {
            		$scope.verify = "Error";
                    $scope.error = response.message;
                    $scope.dataLoading = false;
                }
            });
        };
    }]);



authentication.controller('logctrl2',
    ['$scope', '$rootScope', '$state','$timeout', 'AuthenticationService',
    function ($scope, $rootScope, $state, $timeout, AuthenticationService) {

    	
        $scope.logout = function () {
            AuthenticationService.ClearCredentials();
        	$scope.logmsg2 = "You have been logged out. You will be redirected to the home page!";
        	$rootScope.myVar='0';
        	$timeout(function() {
	              $state.go('home-guest');
	            }, 1500);
        };

    }]);

authentication.controller('addctrl',
		  ['$scope', '$rootScope', 'AuthenticationService', '$state', '$timeout',
             function ($scope, $rootScope, AuthenticationService, $state, $timeout) {
 			  $scope.searchstate = "Search";
 			  $scope.infomessage = "";
 			  $scope.categories2 = "";
 			  $scope.date = new Date();
 			  $scope.cur_year= $scope.date.getFullYear();
 			  $scope.cur_month= $scope.date.getMonth() + 1;
 			  $scope.cur_day= $scope.date.getDate();
		      $scope.getLocation = function () {
		        	if (navigator.geolocation) navigator.geolocation.getCurrentPosition(onPositionUpdate);
		        	 
		        	function onPositionUpdate(position) {
		        		$scope.infomessage = "Location obtained! You can now complete the Add Auction Form!";
		        	    $scope.latitude = position.coords.latitude;
		        	    $scope.longitude = position.coords.longitude;
		        	    $scope.$apply();
	
		        	}
		       };

 			  
 			  
 			  
          	  $scope.add = function () {
     				$scope.ends = $scope.year + '-' + $scope.month + '-' + $scope.day;

	   				$scope.searchstate = "Searching";

          		    AuthenticationService.add($scope.name, $scope.buy_price, $scope.first_bid, $scope.ends, $scope.country,$scope.latitude,$scope.longitude,$scope.images, $scope.categories1, $scope.categories2,$rootScope.globals.currentUser.userid, function (response) {
    	   				$scope.searchstate = "Search";
          		    	if (response.HTTP_CODE==200) {
          			    	$scope.message3 = "Auction added successfully. You will be redirected to the home page";
          		        	$timeout(function() {
          		              $state.go('home-user');
          		            }, 1500);

          		        } else {
          		    		$scope.message3 = "Error";
          		            $scope.error = response.message;

          		        }
          		    });
          		};
          		
    	        $scope.list_categories = function () {
    	            AuthenticationService.list_categories(function (response) {
    	            		$scope.categlist = response;
    	            });
    	        };
          		
          		
          }]);


authentication.controller('logctrl3',
	    ['$scope', '$rootScope', '$state','$timeout', 'AuthenticationService', 
	    function ($scope, $rootScope, $state, $timeout, AuthenticationService) {
			$scope.searchstate = "Search";
			

	        $scope.getLocation = function () {
	        	if (navigator.geolocation) navigator.geolocation.getCurrentPosition(onPositionUpdate);
	        	 
	        	function onPositionUpdate(position) {
	        		$scope.infomessage = "Location obtained! You can now complete the Registration Form!";
	        	    $scope.latitude = position.coords.latitude;
	        	    $scope.longitude = position.coords.longitude;
	        	    $scope.$apply();

	        	}
	        };
			

	       
			
	        $scope.register = function () {
   				$scope.searchstate = "Searching";
   				$scope.birth = $scope.year + '-' + $scope.month + '-' + $scope.day;
	            AuthenticationService.register($scope.username, $scope.password, $scope.address, $scope.cell, $scope.latitude, $scope.longitude, $scope.country, $scope.birth, $scope.email, $scope.fname, $scope.lname, $scope.phone, $scope.tax, function (response) {
	   				$scope.searchstate = "Search";
	            	if (response.HTTP_CODE==200) {
				    	  $scope.regmsg = "Your account has been created. You will be redirected automatically.";			    	  
				       	  $timeout(function() {
				       		  $state.go('home-guest');
				            }, 1000);

	                } else {
	            		$scope.verify = "Error";
	                    $scope.error = response.message;
	                    $scope.dataLoading = false;
	                }
	            });
	        };
	        
	    }]);





authentication.directive('wjValidationError', function () {
	  return {
	    require: 'ngModel',
	    link: function (scope, elm, attrs, ctl) {
	      scope.$watch(attrs['wjValidationError'], function (errorMsg) {
	        elm[0].setCustomValidity(errorMsg);
	        ctl.$setValidity('wjValidationError', errorMsg ? false : true);
	      });
	    }
	  };
	});












app.controller('lalactrl', function($scope) {
	  $scope.map = {
			    center: [40.7, -74]
			  }
			  $scope.marker = {
			    position: [40.7, -74]
			  }
			});







