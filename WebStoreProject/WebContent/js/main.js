
var authentication = angular.module('Authentication', []);
var home = angular.module('Home', []);
var test = angular.module('Test', []);

var app = angular.module('WebApp', [
       'Authentication',
       'Home',
       'Test',
       'ngCookies',
       'ui.router',

 ])


 .directive('fileInput',['$parse',function($parse){
	 return{
		 restrict:'A',
		 link:function(scope,elm,attrs){
			 elm.bind('change',function(){
				 $parse(attrs.fileInput).
				 assign(scope,elm[0].files)
				 scope.$apply()
			 })
		 }
	 }
	 
 }])
.config(['$stateProvider', function ($stateProvider) {


  $stateProvider
  
	  	.state('home-guest', {
	      url: '/home-guest',
	      templateUrl: 'partials/home.html',
	      controller: 'HomePageCtrl',
	      data: {
	          requireLogin: 0,    //guest page
	          id: 0

	        }
	  })
	
	
	   .state('home-user', {
	      url: '/home-user',
	      templateUrl: 'partials/home_users.html',
	      controller: 'PageCtrl1',
	      data: {
	          requireLogin: 1,   //user page
	          id: 1

	        }
	  })
	  
	  	  .state('home-admin', {
	      url: '/home-admin',
	      templateUrl: 'partials/home_admin.html',
	      controller: 'TestCtrl',
	      data: {
	          requireLogin: 2,    //admin page
	          id: 2
	        }
	  })
	  
	  	  	  .state('validate', {
	      url: '/validate',
	      templateUrl: 'partials/validate.html',
	      controller: 'TestCtrl',
	      data: {
	          requireLogin: 2,    //admin page
	          id: 6
	        }
	  })
	  
	  	  .state('error', {
	      url: '/error',
	      templateUrl: 'partials/error.html',
	      controller: 'TestCtrl3',
	      data: {
	          requireLogin: 0,   //guest page
	          id: 3
	        }
	  })
	
	  .state('about', {
	      url: '/about',
	      templateUrl: 'partials/about.html',
	      controller: 'PageCtrl',
	      data: {
	          requireLogin: 0,   //guest page
	          id: 4

	        }
	  })
	  
		  .state('faq', {
	      url: '/faq',
	      templateUrl: 'partials/faq.html',
	      controller: 'PageCtrl',
	      data: {
	          requireLogin: 0,    //guest page
	          id: 5
	        }
	  })

	  
	  	  .state('contact', {
	      url: '/contact',
	      templateUrl: 'partials/contact.html',
	      controller: 'PageCtrl',
	      data: {
	          requireLogin: 0,      //guest page
	          id: 7
	        }
	  })
	  

	  
	  	  .state('login', {
	      url: '/login',
	      templateUrl: 'partials/login.html',
	      controller: 'logctrl',
	      data: {
	          requireLogin: 0,     //guest page
	          id: 8
	          
	        }
	  })
	  
	  	  .state('logout', {
	      url: '/logout',
	      templateUrl: 'partials/logout.html',
	      controller: 'logctrl2',
	      data: {
	          requireLogin: 1,    //user page
	          id: 9
	        }
	 
	  })
  
	  .state('register', {
	      url: '/register',
	      templateUrl: 'partials/register.html',
	      controller: 'logctrl3',
	      data: {
	          requireLogin: 0,    //guest page
	          id: 10
	        }
	 
	  })
  
  .state('myauctions', {
      url: '/myauctions',
      templateUrl: 'partials/my_auctions.html',
      controller: 'UserAuctCtrl',
      data: {
          requireLogin: 1,    //user page
          id: 11
        }
 
  })
  
  .state('add_auction', {
      url: '/add_auction',
      templateUrl: 'partials/add_auction.html',
      controller: 'addctrl',
      data: {
          requireLogin: 1,    //user page
          id: 12
        }
 
  })
  
  .state('search', {
      url: '/search',
      templateUrl: 'partials/search_auctions.html',
      controller: 'PageCtrl2',
      data: {
          requireLogin: 0,    //guest page
          id: 13
        }
  
  })
  
    .state('messaging', {
      url: '/messaging',
      templateUrl: 'partials/messaging.html',
      controller: '',
      data: {
          requireLogin: 1,    //user page
          id: 14
        }
  
  })
  
  .state('inbox', {
      url: '/inbox',
      templateUrl: 'partials/inbox.html',
      controller: 'inboxCtrl',
      data: {
          requireLogin: 1,    //user page
          id: 15
        }
  
  })
  
  .state('outbox', {
      url: '/outbox',
      templateUrl: 'partials/outbox.html',
      controller: 'outboxCtrl',
      data: {
          requireLogin: 1,    //user page
          id: 16
        }
  
  })
  
  .state('auction_details', {
      url: '/auction_details',
      templateUrl: 'partials/auction_details.html',
      controller: 'AucDetCtrl',
      data: {
          requireLogin: 0,    //guest page
          id: 17
        }
  
  })
  
  .state('edit_auction', {
      url: '/edit_auction',
      templateUrl: 'partials/edit_auction.html',
      controller: 'EditAucCtrl',
      data: {
          requireLogin: 1,    //user page
          id: 18
        }
  
  })
  
  .state('search_results', {
      url: '/search_results',
      templateUrl: 'partials/search_results.html',
      controller: 'SearchResCtrl',
      data: {
          requireLogin: 0,    //guest page
          id: 19
        }
  
  })
  
  .state('send_message', {
      url: '/send_message',
      templateUrl: 'partials/send_message.html',
      controller: 'SendMsgCtrl',
      data: {
          requireLogin: 1,    //guest page
          id: 20
        }
  
  })
  
  .state('message_details', {
      url: '/message_details',
      templateUrl: 'partials/message_details.html',
      controller: 'MsgDetCtrl',
      data: {
          requireLogin: 1,    //user page
          id: 21
        }
  
  })
  
  .state('add_category', {
      url: '/add_category',
      templateUrl: 'partials/add_category.html',
      controller: 'AddCategCtrl',
      data: {
          requireLogin: 2,    //admin page
          id: 22
        }
  
  })
  
  
  .state('list_categories', {
      url: '/list_categories',
      templateUrl: 'partials/list_categories.html',
      controller: 'AddCategCtrl',
      data: {
          requireLogin: 2,    //admin page
          id: 23
        }
  
  })
  
  .state('sold_items', {
      url: '/sold_items',
      templateUrl: 'partials/sold_items.html',
      controller: 'SoldItemsCtrl',
      data: {
          requireLogin: 1,    //user page
          id: 24
        }
  
  })
  
  .state('purchased_items', {
      url: '/purchased_items',
      templateUrl: 'partials/purchased_items.html',
      controller: 'PurchItemsCtrl',
      data: {
          requireLogin: 1,    //user page
          id: 24
        }
  
  })
  
  .state('rate_seller', {
      url: '/rate_seller',
      templateUrl: 'partials/rate_seller.html',
      controller: 'RateSellerCtrl',
      data: {
          requireLogin: 1,    //user page
          id: 25
        }
  
  })
  
  .state('rate_buyer', {
      url: '/rate_buyer',
      templateUrl: 'partials/rate_buyer.html',
      controller: 'RateBuyerCtrl',
      data: {
          requireLogin: 1,    //user page
          id: 26
        }
  
  });
  
}])
 

.run(['$rootScope', '$state', '$cookieStore', '$http','$timeout',
    function ($rootScope, $state, $cookieStore, $http, $timeout ) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
        }
  
        $rootScope.$on('$stateChangeStart', function (event, toState, toParams) {
        	$rootScope.notifications = localStorage.getItem("notifications"); 
            var requireLogin = toState.data.requireLogin;
            var hide = toState.data.hide;
            var id = toState.data.id;
            
            // redirect to login page if not logged in
            if (requireLogin != 0  && !$rootScope.globals.currentUser) {
            	event.preventDefault();
            	$state.go('login');

            }
            if ($rootScope.globals.currentUser && id == 0) {
            	event.preventDefault();
            	$state.go('home-user');
           	
            }
            if ($rootScope.globals.currentUser && id == 10) {
            	event.preventDefault();
           	
            }
            
            if ($rootScope.globals.currentUser.username== "admin" && (id == 0 || id==1)) {
            	event.preventDefault();
            	$state.go('home-admin');
           	
            }
            
            if ($rootScope.globals.currentUser.username!= "admin" && requireLogin == 2) {
            	event.preventDefault();
            	$state.go('error');
           	
            }

            if ( id==8  && $rootScope.globals.currentUser) {
            	event.preventDefault();	
            }
            
            if ($rootScope.globals.currentUser) {
            	$rootScope.myVar='1';
            }
            else{
            	$rootScope.myVar='0';

            }

            	
        });
    }]);

        
        

