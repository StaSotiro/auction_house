
package rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.prism.Image;

import daos.AuctionDAOImpl;
import daos.CategoryDAOImpl;
import daos.MessageDAOImpl;
import daos.UserDAOImpl;
import model.Auction;
import model.Bid;
import model.Category;
import model.Message;
import model.User;
//
@Path("/auctions")
public class AuctionEndpoint {

	@Path("/list")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	//sends auctions based on category and has a limit on it
	public Response list_auctions(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject aucdata = new JSONObject(data);
		AuctionDAOImpl auctiondao = new AuctionDAOImpl();
		List<Auction> list=null;
		String str = "";
		//try{
		//list=auctiondao.list();
		//}

		aucdata.optString("limit");
		return null;

	}
	@Path("/rate")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response rate_auction(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject aucdata = new JSONObject(data);
		AuctionDAOImpl auctiondao = new AuctionDAOImpl();
		String str = "";
		UserDAOImpl userdao = new UserDAOImpl();
		JSONObject resp = new JSONObject();
		Auction auction = auctiondao.find(aucdata.optInt("auc_id"));
		
		
		
		
		str=resp.toString();
		return Response.ok(str).build();
	}
	
	@Path("/buy")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response buy_auction(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject aucdata = new JSONObject(data);
		AuctionDAOImpl auctiondao = new AuctionDAOImpl();
		String str = "";
		UserDAOImpl userdao = new UserDAOImpl();
		JSONObject resp = new JSONObject();
		Auction auction = auctiondao.find(aucdata.optInt("auc_id"));
		MessageDAOImpl msgdao= new MessageDAOImpl();
		User winner = userdao.find(aucdata.optInt("usr_id"));
		/*EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();*/
		System.out.println("Auction bought!");
		System.out.println( "returnString: " + str );
		//send message to winner
		Message msg = new Message();
		msg.setBody("Congratulations you won Auction " +
				auction.getName() +". Contact seller for the transaction: "+
				auction.getUser1().getUsername());
		msg.setTitle("You Won an Auction!!");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date date = formatter.parse(LocalDateTime.now().toString());
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			msg.setTime(sqlDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg.setUser1(userdao.find_by_name("admin"));
		msg.setUser2(winner);
		msgdao.create(msg);
		
		msg = new Message();
		msg.setBody("Auction has ended " + auction.getName()+ 
				" with winner: "+winner.getUsername() + " who bought it for: " +
				Float.toString(auction.getBuyPrice()) + ", contact him soon!");
		msg.setTitle("Auction Ended");
		msg.setUser1(userdao.find_by_name("admin"));
		msg.setUser2(auction.getUser1());
		try {
			java.util.Date date = formatter.parse(LocalDateTime.now().toString());
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			msg.setTime(sqlDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msgdao.create(msg);
		auction.setUser2(winner);
		auctiondao.done(auction);
		resp.put("HTTP_CODE", "200");
		resp.put("MSG: ", "Successfully bought auction");
		str=resp.toString();
		return Response.ok(str).build();
	}
	@Path("/add")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	//sends auctions based on category and has a limit on it
	public Response add_auction(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject aucdata = new JSONObject(data);
		AuctionDAOImpl auctiondao = new AuctionDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		Auction auction = new Auction();
		auction.setBuyPrice(Float.parseFloat(aucdata.optString("buy_price")));
		auction.setCurrentBid(0);
		auction.setFirstBid(Float.parseFloat(aucdata.optString("first_bid")));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date date = formatter.parse(aucdata.optString("ends"));
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			auction.setEnds(sqlDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		auction.setItemLocation(aucdata.optString("country"));
		auction.setName(aucdata.optString("name"));
		auction.setLatitude(Float.parseFloat(aucdata.optString("latitude")));
		auction.setLongitude(Float.parseFloat(aucdata.optString("longitude")));
		JSONArray array = aucdata.optJSONArray("categories");
		List<Category> list = auction.getCategories();
		List<Auction> alist = null;
		CategoryDAOImpl cdao= new CategoryDAOImpl();
		for(int i=0; i<array.length();i++){
			System.out.println("JSONarray: " + (String) array.get(i));
			Category cat = cdao.search_name((String) array.get(i));
			if(cat!=null){
				list.add(cat);
			}
			else{
				System.out.println("Category doesn't exist bro");
			}
		}
		if(list!=null)
			auction.setCategories(list);
		try {
			java.util.Date date = formatter.parse(LocalDateTime.now().toString());
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			auction.setStarted(sqlDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONArray pics = aucdata.optJSONArray("images");
		
		UserDAOImpl usrdao= new UserDAOImpl();
		CategoryDAOImpl catdao=new CategoryDAOImpl();

		auction.setUser1(usrdao.find(Integer.parseInt( aucdata.optString("usrID"))) );
		try {
			int code=auctiondao.create(auction, pics);
			//an exoume failure
			if(code == 200){
				resp.put("HTTP_CODE", "200");
				resp.put("MSG: ", "Successfully added new auction");
				str=resp.toString();
			}
			else if(code==501){
				resp.put("HTTP_CODE", "501");
				resp.put("MSG: ", "Already exists with this PK");
				str=resp.toString();
			}
			else{
				resp.put("HTTP_CODE", "500");
				resp.put("MSG: ", "Something went wrong with your addition, please try later");
				str=resp.toString();
			}

		} catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		//catdao.search_name("boobs").getAuctions().size();
		//System.out.println("BBBB" + catdao.search_name("boobs").getAuctions().size());
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
	@Path("/get_img")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response get_img(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject aucdata = new JSONObject(data);
		AuctionDAOImpl auctiondao = new AuctionDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		Auction a = auctiondao.find(aucdata.optInt("id"));
		for(model.Image img : a.getImages())
			str+=img.getLink()+ ",";
		//str=jsonarray.toString();
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}
	@Path("/get")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response get_auction(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject aucdata = new JSONObject(data);
		AuctionDAOImpl auctiondao = new AuctionDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		try{	
			Auction auction = auctiondao.find(aucdata.getInt("id"));
			if(auction!=null){
				resp.put("HTTP_CODE", "200");
				resp.put("id", Integer.toString(auction.getId()));
				resp.put("buy",auction.getBuyPrice());
				resp.put("cur_bid", auction.getCurrentBid());
				if(auction.getDescription()!=null)
					resp.put("desc", auction.getDescription());
				else
					resp.put("desc","");
				resp.put("end", auction.getEnds().toString());
				resp.put("first", auction.getFirstBid());
				resp.put("location", auction.getItemLocation());
				resp.put("lat", Float.toString(auction.getLatitude()));
				resp.put("long", Float.toString(auction.getLongitude()));
				resp.put("star", auction.getStarted().toString());
				JSONArray jsonarray = new JSONArray();
				for(Category c : auction.getCategories()){
					JSONObject obj = new JSONObject();
					try {
						obj.put("name",c.getName());
					} catch (JSONException e){
						e.printStackTrace();
					}
					jsonarray.put(obj);
				}
				resp.put("categories", jsonarray.toString());
				if(auction.getUser2()!=null)
					resp.put("high_bidder", auction.getUser2().getUsername());
				else
					resp.put("high_bidder","");
				resp.put("seller", auction.getUser1().getUsername());
				/*for(model.Image img : auction.getImages()){
					JSONObject obj = new JSONObject();
					try {
						obj.put("link",img.getLink());
					} catch (JSONException e){
						e.printStackTrace();
					}
					jsonarray.put(obj);
				}
				resp.put("images", jsonarray.toString());*/
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
	@Path("/search")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response find_auction(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject aucdata = new JSONObject(data);
		AuctionDAOImpl auctiondao = new AuctionDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		Auction auction = new Auction();
		try{
			List<Auction> list = auctiondao.list(aucdata.optInt("limit"),
					aucdata.optString("sort"), aucdata.optString("asc"), 
					aucdata.optString("categ"), aucdata.optString("name"), 
					Float.parseFloat(aucdata.optString("from")), 
					Float.parseFloat(aucdata.optString("to")));
			if(list.isEmpty()){
				resp.put("HTTP_CODE", "500");
				resp.put("MSG: ", "You got no auctions ya noob");
				str=resp.toString();
			}
			else{
				resp.put("HTTP_CODE", "200");
				JSONArray jsonarray = new JSONArray();
				for(Auction a : list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("id",Integer.toString(a.getId()));
						obj.put("name", a.getName());
						obj.put("cur_bid", Float.toString(a.getCurrentBid()));
					} catch (JSONException e){
						e.printStackTrace();
					}
					jsonarray.put(obj);
				}
				//resp.put("MSG: ", jsonarray.toString());
				//str=resp.toString();
				str=jsonarray.toString();

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
	@Path("/bid")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response bid_auction(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject aucdata = new JSONObject(data);
		AuctionDAOImpl auctiondao = new AuctionDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		try{
			int code = auctiondao.bid_auction(aucdata.optInt("usr_id"), aucdata.optInt("auc_id"),
					Float.parseFloat(aucdata.optString("bid")));
			if(code == 200){
				resp.put("HTTP_CODE", "200");
				resp.put("MSG: ", "Successfully bidded auction");
				str=resp.toString();
			}
			else if(code==503){
				resp.put("HTTP_CODE", "500");
				resp.put("MSG: ", "Bid is too low");
				str=resp.toString();
			}
			else{
				resp.put("HTTP_CODE", "500");
				resp.put("MSG: ", "Something went wrong with your bidding, please try later");
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
	@Path("/bid_list")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response bid_auction_list(String data) throws JSONException{
		System.out.println("Incoming data: "+ data);
		JSONObject aucdata = new JSONObject(data);
		AuctionDAOImpl auctiondao = new AuctionDAOImpl();
		String str = "";
		JSONObject resp = new JSONObject();
		User user = null;
		UserDAOImpl userdao = new UserDAOImpl();
		try{
			List<Bid> blist = auctiondao.find(aucdata.optInt("id")).getBids();
			JSONArray jsonarray = new JSONArray();
			for(Bid bid : blist){
				JSONObject obj = new JSONObject();
				try {
					obj.put("username",userdao.find(bid.getUser().getId()).getUsername());
					obj.put("bid", Float.toString(bid.getValue()));
					obj.put("date", bid.getTime().toString());
				} catch (JSONException e){
					e.printStackTrace();
				}
				jsonarray.put(obj);
			}
			str+=jsonarray.toString();
		}catch(Exception e) {
			e.printStackTrace();
			resp.put("HTTP_CODE", "500");
			resp.put("MSG: ", "Server unable to fill request");
			str=resp.toString();
		}
		System.out.println( "returnString: " + str );
		return Response.ok(str).build();
	}

}
