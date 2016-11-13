

authentication.controller("HomePageCtrl",['$scope','$rootScope', '$http','AuthenticationService', '$state',
                                          function ($scope,$rootScope, $http, AuthenticationService, $state) {
	$scope.searchstate = "Search";
	$scope.testmsg="arxi";
	$scope.name2="";
	$scope.from2="0";
	$scope.to2="99999";	
	$scope.categ2="";
	$scope.asc2="ASC";
	$scope.sort2="end";
	$scope.limit2="100";
	$scope.search = function () {
		$scope.searchstate = "Searching";
		$scope.testmsg="mpike";
		AuthenticationService.search($scope.from2, $scope.to2, $scope.name2, $scope.categ2, $scope.asc2, $scope.sort2, $scope.limit2, function (response) {
			$scope.searchstate = "Search";
			$rootScope.search_results = response;
			if (response.HTTP_CODE==500) {
				$rootScope.search_results = "";
			}     	

			$scope.testmsg="doulepse";
			$state.go('search_results');

		});
	}; 


}]);



authentication.controller("SearchResCtrl",['$scope','$rootScope', '$http','AuthenticationService', '$state',

                                           function ($scope, $rootScope, $http, AuthenticationService, $state) {
	$scope.searchauc   = '';
	$scope.searchstate = "Search";
	$scope.sortType='id';
	$scope.sortReverse  = false;
	$scope.search_results= $rootScope.search_results;
	$scope.name2="";
	$scope.from2="0";
	$scope.to2="99999";	
	$scope.categ2="";
	$scope.asc2="ASC";
	$scope.sort2="end";
	$scope.limit2="100";

	$scope.list_categories = function () {
		AuthenticationService.list_categories(function (response) {
			$scope.categlist = response;
		});
	};
	$scope.auction_details = function (aucid,title) {
		AuthenticationService.auction_details(aucid, function (response) {
			$rootScope.titleauc = title;
			$rootScope.details = response;
			$state.go('auction_details');

		});
	}; 


	$scope.search_aucti = function () {
		$scope.searchstate = "Searching";

		AuthenticationService.search($scope.from2, $scope.to2, $scope.name2, $scope.categ2, $scope.asc2, $scope.sort2, $scope.limit2, function (response) {
			$scope.searchstate = "Search";
			$rootScope.show_res='1';
			if (response.HTTP_CODE!=500) {
				$rootScope.search_results = response;
				$scope.search_results = $rootScope.search_results;
			}  
			else {
				$rootScope.search_results = "";
				$scope.search_results ="";
			}   

		});
	}; 

	$scope.search = function () {
		if($scope.to2==""){
			$scope.to2="99999";
		}
		if($scope.from2==""){
			$scope.from2="0";
		}

		$scope.searchstate = "Searching";
		AuthenticationService.search($scope.from2, $scope.to2, $scope.name2, $scope.categ2, $scope.asc2, $scope.sort2, $scope.limit2, function (response) {
			$scope.searchstate = "Search";
			$rootScope.show_res='1';
			if (response.HTTP_CODE!=500) {
				$rootScope.search_results = response;
				$scope.search_results = $rootScope.search_results;
			}  
			else {
				$rootScope.search_results = "";
				$scope.search_results ="";
			}   

		});
	}; 
}]);




authentication.controller("AucDetCtrl",['$scope','$rootScope', '$http','AuthenticationService', '$state','$timeout',
                                        function ($scope,$rootScope, $http, AuthenticationService, $state, $timeout) {
	$scope.bid = "";
	$scope.myImg ="pic_angular.jpg";
	$scope.searchstate = "Search";
	$scope.details = $rootScope.details;
	$scope.categories= $scope.details.categories;
	$scope.auc_title= $rootScope.titleauc;

	$scope.makebid = function (auctionid, bid) {
		if (confirm("You are about to bid on this auction!! If you win the auction, you are obliged to pay. Confirm?") == true) {
			$scope.searchstate = "Searching";
			AuthenticationService.makebid(auctionid, bid, $rootScope.globals.currentUser.userid, function (response) {
				$scope.searchstate = "Search";
				$scope.data = response.MSG;
				if (response.HTTP_CODE==200) {
					$scope.message23= "Bidded Successfully";
					$timeout(function() {
						$state.go('auction_details');
					}, 1500);
				} else {
					$scope.message23 = "An error occured. Your bid was not successfull";
					$timeout(function() {
						$state.go('auction_details');
					}, 1500);
				}
			});
		}
		else{
			$scope.message1="Deletion Cancelled"
		}

	}; 

	$scope.downloadxml = function(auc_name){
		$scope.message1="aaaa";
		AuthenticationService.download_xml(auc_name, function (response) {
			$scope.searchstate = "Search";
			$scope.message23 = response;
			
			return;
			//blob = new Blob([response], {type: "octet/stream"});
			
		});
	};
	$scope.buynow = function () {
		if (confirm("You are about to buy this item!! The auction will end and you will have to pay the buying price for your item. Confirm?") == true) {
			$scope.searchstate = "Searching";
			AuthenticationService.buynow( $rootScope.globals.currentUser.userid,$scope.details.id, function (response) {
				$scope.searchstate = "Search";
				$scope.respmsg = response;
				if (response.HTTP_CODE==200) {
					$scope.message23= "Won Item. You will be redirected to the home page.";
					$timeout(function() {
						$state.go('home-user');
					}, 1500);
				} else {
					$scope.message23 = "An error occured. Try again later.";
					$timeout(function() {
						$state.go($state.current, {}, {reload: true});
					}, 1500);
				}
			});
		}
		else{
			$scope.message1="Deletion Cancelled"
		}

	}; 

}]);

home.controller('PageCtrl1',['$scope','$rootScope', '$http','AuthenticationService', '$state',
                             function ($scope,$rootScope, $http, AuthenticationService, $state) {
	$scope.name = $rootScope.globals.currentUser.username;
	$scope.searchauc   = '';
	$scope.searchstate = "Search";
	$scope.sortType='id';
	$scope.sortReverse  = false;
	$scope.testmsg="arxi";
	$scope.name2="";
	$scope.from2="0";
	$scope.to2="99999";	
	$scope.categ2="";
	$scope.asc2="ASC";
	$scope.sort2="end";
	$scope.limit2="100";

	$scope.search = function () {
		$scope.searchstate = "Searching";
		$rootScope.show_res='1';
		AuthenticationService.search($scope.from2, $scope.to2, $scope.name2, $scope.categ2, $scope.asc2, $scope.sort2, $scope.limit2, function (response) {
			$scope.searchstate = "Search";
			if (response.HTTP_CODE!=500) {
				$rootScope.search_results = response;
			}     	

			$state.go('search_results');

		});
	}; 
	
	$scope.suggest = function () {
		AuthenticationService.suggest($scope.name, function (response) {
			$scope.suggestions = response;
			//$state.go('search_results');

		});
	};
	
	$scope.auction_details = function (aucid,title) {
		AuthenticationService.auction_details(aucid, function (response) {
			$rootScope.titleauc = title;
			$rootScope.details = response;
			$state.go('auction_details');

		});
	}; 
}]);


//data has the auctions returned from the search
authentication.controller("PageCtrl2",['$scope','$rootScope','$state', '$http', 'AuthenticationService','$timeout',
                                       function ($scope, $rootScope,$state, $http, AuthenticationService, $timeout) {

	$scope.searchstate = "Search";
	$scope.errormsg2="";
	$scope.asc="ASC";
	$scope.sort="end";
	$scope.limit="100";
	$scope.from="";
	$scope.to="";

	$scope.list_categories = function () {
		AuthenticationService.list_categories(function (response) {
			$scope.categlist = response;
		});
	};

	$scope.search = function () {
		if($scope.to==""){
			$scope.to="99999";
		}
		if($scope.from==""){
			$scope.from="0";
		}

		$scope.searchstate = "Searching";
		AuthenticationService.search($scope.from, $scope.to, $scope.name, $scope.categ, $scope.asc, $scope.sort, $scope.limit, function (response) {
			$rootScope.show_res='1';
			$scope.search_results= response;
			$scope.searchstate = "Search";
			if (response.HTTP_CODE==500) {
				$scope.errormsg2= "No auctions found or an uknown error occured. Please refresh the page and try again.";
			}
			else
			{
				$rootScope.search_results = response;
				$state.go('search_results');
			}

		});
	}; 




}]);



//data has auctions from the logged in user
authentication.controller("UserAuctCtrl",['$scope','$rootScope', '$http','AuthenticationService', '$state',
                                          function ($scope, $rootScope, $http, AuthenticationService, $state ) {
	$scope.sortType='id';
	$scope.sortReverse  = false;
	$scope.searchstate1 = "Search";
	$scope.searchstate2 = "Search";
	$scope.retrieve = function () {
		AuthenticationService.retrieve($rootScope.globals.currentUser.userid, function (response) {
			$scope.data4 = response;
			if (response.HTTP_CODE!=500) {
				$scope.data3 = response;
			}     	
		});
	}; 
	$scope.delete_auction = function (auc_id) {

		if (confirm("You are about to delete your auction!! Are you SURE you want to delete it?") == true) {
			$scope.searchstate1 = "Searching";
			AuthenticationService.delete_auction($rootScope.globals.currentUser.userid, auc_id, function (response) {
				$scope.searchstate1 = "Search";
				$scope.data = response.MSG;
				if (response.HTTP_CODE==200) {
					$scope.message1="Successfull Deletion"
				} else {
					$scope.message1="Something went wrong. Refresh the page and try again."
				}
			});
		}
		else{
			$scope.message1="Deletion Cancelled"
		}
	}; 

	$scope.redirect_edit = function (auc_id, auc_name, auc_bid) {
		if (confirm("You are about to be redirected to the edit page!! Confirm?") == true) {
			$scope.searchstate = "Searching";
			$rootScope.auctionid = auc_id;
			$rootScope.auctionname = auc_name;
			$rootScope.auctionbid = auc_bid;    
			$scope.searchstate = "Search";
			$state.go('edit_auction');
		}
		else{
			$scope.message1="Edit Cancelled";
		}

	}; 








}]);



//eidt_auction controller
authentication.controller("EditAucCtrl",['$scope','$rootScope', '$http','AuthenticationService', '$state',
                                         function ($scope, $rootScope, $http, AuthenticationService, $state) {
	$scope.searchstate = "Search";
	$scope.date = new Date();
	$scope.cur_year= $scope.date.getFullYear();
	$scope.cur_month= $scope.date.getMonth() + 1;
	$scope.cur_day= $scope.date.getDate();
	$scope.auctionid =   $rootScope.auctionid;
	$scope.auc_name = $rootScope.auctionname; 
	$scope.auc_bid =  $rootScope.auctionbid;     
	$scope.buy_price = "";
	$scope.desc = "";
	$scope.location = "";
	$scope.ends = "";
	$scope.first_bid = "";
	$scope.edit_auction = function () {

		if (confirm("The edited data are about to be submitted!! Confirm?") == true) {
			if($scope.buy_price == ""){
				$scope.buy_price = "99999";
			}
			if($scope.desc == ""){
				$scope.desc = "Item Description";
			}
			if($scope.location == ""){
				$scope.location = "Greece";
			}
			if($scope.first_bid == ""){
				$scope.first_bid= "0";
			}
			if($scope.ends == ""){
				$scope.ends= "2016-12-12";
			}
			$scope.ends = $scope.year + '-' + $scope.month + '-' + $scope.day;

			AuthenticationService.edit_auction($scope.auctionid, $scope.buy_price, $scope.desc, $scope.location, $scope.ends, $scope.first_bid, function (response) {
				$scope.data = response;
				if (response.HTTP_CODE==200) {
					$scope.message4= "Success";

				} else {
					$scope.message4 = "Error";
				}
			});
		}
		else{
			$scope.message4="Edit Cancelled";
		}

	}; 
}]);



//outbox controller
authentication.controller("outboxCtrl",['$scope','$rootScope', '$http','AuthenticationService', '$state',
                                        function ($scope, $rootScope, $http, AuthenticationService, $state) {
	$scope.searchstate = "Search";
	$scope.searchauc   = '';
	$scope.sortType='date';
	$scope.sortReverse  = true;
	$scope.get_sentmessages = function () {
		AuthenticationService.get_sentmessages($rootScope.globals.currentUser.userid, function (response) {
			$scope.messages= response;

			if(response.HTTP_CODE==500){
				$scope.messages= "";
			}
		});
	}; 
	$scope.go_to_send_message2 = function (username) {
		$scope.searchstate = "Searching";
		$rootScope.msg_username=username;
		$scope.searchstate = "Search";
		$state.go('send_message');
	}; 

	$scope.message_details = function (message,username) {
		$scope.searchstate = "Searching";
		$rootScope.msg_body=message;
		$rootScope.msg_username=username;
		$scope.searchstate = "Search";
		$rootScope.var1 = "To";    
		$state.go('message_details');
	}; 

}]);

authentication.controller("inboxCtrl",['$scope','$rootScope', '$http','AuthenticationService', '$state',
                                       function ($scope,$rootScope, $http, AuthenticationService, $state) {
	$scope.search   = '';
	$scope.sortType='date';
	$scope.sortReverse  = true;
	$scope.searchauc   = '';
	$scope.searchstate = "Search";
	$rootScope.notifications = localStorage.getItem("notifications"); 
	$rootScope.notifications = '0';
	localStorage.setItem("notifications", $rootScope.notifications);

	$scope.userid = $rootScope.globals.currentUser.userid;

	$scope.get_messages = function () {       		  
		AuthenticationService.get_messages($rootScope.globals.currentUser.userid, function (response) {         	
			$scope.messages= response;
			if(response.HTTP_CODE == 500){
				$scope.messages= "";
			}
		});
	}; 

	$scope.go_to_send_message = function (username) {
		$scope.searchstate = "Searching";
		$rootScope.msg_username=username;
		$scope.searchstate = "Search";
		$state.go('send_message');
	}; 
	$scope.message_details = function (message,username) {
		$scope.searchstate = "Searching";
		$rootScope.msg_body=message;
		$rootScope.msg_username=username;
		$scope.searchstate = "Search";
		$rootScope.var1 = "From";                                 	   				  
		$state.go('message_details');
	}; 


}]);

authentication.controller("SendMsgCtrl",['$scope','$rootScope', '$http','AuthenticationService', '$state',
                                         function ($scope,$rootScope, $http, AuthenticationService, $state) {
	$scope.searchstate = "Search";
	$scope.to_username=$rootScope.msg_username;
	$scope.send_message = function () {
		$scope.searchstate = "Searching";
		AuthenticationService.send_message($rootScope.globals.currentUser.username, $scope.to_username, $scope.title, $scope.message, function (response) {
			$scope.searchstate = "Search";
			$rootScope.msg_username="";
			if (response.HTTP_CODE==200) {
				if($scope.to_username == $rootScope.globals.currentUser.username){
					$rootScope.notifications = localStorage.getItem("notifications"); 
					$rootScope.notifications = $rootScope.notifications + 1;
					localStorage.setItem("notifications", $rootScope.notifications);
				}
				$scope.msg= "Message sent successfully.";

			} else {
				$scope.msg = "Error";

			}

		});
	}; 


}]);


//message_details controller
authentication.controller("MsgDetCtrl",['$scope','$rootScope', '$http','AuthenticationService', '$state',
                                        function ($scope, $rootScope, $http, AuthenticationService, $state) {
	$scope.messagebody = $rootScope.msg_body;
	$scope.fromuser = $rootScope.msg_username;


}]);

//add_category controller

authentication.controller('AddCategCtrl',
		['$scope', '$rootScope', '$state','$timeout', 'AuthenticationService', 
		 function ($scope, $rootScope, $state, $timeout, AuthenticationService) {
			$scope.searchstate = "Search";
			$scope.sortType='date';
			$scope.sortReverse  = true;
			$scope.searchauc   = '';
			$scope.regmsg = "";

			$scope.add_category = function () {
				$scope.searchstate = "Searching";
				AuthenticationService.add_category($scope.categ_name, $scope.categ_desc, function (response) {
					$scope.searchstate = "Search";
					if (response.HTTP_CODE==200) {
						$scope.regmsg = "The new category has been added.The page will refresh automatically.";			    	  
						$timeout(function() {
							$scope.regmsg = "";			    	  
							$state.go($state.current, {}, {reload: true});
						}, 1000);

					} else if(response.HTTP_CODE==503){
						$scope.regmsg = "This category already exists.The page will refresh automatically.";			    	  
						$timeout(function() {
							$scope.regmsg = "";	
							$state.go($state.current, {}, {reload: true});
						}, 1000);
					}
					else{

						$scope.regmsg = "A server error occured.Try again in a while. The page will refresh automatically.";
						$timeout(function() {
							$scope.regmsg = "";	
							$scope.categ_name = "";	 
							$scope.categ_desc = "";	
							$state.go($state.current, {}, {reload: true});
						}, 1000);
					}
				});
			};

			$scope.list_categories = function () {
				$scope.sortType='name';
				AuthenticationService.list_categories(function (response) {
					$scope.categlist = response;
				});
			};
		}]);

//Sold Items Controller
authentication.controller("SoldItemsCtrl",['$scope','$rootScope', '$http','AuthenticationService', '$state',
                                           function ($scope, $rootScope, $http, AuthenticationService, $state) {
	$scope.sortType='buyer';
	$scope.sortReverse  = true;

	$scope.sold_items = function () {
		AuthenticationService.sold_items($rootScope.globals.currentUser.userid, function (response) {  
			$scope.items= response;
			if(response.HTTP_CODE==500){
				$scope.items="";
			}


		});
	};

	$scope.rate_buyer = function (username12, item) {
		$rootScope.rateusername = username12;
		$rootScope.puritem = item;  
		$state.go('rate_buyer');
	};


}]);

//Purchased Items Controller
authentication.controller("PurchItemsCtrl",['$scope','$rootScope', '$http','AuthenticationService', '$state',
                                            function ($scope, $rootScope, $http, AuthenticationService, $state) {

	$scope.sortType='seller';
	$scope.sortReverse  = true;
	$scope.purchased_items = function () {

		AuthenticationService.purchased_items($rootScope.globals.currentUser.userid, function (response) {
			$scope.items= response;
			if(response.HTTP_CODE==500){
				$scope.items="";
			}

		});
	};

	$scope.rate_seller = function (username12, item) {
		$rootScope.rateusername = username12;
		$rootScope.puritem = item;  
		$state.go('rate_seller');
	};

}]);


//Rate seller Controller
authentication.controller("RateSellerCtrl",['$scope','$rootScope', '$http','AuthenticationService', '$state', '$timeout',
                                            function ($scope, $rootScope, $http, AuthenticationService, $state, $timeout) {

	$scope.rate_uname = $rootScope.rateusername;
	$scope.item = $rootScope.puritem;

	$scope.rateseller = function () {
		AuthenticationService.rate_seller($rootScope.globals.currentUser.userid,$scope.rate_uname, $scope.rating , function (response) {
			if(response.HTTP_CODE == 200){
				$scope.message23 = "You left Feedback successfully. Thank you for your time. You will be redirected to the home page.";		  		    		
			}else{
				$scope.message23 = "An error occured. Try again later";
			}
			$timeout(function() {
				$state.go('home-user');
			}, 1000);
		});
	}; 
}]);

//Rate Buyer Controller
authentication.controller("RateBuyerCtrl",['$scope','$rootScope', '$http','AuthenticationService', '$state', '$timeout',
                                           function ($scope, $rootScope, $http, AuthenticationService, $state, $timeout) {

	$scope.rate_uname = $rootScope.rateusername;
	$scope.item = $rootScope.puritem;

	$scope.rate_buyer = function () {

		AuthenticationService.rate_buyer($rootScope.globals.currentUser.userid,$scope.rate_uname, $scope.rating , function (response) {
			$scope.message23= response.MSG;				  		    		
			$timeout(function() {
				$state.go('home_user');
			}, 1000);
		});
	}; 
}]);

/*authentication.controller("UploadXmlCtrl",['$scope','$rootScope', '$http','AuthenticationService', '$state', '$timeout',
                                           function ($scope, $rootScope, $http, AuthenticationService, $state, $timeout) {
	 $scope.uploadxml = function () {
		 alert("Success ... ");
		 return;
		 var formData=new FormData();
         formData.append("file",file.files[0]);
                   $http({
                  method: 'POST',
                  url: '/serverApp/rest/newDocument',
                  headers: { 'Content-Type': undefined},
                  data:  formData
                })
                .success(function(data, status) {                       
                    alert("Success ... " + status);
                })
                .error(function(data, status) {
                    alert("Error ... " + status);
                });
      };
	 
}]);*/

