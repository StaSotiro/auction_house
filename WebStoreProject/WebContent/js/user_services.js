'use strict';

angular.module('Authentication')

.factory('AuthenticationService',
    ['Base64', '$http', '$cookieStore', '$rootScope', '$timeout',
    function (Base64, $http, $cookieStore, $rootScope, $timeout) {
        var service = {};

        service.Login = function (username, password, callback) {

           $http.post("http://localhost:8080/WebStoreProject/rest/users/login", { username: username, password: password })
               .success(function (response) {
                   callback(response);
               });

       };
       
       service.register = function (username, password, address, cell, latitude, longitude, country, birth, email, fname, lname, phone, tax, callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/users/register", { username: username, password: password, address: address, cell: cell, latitude:latitude, longitude:longitude, country:country, birth:birth, email:email, fname:fname, lname:lname, phone:phone, tax:tax })
              .success(function (response) {
                  callback(response);
              });

      };
      
      service.retrieve = function (user_id, callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/users/my_auctions", { id: user_id })
              .success(function (response) {
                  callback(response);
              });

      };
      
      service.delete_auction = function (user_id, auction_id, callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/users/delete_auction", { user_id: user_id, auc_id: auction_id })
              .success(function (response) {
                  callback(response);
              });

      };

      service.edit_auction = function (auction_id, buy_price, desc, location, ends ,first_bid, callback) {
          $http.post("http://localhost:8080/WebStoreProject/rest/users/edit_auction", { auc_id: auction_id,  buy_price: buy_price, desc: desc, location: location, ends: ends, first_bid:first_bid  })
              .success(function (response) {
                  callback(response);
              });

      };
      
      service.validateuser = function (user_id, callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/users/validate", { id: user_id })
              .success(function (response) {
                  callback(response);
              });

      };
      
      
      service.add = function (auc_name, buy_price, first_bid, ends, country, latitude, longitude,images, category1, category2, user_id, callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/auctions/add", {name: auc_name, buy_price: buy_price, first_bid: first_bid, ends: ends, country: country, latitude:latitude, longitude:longitude, images:[images], categories:[category1, category2], usrID: user_id })
              .success(function (response) {
                  callback(response);
              });

      };
       
       
       service.search = function (from, to, name, categ, asc, sort, limit, callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/auctions/search", { from:from, to:to, name:name, categ:categ, asc:asc, sort:sort, limit:limit })
              .success(function (response) {
                  callback(response);
              });

      };
      
      service.makebid = function (auc_id, bid, usr_id, callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/auctions/bid", { auc_id:auc_id, bid:bid, usr_id:usr_id })
              .success(function (response) {
                  callback(response);
              });

      };
      
      service.auction_details = function (id,callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/auctions/get", { id:id })
              .success(function (response) {
                  callback(response);
              });

      };
      
      service.get_messages = function (userid,callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/users/inbox_messages", { id:userid })
              .success(function (response) {
                  callback(response);
              });

      };
      
      service.get_sentmessages = function (userid,callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/users/sent_messages", { id:userid })
              .success(function (response) {
                  callback(response);
              });

      };

      
      service.send_message = function (username1,username2,title,message, callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/users/send_message", { from:username1, to:username2, title:title, message:message })
              .success(function (response) {
                  callback(response);
              });

      };
      
      service.add_category = function (name, description, callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/category/add", { name:name, description:description })
              .success(function (response) {
                  callback(response);
              });

      };
      
      service.list_categories = function ( callback) {

    	  $http.get('http://localhost:8080/WebStoreProject/rest/category/list')
          .success(function (response) {
              callback(response);
          });

      };
      
      service.sold_items = function (userid, callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/users/sold_auctions", { id:userid })
              .success(function (response) {
                  callback(response);
              });

      };
      
      service.purchased_items = function (userid, callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/users/won_auctions", { id:userid })
              .success(function (response) {
                  callback(response);
              });

      };
      
      service.buynow = function (userid,auctionid, callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/auctions/buy", { usr_id:userid, auc_id:auctionid })
              .success(function (response) {
                  callback(response);
              });

      };
      
      service.rate_buyer = function (userid, username,rating, callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/users/rate", { id:userid, username:username ,rate:rating})
              .success(function (response) {
                  callback(response);
              });

      };
      
      service.suggest = function (username,callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/users/suggestions", { username:username })
              .success(function (response) {
                  callback(response);
              });

      };
      
      service.rate_seller = function (userid, username, rating, callback) {

          $http.post("http://localhost:8080/WebStoreProject/rest/users/rate", { id:userid, username:username ,rate:rating})
              .success(function (response) {
                  callback(response);
              });

      };
      //edw se thelw maga mou
      service.download_xml = function(auction_name,callback){
    	  var stringer = 'http://localhost:8080/WebStoreProject/download?auction_name='+ auction_name;
    	  $http.get(stringer,{
    		  responseType: 'arraybuffer',
    		  headers:{'Content-Type': undefined}
    	  })
    	  .success(function (response) {
    		  var blob = new Blob([response], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
    		    var objectUrl = URL.createObjectURL(blob);
    		    window.open(objectUrl);
              callback(response.headers);
          });
        
      };
      
      service.upload_xml = function (files, callback) {

    	  var formData=new FormData();
    	  angular.forEach(files,function(file){
    		  formData.append('file',file)
    	  })
          $http.post("http://localhost:8080/WebStoreProject/upload", formData, {
              transformRequest: angular.identity,
              headers: {'Content-Type': undefined}
          })
                 .success(function (response) {
                     callback(response);
                 })
                 .error(function(data, status) {
                     alert("Error ... " + status);
                 });
          
      };
      
      
      
        service.SetCredentials = function (username, password, id) {
            var authdata = Base64.encode(username + ':' + password);

            $rootScope.globals = {
                currentUser: {
                    username: username,
                    authdata: authdata,
                    userid:   id
                }

            };
           
            $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata; // jshint ignore:line
            $cookieStore.put('globals', $rootScope.globals);
        };   
        
        service.ClearCredentials = function () {
            $rootScope.globals = {};
            $cookieStore.remove('globals');
            $http.defaults.headers.common.Authorization = 'Basic ';
        };

        return service;
    }])

.factory('Base64', function () {
    /* jshint ignore:start */

    var keyStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';

    return {
        encode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            do {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);

                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;

                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }

                output = output +
                    keyStr.charAt(enc1) +
                    keyStr.charAt(enc2) +
                    keyStr.charAt(enc3) +
                    keyStr.charAt(enc4);
                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";
            } while (i < input.length);

            return output;
        },

        decode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
            var base64test = /[^A-Za-z0-9\+\/\=]/g;
            if (base64test.exec(input)) {
                window.alert("There were invalid base64 characters in the input text.\n" +
                    "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
                    "Expect errors in decoding.");
            }
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

            do {
                enc1 = keyStr.indexOf(input.charAt(i++));
                enc2 = keyStr.indexOf(input.charAt(i++));
                enc3 = keyStr.indexOf(input.charAt(i++));
                enc4 = keyStr.indexOf(input.charAt(i++));

                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;

                output = output + String.fromCharCode(chr1);

                if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
                }

                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";

            } while (i < input.length);

            return output;
        }
    };

    /* jshint ignore:end */
});