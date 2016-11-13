package daos;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import model.Auction;

public interface AuctionDAO 
{
	public List<Auction> list(int limit, String sort,String asc, String categ, String name, float price_from, float price_to);

	JSONArray find_user_auctions(int id);

	Auction find(int id);

	int bid_auction(int user_id, int auc_id, float bid);

	int create(Auction auction, JSONArray pics);

	List<Auction> find_ended();

	void done(Auction auction);

	int rate(int id, int auct_id, float value);

	Auction find_by_name_auction(String name);

}
