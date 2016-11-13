package daos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.codehaus.jettison.json.JSONObject;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import jpathings.EntityManagerHelper;
import model.Auction;
import model.User;
import model.UserHasRatedUser;
import model.UserHasRatedUserPK;
import sun.util.locale.provider.AuxLocaleProviderAdapter;

public class UserDAOImpl implements UserDAO 
{

	@Override
	public User find(int id) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		User user = em.find(User.class, id); 
		tx.commit();
		em.close();
		return user;
	}
	@Override
	public int rate(int id, String name, float value) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		User usr = this.find(id);
		User user = this.find_by_name(name);
		if(user==null || usr==null){
			em.close();
			return 0;
		}
		
		try {
			float rate = user.getRating();
			rate = (rate*(user.getRatedBy()) + value)/(user.getRatedBy() + 1);
			user.setRatedBy(user.getRatedBy() + 1);
			user.setRating(rate);

			UserHasRatedUser usrhas = new UserHasRatedUser();
			UserHasRatedUserPK usrhaspk = new UserHasRatedUserPK();
			usrhaspk.setIdUserFrom(id);
			usrhaspk.setIdUserTo(user.getId());
			usrhas.setId(usrhaspk);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				java.util.Date date = formatter.parse(LocalDateTime.now().toString());
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				usrhas.setTime(sqlDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			usrhas.setRating(rate);
			usrhas.setUser1(usr);
			usrhas.setUser2(user);

			List<UserHasRatedUser> list = usr.getUserHasRatedUsers1();
			list.add(usrhas);
			usr.setUserHasRatedUsers1(list);

			list = user.getUserHasRatedUsers2();
			list.add(usrhas);
			user.setUserHasRatedUsers2(list);

			em.merge(usr);
			em.merge(user);
			em.persist(usrhas);
		} catch (EntityExistsException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			em.close();
			return 501;
		}catch(IllegalArgumentException e){
			e.printStackTrace();
			em.close();
			return 500;
		}
		tx.commit();
		em.close();
		return 1;
	}
	@Override
	public User find_by_name(String username) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		javax.persistence.Query query = em.createQuery(
				"SELECT u FROM User u WHERE u.username LIKE"
						+ " :username  OR u.emailAddress LIKE :email");
		query.setParameter("username", username);
		query.setParameter("email", username);
		User u = null;
		try {
			u = (User) query.getSingleResult();
		} catch (Exception e) {
			tx.commit();
			em.close();
			// TODO Auto-generated catch block
			return null;//User does not exist
		}
		tx.commit();
		em.close();

		if(u==null)
			return null;//User does not exist
		return u;
	}
	@Override
	public int verify(int id) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		User u = this.find(id);
		if(u==null)
			return 0;
		u.setValidated(true);
		em.merge(u);
		tx.commit();
		em.close();
		return 1;
	}
	@Override
	public List<User> list() {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		javax.persistence.Query query = em.createNamedQuery("User.findAll");
		List<User> users =  null;
		users = query.getResultList(); 
		tx.commit();
		em.close();
		return users;
	}

	@Override
	public List<JSONObject> jlist(){
		List<User> list=this.list();
		if(list==null)
			return null;
		List<JSONObject> kappa = null;
		for(User u : list){
			kappa.add(u.toJSON());
		}
		return kappa;		
	}
	@Override
	public int create(User user) 
	{
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			em.persist(user);
		} catch (EntityExistsException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 501;
		} catch(IllegalArgumentException e){
			e.printStackTrace();
			return 500;
		}
		tx.commit();
		em.close();
		return 200;
	}

	@Override
	public User LogIn(String username, String pass){
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		javax.persistence.Query query = em.createQuery(
				"SELECT u FROM User u WHERE u.username LIKE"
						+ " :username AND u.password LIKE :pass OR u.emailAddress LIKE :email AND u.password LIKE :pass");
		query.setParameter("username", username);
		query.setParameter("email", username);
		query.setParameter("pass", pass);

		User u;
		try {
			u = (User) query.getSingleResult();
		} catch (Exception e) {
			tx.commit();
			em.close();
			// TODO Auto-generated catch block
			return null;//User does not exist
		}
		tx.commit();
		em.close();

		if(u==null)
			return null;//User does not exist
		return u;
	}
	@Override
	public int delete_auction(int id_user, int id_auction){
		//AuctionDAOImpl adao = new AuctionDAOImpl();
		//Auction auction = adao.find((long) id_auction);
		User user = this.find(id_user);
		List<Auction> list = user.getAuctions1();
		for(Auction auction : list){
			if((int)auction.getId()==id_auction){
				if(auction!=null && auction.getNumberOfBids()==0){
					EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
					EntityManager em = emfactory.createEntityManager( );
					EntityTransaction tx = em.getTransaction();
					tx.begin();
					list.remove(auction);
					Auction remover = em.merge(auction);
					em.remove(remover);
					user.setAuctions1(list);
					tx.commit();
					em.close();
					return 1;
				}
				else{
					return 0;
				}
			}
		}
		return 1;
	}
	@Override
	public int edit_auction(int id, String desc, float buy, float first, String ends, String location) {
		AuctionDAOImpl adao = new AuctionDAOImpl();
		Auction auction = adao.find(id);
		if(auction!=null && auction.getNumberOfBids()==0){
			EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
			EntityManager em = emfactory.createEntityManager( );
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			auction.setBuyPrice(buy);
			auction.setId(id);
			auction.setDescription(desc);
			auction.setFirstBid(first);
			auction.setItemLocation(location);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				java.util.Date date = formatter.parse(ends);
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				auction.setEnds(sqlDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			em.merge(auction);
			tx.commit();
			em.close();
			return 1;
		}
		else{
			return 0;
		}
	}

}
