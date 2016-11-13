package daos;

import java.util.List;

import javax.management.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import jpathings.EntityManagerHelper;
import model.Category;

public class CategoryDAOImpl implements CategoryDAO {

	@Override
	public Category find(long id) {
		// TODO Auto-generated method stub
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Category category = em.find(Category.class, id); 
		tx.commit();
		em.close();
		return category;
	}

	@Override
	public List<Category> list() {
		// TODO Auto-generated method stub
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		javax.persistence.Query query =  em.createNamedQuery("Category.findAll");
		@SuppressWarnings("unchecked")
		List<Category> categories = ((javax.persistence.Query) query).getResultList();
		tx.commit();
		em.close();
		return categories;
	}

	@Override
	public int create(Category category) {
		// TODO Auto-generated method stub
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try{
			em.persist(category);
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
	@Override
	public Category search_name(String name){
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		javax.persistence.Query query = em.createQuery(
				"SELECT c FROM Category c WHERE c.name LIKE :str");
		query.setParameter("str",name);
		Category c;
		try {
			c = (Category) query.getSingleResult();
		} catch (Exception e) {
			tx.commit();
			em.close();
			// TODO Auto-generated catch block
			return null;//Category does not exist
		}
		tx.commit();
		em.close();

		if(c==null)
			return null;//Category does not exist
		return c;

	}
}
