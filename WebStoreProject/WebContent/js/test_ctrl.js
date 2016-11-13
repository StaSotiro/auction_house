


   authentication.controller("TestCtrl",['$scope','$state', '$rootScope', '$timeout', '$http','AuthenticationService',
      function ($scope, $state, $rootScope, $timeout, $http, AuthenticationService) {
	   $scope.searchstate = "Search";
	   $scope.search   = '';
	   $scope.message = "aeeee";
	   $scope.sortType='id';
	   $scope.sortReverse  = false;
	   $scope.filesChanged=function(elm){
		   $scope.files=elm.files;
		   $scope.$apply();
	   }
	   $scope.uploadxml= function () {
			 $scope.message="aa";
			 AuthenticationService.upload_xml( $scope.files, function (response) {   
			    	if (response.HTTP_CODE==200) {
				    	$scope.message= "File uploaded successfully.";
				       	  $timeout(function() {
						    	$state.go($state.current, {}, {reload: true});
				            }, 500);


			        } else {
			    		$scope.message = "The file you entered is not valid or an error occured";
			        }
			    });
			 
		};
	   $http.get('http://localhost:8080/WebStoreProject/rest/users/allusers').
	  success(function(data) {
	        $scope.users = data;
	    });
	   
 	  $scope.validateuser= function (userid) {
			$scope.searchstate = "Searching";
		    AuthenticationService.validateuser(userid, function (response) {   
   				$scope.searchstate = "Search";
		    	if (response.HTTP_CODE==200) {
			    	$scope.message= "User succesfully validated.";
			       	  $timeout(function() {
					    	$state.go($state.current, {}, {reload: true});
			            }, 500);


		        } else {
		    		$scope.message = "The User ID you entered is not valid or an unknown error occured";
		            $scope.error = response.message;
			       	  $timeout(function() {
					    	$state.go($state.current, {}, {reload: true});
			            }, 500);

		        }
		    });
		}; 
		
   }]);


   test.controller("TestCtrl3",['$scope','$state','$timeout',
                                function ($scope, $state, $timeout) {
 						    	  $scope.error = "You are not authorized to view this page. You will be redirected to the home page.";
 						       	  $timeout(function() {
 						       		  $state.go('home-guest');
 						            }, 3000);
                             }]);
   

   

