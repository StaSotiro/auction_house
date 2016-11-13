package utilities;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import model.Auction;
import model.User;

public class ToJSON {
	
	//for auctions
	public JSONArray toJSONArray(List<Auction> list) throws Exception {
		JSONArray json = new JSONArray(); //JSON array that will be returned
		try {
			//loop through the ResultSet
			for(Auction au : list){	             
				//each row in the ResultSet will be converted to a JSON Object
				JSONObject obj = new JSONObject();
				obj.put("id", au.getId());//int(11)
				obj.put("name",au.getName());//varchar(45) 
				obj.put("cur_bid",au.getCurrentBid());
				obj.put("buy_price",au.getBuyPrice());
				obj.put("started",au.getStarted());
				obj.put("ends",au.getEnds());
				obj.put("item_loc",au.getItemLocation());
				obj.put("seller",au.getUser1());
				obj.put("high_bidder",au.getUser2());                 
				json.put(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json; //return JSON array
	}
	
	//we will use that if we need to get list of auctions for user
	public JSONObject toJSONUser(User u)  throws Exception{
		JSONObject obj = u.toJSON();
		return obj;
	}

}
