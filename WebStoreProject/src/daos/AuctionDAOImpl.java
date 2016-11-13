package daos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import model.Auction;
import model.Bid;
import model.Category;
import model.EndedAuction;
import model.Image;
import model.Message;
import model.User;
import model.UserRatedAuction;
import model.UserRatedAuctionPK;
import rest.UserEndpoint;

public class AuctionDAOImpl implements AuctionDAO {
	@Override
	public Auction find_by_name_auction(String name){
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		javax.persistence.Query query = em.createQuery(
				"SELECT u FROM Auction u WHERE u.name LIKE"
						+ " :username");
		query.setParameter("username", name);
		Auction auction=null;
		try {
			auction = (Auction) query.getSingleResult();
		} catch (Exception e) {
			tx.commit();
			em.close();
			// TODO Auto-generated catch block
			return null;//User does not exist
		}
		tx.commit();
		em.close();

		if(auction==null)
			return null;//User does not exist
		return auction;
	}

	@Override
	public Auction find(int id) {
		// TODO Auto-generated method stub
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Auction auction = em.find(Auction.class, id); 
		tx.commit();
		em.close();
		return auction;
	}
	@Override
	public int rate(int id, int auct_id, float value) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		UserDAOImpl userdao = new UserDAOImpl();
		tx.begin();
		User usr = userdao.find(id);
		Auction auc = this.find(auct_id);
		if(usr==null || auc==null)
			return 500;
		try {
			UserRatedAuction usrauc = new UserRatedAuction();
			UserRatedAuctionPK usraucpk = new UserRatedAuctionPK();
			usraucpk.setIdUser(id);
			usraucpk.setIdAuction(auct_id);
			usrauc.setId(usraucpk);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				java.util.Date date = formatter.parse(LocalDateTime.now().toString());
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				usrauc.setTime(sqlDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			usrauc.setRating(value);
			usrauc.setUser(usr);
			usrauc.setAuction(auc);

			List<UserRatedAuction> list = usr.getUserRatedAuctions();
			list.add(usrauc);
			usr.setUserRatedAuctions(list);

			list = auc.getUserRatedAuctions();
			list.add(usrauc);
			auc.setUserRatedAuctions(list);

			em.merge(usr);
			em.merge(auc);
			em.persist(usrauc);
		}catch (EntityExistsException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			em.close();
			return 501;
		} catch(IllegalArgumentException e){
			e.printStackTrace();
			em.close();
			return 500;
		}
		tx.commit();
		em.close();
		return 1;
	}
	@Override
	public List<Auction> find_ended() {
		// TODO Auto-generated method stub
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		UserDAOImpl userdao = new UserDAOImpl();
		MessageDAOImpl msgdao= new MessageDAOImpl();
		List<Auction> list;
		javax.persistence.Query query=null;
		query= em.createQuery("SELECT a FROM Auction a WHERE a.ends < :today");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try {
			date = formatter.parse(LocalDateTime.now().toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		query.setParameter("today", sqlDate);
		list = query.getResultList();
		for(Auction a : list){
			System.out.println("Auction ended");
			//send message to winner
			String s="Auction has ended " + a.getName();
			if(a.getUser2()!=null){
				Message msg = new Message();
				msg.setBody("Congratulations you won Auction " +
						a.getName() +". Contact seller for the transaction: "+
						a.getUser1().getUsername());
				msg.setTitle("You Won an Auction!!");
				msg.setTime(date);
				msg.setUser1(userdao.find_by_name("admin"));
				msg.setUser2(a.getUser2());
				msgdao.create(msg);
				s+=" with winner: "+a.getUser2().getUsername() + " who bidded " +
						Float.toString(a.getCurrentBid()) + ", contact him soon!";
			}
			//send message to seller
			Message msg = new Message();
			msg.setBody(s);
			msg.setTitle("Auction Ended");
			msg.setUser1(userdao.find_by_name("admin"));
			msg.setTime(date);
			msg.setUser2(a.getUser1());
			msgdao.create(msg);
			//deleting auction from base
			this.done(a);
		}
		tx.commit();
		em.close();
		return list;
	}
	//removes images and bids and auction
	@Override
	public void done(Auction auction){
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		UserDAOImpl userdao = new UserDAOImpl();
		User u;
		CopyOnWriteArrayList<Bid> alist = new CopyOnWriteArrayList<Bid>( auction.getBids());
		try{
			for(Bid b : alist){
				auction.removeBid(b);
				u=b.getUser();
				u.removeBid(b);
				Bid sapios = em.merge(b);
				em.remove(sapios);
				em.merge(u);
			}
			CopyOnWriteArrayList<Image> list = new CopyOnWriteArrayList<Image>( auction.getImages());
			for(Image i : list){
				auction.removeImage(i);
				Image img = em.merge(i);
				em.remove(img);
			}
			EndedAuction ended = new EndedAuction();
			ended.setDescription(auction.getDescription());
			ended.setItemLocation(auction.getItemLocation());
			ended.setLatitude(auction.getLatitude());
			ended.setLongitude(auction.getLongitude());
			ended.setName(auction.getName());
			ended.setUser1(auction.getUser1());
			if(auction.getUser2()!=null)
				ended.setUser2(auction.getUser2());
			else
				ended.setUser2(null);
			List<EndedAuction> elist = auction.getUser1().getEndedAuctions1();
			elist.add(ended);
			auction.getUser1().setEndedAuctions1(elist);
			System.out.println("name: "+auction.getName());
			//System.out.println("AAAAAAAAAAA"+auction.getUser2().getUsername());
			if(auction.getUser2()!=null){
				elist=auction.getUser2().getEndedAuctions2();
				elist.add(ended);
				auction.getUser2().setEndedAuctions2(elist);
			}
			Auction remover=em.merge(auction);
			em.persist(ended);
			em.merge(auction.getUser1());
			if(auction.getUser2()!=null)
				em.merge(auction.getUser2());
			em.remove(remover);
		}catch(Exception e){
			e.printStackTrace();
		}

		tx.commit();
		em.close();
	}
	@Override
	public List<Auction> list(int limit, String sort,String asc, String categ, String name, float price_from, float price_to){
		// sort->Field of Class/ asc->ASC or DESC
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		javax.persistence.Query query=null;
		String s = "SELECT u FROM Auction u WHERE u.buyPrice > :from AND u.buyPrice < :to ";
		if(categ.length()!=0){
			CategoryDAOImpl cdao= new CategoryDAOImpl();
			Category cat=cdao.search_name(categ);
			if(cat!=null){
				s = "SELECT u FROM Auction u JOIN u.categories c "
						+ "WHERE u.buyPrice > :from AND u.buyPrice < :to "
						+ "AND c.name LIKE :categ ";
			}
			else{
				System.out.println("Category doesn't exist bro");
			}
		}
		if(name.length()!=0){
			s+="AND u.name LIKE :name ";
		}
		if(sort.equals("end"))
			s+="ORDER BY u.ends ";
		else if(sort.equals("new"))
			s+="ORDER BY u.started ";
		else
			s+="ORDER BY u.buyPrice ";
		if(asc.equals("ASC"))
			s+="ASC ";
		else if(asc.equals("DESC"))
			s+="DESC ";
		query= em.createQuery(s);
		if(categ.length()!=0){
			query.setParameter("categ", categ);
		}
		if(name.length()!=0){
			query.setParameter("name", "%"+name+"%");
		}
		query.setParameter("from", price_from);
		query.setParameter("to", price_to);
		List<Auction> auctions = ((javax.persistence.Query) query).getResultList(); 
		tx.commit();
		em.close();
		return auctions;
	}

	@Override
	public int create(Auction auction, JSONArray pics) {
		// TODO Auto-generated method stub
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		CategoryDAOImpl cdao= new CategoryDAOImpl();
		tx.begin();
		List<Auction> alist = null;
		try {
			List<model.Image> ilist = auction.getImages();
			if(pics!=null){
				for(int i=0; i < pics.length();i++){
					model.Image img = new model.Image();
					try {
						img.setLink((String) pics.get(i));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					img.setAuction(auction);
					auction.addImage(img);
					em.persist(img);
					System.out.println("added image");
				}
			}
			else{
				model.Image img = new model.Image();
				img.setLink("noImage");
				img.setAuction(auction);
				auction.addImage(img);
				em.persist(img);;
			}
			em.persist(auction);
			for( Category cat : auction.getCategories()){
				alist = cat.getAuctions();
				alist.add(auction);
				cat.setAuctions(alist);
				em.merge(cat);
				//tx.commit();
			}

		} catch (EntityExistsException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			em.close();
			return 501;
		} catch(IllegalArgumentException e){
			e.printStackTrace();
			em.close();
			return 500;
		}
		tx.commit();
		em.close();
		return 200;
	}

	@Override
	public JSONArray find_user_auctions(int id){
		UserDAOImpl userdao = new UserDAOImpl();
		User u = userdao.find(id); 
		List<Auction> list = u.getAuctions1();
		if(list.isEmpty())
			return null;
		JSONArray jlist = new JSONArray();
		for(Auction a : list){
			JSONObject obj = new JSONObject();
			try {
				obj.put("id",Integer.toString(a.getId()));
				obj.put("name", a.getName());
				obj.put("cur_bid", Float.toString(a.getCurrentBid()));
			} catch (JSONException e){
				e.printStackTrace();
			}
			jlist.put(obj);
		}
		return jlist;
	}
	@Override
	public int bid_auction(int user_id, int auc_id, float bid){
		Auction auction = this.find(auc_id);
		UserDAOImpl userdao = new UserDAOImpl();
		User usr = userdao.find(user_id);
		if((auction.getCurrentBid() > bid) || (auction.getFirstBid() > bid))
			return 503;//bid lower than current
		Bid newbid = new Bid();
		newbid.setUser(usr);
		newbid.setValue(bid);
		newbid.setAuction(auction);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			java.util.Date date = formatter.parse(LocalDateTime.now().toString());
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			newbid.setTime(sqlDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BidDAOImpl biddao=new BidDAOImpl();
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			em.persist(newbid);
			List<Bid> list = auction.getBids();
			list.add(newbid);
			User usr2 = auction.getUser2();
			if(usr2!=null){
				System.out.println(usr2.getFname() + " was outbidded");
				UserEndpoint endp = new UserEndpoint();
				String msg = "User: " + usr.getUsername() + 
						" outbid you in auction " + auction.getName() +
						" by bidding " + Float.toString(bid);
				JSONObject obj = new JSONObject();
				obj.put("from", "admin");
				obj.put("to", usr2.getUsername());
				obj.put("title", "Outbidded in Auction");
				obj.put("message", msg);
				endp.send_message(obj.toString());
			}
			else
				System.out.println("you were the first bidder here");
			auction.setUser2(usr);
			auction.setCurrentBid(bid);
			auction.setNumberOfBids(auction.getNumberOfBids() + 1);
			auction.setBids(list);
			list = usr.getBids();
			list.add(newbid);
			usr.setBids(list);
			em.merge(usr);
			em.merge(auction);
		} catch (EntityExistsException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			em.close();
			return 501;
		} catch(IllegalArgumentException e){
			e.printStackTrace();
			em.close();
			return 500;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tx.commit();
		em.close();
		return 200;
	}	

}
