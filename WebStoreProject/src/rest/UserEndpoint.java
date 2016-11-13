
package rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import bonus.Bonususer;
import daos.AuctionDAOImpl;
import daos.MessageDAOImpl;
import daos.UserDAOImpl;
import model.Auction;
import model.Bid;
import model.EndedAuction;
import model.Message;
import model.User;
import model.UserHasRatedUser;
//
@Path("/users/")
public class UserEndpoint {

	@Path("/kol")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String sayHtmlHello() {

		UserDAOImpl userdao = new UserDAOImpl();
		List<User> list = userdao.list();
		if(list == null){
			return null;
		}
		String opa = "Oi malakes: ";
		for(User usr:list){
			opa = opa + usr.toString() + "\n";
		}
		return opa;
	}

	//@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.APPLICATION_JSON)
	@Path("/login")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	//logs in either with username or email
	public Response login(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject usrdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		String str = "";
		MessageDAOImpl msgdao = new MessageDAOImpl();
		JSONObject resp = new JSONObject();
		try {
			User u = userdao.LogIn(usrdata.optString("username"),
					usrdata.optString("password"));
			//an exoume failure
			if(u != null){
				resp.put("HTTP_CODE", "200");
				resp.put("USR_ID", Integer.toString(u.getId()));
				resp.put("VALID", Boolean.toString(u.getValidated()));
				resp.put("UNREAD", Integer.toString(msgdao.get_unread_number(u.getId())));
				resp.put("MSG: ", "Successfull Login");
				str=resp.toString();
			}
			else {
				resp.put("HTTP_CODE", "500");
				resp.put("MSG: ", "Wrong email or password");
				str=resp.toString();
			}

		} catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}


	@Path("/allusers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getusr(){
		Response rb=null;
		String str="";
		UserDAOImpl userdao = new UserDAOImpl();
		JSONArray jsonArray = new JSONArray();
		List<User> list = userdao.list();
		for(User u: list){
			JSONObject obj = new JSONObject();
			obj=u.toJSON();
			jsonArray.put(obj);

		}
		str+=jsonArray.toString();
		rb=Response.ok(str).build();
		return rb;
	}
	@Path("/register")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject usrdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		if(userdao.find_by_name(usrdata.optString("username"))!=null){
			resp.put("HTTP_CODE", "501");
			resp.put("MSG: ", "Someone already exists with this username/email");
			str=resp.toString();
			System.out.println( "returnString: " + str );
			return Response.ok(str).build();
		}
		User usr = new User();
		usr.setUsername(usrdata.optString("username"));
		usr.setPassword(usrdata.optString("password"));
		usr.setAddress(usrdata.optString("address"));
		usr.setCellPhone(usrdata.optString("cell"));
		usr.setCountry(usrdata.optString("country"));
		usr.setLatitude(Float.parseFloat(usrdata.optString("latitude")));
		usr.setLongitude(Float.parseFloat(usrdata.optString("longitude")));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date date = formatter.parse(usrdata.optString("birth"));
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			usr.setDateOfBirth(sqlDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		usr.setEmailAddress(usrdata.optString("email"));
		usr.setFname(usrdata.optString("fname"));
		usr.setLname(usrdata.optString("lname"));
		usr.setPhoneNumber(usrdata.optString("phone"));
		usr.setTaxId(usrdata.optString("tax"));

		try {
			int code=userdao.create(usr);
			//an exoume failure
			if(code == 200){
				resp.put("HTTP_CODE", "200");
				resp.put("MSG: ", "Successfull Register");
				str=resp.toString();
			}
			else if(code==501){
				resp.put("HTTP_CODE", "501");
				resp.put("MSG: ", "Someone already exists with this username/email");
				str=resp.toString();
			}
			else{
				resp.put("HTTP_CODE", "500");
				resp.put("MSG: ", "Something went wrong with your registration, please try later");
				str=resp.toString();
			}

		} catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
	@Path("/my_auctions")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response my_auctions(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		AuctionDAOImpl auctiondao = new AuctionDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();

		try {
			JSONArray list = auctiondao.find_user_auctions(userdata.optInt("id"));
			if(list!=null){
				resp.put("HTTP_CODE", "200");
				//resp.put("MSG", list.toString());
				//str=resp.toString();
				str = list.toString();
			}
			else{
				resp.put("HTTP_CODE", "500");
				resp.put("MSG: ", "nothing found");
				str=resp.toString();
			}
		} catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
	@Path("/delete_auction")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response my_auction_delete(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		try {
			int code = userdao.delete_auction(userdata.optInt("user_id"), userdata.optInt("auc_id"));
			if(code == 1){
				resp.put("HTTP_CODE", "200");
				resp.put("MSG: ", "Successfull Deletion");
				str=resp.toString();
			}
			else if(code==0){
				resp.put("HTTP_CODE", "500");
				resp.put("MSG: ", "Someone already exists with this username/email");
				str=resp.toString();
			}
		} catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
	@Path("/edit_auction")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response my_auction_edit(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		try {
			int code = userdao.edit_auction(userdata.optInt("auc_id"), userdata.optString("desc"),
					Float.parseFloat(userdata.optString("buy_price")), Float.parseFloat(userdata.optString("first_bid")),
					userdata.optString("ends"), userdata.optString("location"));
			if(code == 1){
				resp.put("HTTP_CODE", "200");
				resp.put("MSG: ", "Successfull Edit");
				str=resp.toString();
			}
			else if(code==0){
				resp.put("HTTP_CODE", "500");
				resp.put("MSG: ", "Someone already exists with this username/email");
				str=resp.toString();
			}
		} catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
	@Path("/validate")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response verify_user(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		try {
			int code = userdao.verify(userdata.optInt("id"));
			if(code==1){
				resp.put("HTTP_CODE", "200");
				resp.put("MSG: ", "Successfull Validation");
				str=resp.toString();
			}
			else{
				resp.put("HTTP_CODE", "500");
				resp.put("MSG: ", "not exist");
				str=resp.toString();
			}
		}catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
	@Path("/get_user")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response get_user(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		try {
			User user = userdao.find(userdata.optInt("id"));
			if(user!=null){
				resp.put("HTTP_CODE", "200");
				resp.put("MSG: ", "Successfull get");
				//resp.put("USER: ", user.toJSON().toString());
				//str=resp.toString();
				str=user.toJSON().toString();
			}
			else{
				resp.put("HTTP_CODE", "500");
				resp.put("MSG: ", "not exist");
				str=resp.toString();
			}
		}catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}

	//id user pou kanei to rate, username to tupa ton opoion KANOUME rate, kai rate float
	@Path("/rate")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response rate(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		try {
			int code = userdao.rate(userdata.optInt("id"),userdata.optString("username"), Float.parseFloat(userdata.optString("rate")));
			if(code==1){
				resp.put("HTTP_CODE", "200");
				resp.put("MSG: ", "Successfully rated user");
				str=resp.toString();
			}
			else{
				resp.put("HTTP_CODE", Integer.toString(code));
				resp.put("MSG: ", "Something went wrong");
				str=resp.toString();
			}
		}catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
	@Path("/won_auctions")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response won_auctions(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		MessageDAOImpl messagedao = new MessageDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		User user = userdao.find(userdata.optInt("id"));

		if(!user.getEndedAuctions2().isEmpty()){
			JSONArray list = new JSONArray();
			for(EndedAuction e : user.getEndedAuctions2()){
				JSONObject obj= new JSONObject();
				
				obj.put("rated","0");
				for(UserHasRatedUser k : user.getUserHasRatedUsers1())
				if(k.getUser2().equals(e.getUser1()) )
					obj.put("rated","1");
				
					
				obj.put("desc",e.getDescription());
				obj.put("location", e.getItemLocation());
				obj.put("lat",e.getLatitude());
				obj.put("long",e.getLongitude());
				obj.put("name",e.getName());
				obj.put("seller",e.getUser1().getUsername());
				list.put(obj);
			}
			str=list.toString();
		}
		else{
			resp.put("HTTP_CODE", "500");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}	
	@Path("/sold_auctions")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response sold_auctions(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		MessageDAOImpl messagedao = new MessageDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		User user = userdao.find(userdata.optInt("id"));

		if(!user.getEndedAuctions1().isEmpty()){
			JSONArray list = new JSONArray();
			for(EndedAuction e : user.getEndedAuctions1()){
				JSONObject obj= new JSONObject();
				
				obj.put("rated","0");
				for(UserHasRatedUser k : user.getUserHasRatedUsers1())
				if(k.getUser2().equals(e.getUser2()) )
					obj.put("rated","1");
				
				obj.put("desc",e.getDescription());
				obj.put("location", e.getItemLocation());
				obj.put("lat",e.getLatitude());
				obj.put("long",e.getLongitude());
				obj.put("name",e.getName());
				obj.put("buyer",e.getUser2().getUsername());
				list.put(obj);
			}
			str=list.toString();
		}
		else{
			resp.put("HTTP_CODE", "500");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}	
	@Path("/send_message")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response send_message(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		MessageDAOImpl messagedao = new MessageDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		Message msg = new Message();
		msg.setUser1(userdao.find_by_name((userdata.optString("from"))));
		msg.setUser2(userdao.find_by_name((userdata.optString("to"))));
		msg.setTitle(userdata.optString("title"));
		msg.setBody(userdata.optString("message"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date date = formatter.parse(LocalDateTime.now().toString());
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			msg.setTime(sqlDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			int code = messagedao.create(msg);
			if(code==200){
				resp.put("HTTP_CODE", "200");
				resp.put("MSG: ", "Successfully added message");
				str=resp.toString();
			}
			else{
				resp.put("HTTP_CODE", Integer.toString(code));
				resp.put("MSG: ", "Something went wrong");
				str=resp.toString();
			}
		}catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
	@Path("/sent_messages")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response inbox_message(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		MessageDAOImpl messagedao = new MessageDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		User user = userdao.find(userdata.optInt("id"));
		if(!user.getMessages1().isEmpty()){
			JSONArray list = new JSONArray();
			for(Message m : user.getMessages1()){
				JSONObject obj = new JSONObject();
				obj.put("id",m.getId());
				obj.put("from", m.getUser1().getUsername());
				obj.put("to", m.getUser2().getUsername());
				obj.put("title", m.getTitle());
				obj.put("message", m.getBody());
				obj.put("date", m.getTime().toString());
				list.put(obj);
			}
			str=list.toString();
		}
		else{
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Empty AF");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
	@Path("/inbox_messages")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response sent_message(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		MessageDAOImpl messagedao = new MessageDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		User user = userdao.find(userdata.optInt("id"));
		if(!user.getMessages2().isEmpty()){
			JSONArray list = new JSONArray();
			for(Message m : user.getMessages2()){
				JSONObject obj = new JSONObject();
				obj.put("id",m.getId());
				obj.put("from", m.getUser1().getUsername());
				obj.put("to", m.getUser2().getUsername());
				obj.put("title", m.getTitle());
				obj.put("message", m.getBody());
				obj.put("date", m.getTime().toString());
				list.put(obj);
			}
			str=list.toString();
		}
		else{
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Empty AF");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
	@Path("/del_inmessage")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response del_message(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		MessageDAOImpl messagedao = new MessageDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		Message m = messagedao.find(userdata.optInt("id"));
		try{
			if(messagedao.delete_inbox(m)==200){
				resp.put("HTTP_CODE", "200");
				resp.put("MSG: ", "Successfully deleted message");
				str=resp.toString();
			}

		}catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Empty AF");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
	@Path("/del_semessage")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response del_se_message(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		MessageDAOImpl messagedao = new MessageDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		Message m = messagedao.find(userdata.optInt("id"));
		try{
			if(messagedao.delete_sent(m)==200){
				resp.put("HTTP_CODE", "200");
				resp.put("MSG: ", "Successfully deleted message");
				str=resp.toString();
			}


		}catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
	@Path("/unseen_messages")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response get_unseen(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		MessageDAOImpl messagedao = new MessageDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		int code = messagedao.get_unread_number(userdata.optInt("id"));
		if(code!=-1){
			resp.put("HTTP_CODE", "200");
			resp.put("No", Integer.toString(code));
			str=resp.toString();
		}
		else{
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
	
	/*bonus
		find all users that have bidded  in the same auctions as him, sort by
		number of common bids. Find
	
	*/
	@Path("/suggestions")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response suggestions(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject userdata = new JSONObject(data);
		UserDAOImpl userdao = new UserDAOImpl();
		MessageDAOImpl messagedao = new MessageDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		User u = userdao.find_by_name(userdata.optString("username"));
		int id = u.getId();
		List<Bonususer> bonus = new ArrayList<Bonususer>();
		//finding all the auctions he has bidded
		for(Bid b: u.getBids()){
			Auction a = b.getAuction();
			//for each auction find all the users that have bidded
			//in the auction
			for(Bid c : a.getBids()){
				boolean found = false;
				if(c.getUser().getId() == id)
					continue;
				//searching the list in case he already exists
				for(Bonususer k : bonus){
					if(k.getId() == c.getUser().getId()){
						k.setCount(k.getCount()+1);
						found = true;
						break;
					}
				}
				//if not, we add him
				if(found == false){
					Bonususer bonusaros = new Bonususer();
					bonusaros.setCount(1);
					bonusaros.setId(c.getUser().getId());
					bonus.add(bonusaros);
				}
			}
		}
		//sorting list in descending order of common bids
		bonus.sort(Bonususer.Bonuscomarator);
		int count =1;
		List<Auction> uber = new ArrayList<Auction>();
		for(Bonususer b: bonus){
			count++;
			if(!userdao.find(b.getId()).getBids().isEmpty()){
				Auction auction = userdao.find(b.getId()).getBids().get(userdao.find(b.getId()).getBids().size() % count).getAuction();
				uber.add(auction);
			}
		}
		//if we have less than 5 to suggest, add some of the newest to show him
		if(uber.size()<5){
			AuctionDAOImpl aucdao = new AuctionDAOImpl();
			List<Auction> helper = aucdao.list(5, "new", "ASC", "", "", 0, 9999);
			for(Auction helpa: helper){
				uber.add(helpa);
				if(uber.size()==5){
					break;
				}
			}
		}
		//creating what i return to user
		JSONArray jsonarray = new JSONArray();
		for(Auction a : uber){
			JSONObject jman = new JSONObject();
			jman.put("id", a.getId());
			jman.put("name", a.getName());
			jman.put("buy",a.getBuyPrice());
			jman.put("cur_bid", a.getCurrentBid());
			jman.put("location", a.getItemLocation());
			//jman.put("image", a.getImages().get(0));
			jsonarray.put(jman);
		}
		str+=jsonarray.toString();
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
}
