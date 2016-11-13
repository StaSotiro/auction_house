package daos;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import jpathings.EntityManagerHelper;
import model.Bid;

public class BidDAOImpl implements BidDAO {

	@Override
	public int create(Bid bid) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try{
			em.persist(bid);
		}catch(EntityExistsException e){
			tx.commit();
			em.close();
			return 500; //already exists
		}catch(PersistenceException e){
			tx.commit();
			em.close();
			return 501;
		}catch(IllegalArgumentException e){
			tx.commit();
			em.close();
			return 502;
		}
		
		tx.commit();
		em.close();
		return 200;
	}

}
