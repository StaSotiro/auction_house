package daos;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import jpathings.EntityManagerHelper;
import model.Message;
import model.User;

public class MessageDAOImpl implements MessageDao {
	@Override
	public Message find(int id) {
		// TODO Auto-generated method stub
		EntityManager em = EntityManagerHelper.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Message auction = em.find(Message.class, id); 
		tx.commit();
		em.close();
		return auction;
	}
	@Override
	public int get_unread_number(int id) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		UserDAOImpl userdao = new UserDAOImpl();
		tx.begin();

		int count=0;
		try {
			for(Message m : userdao.find(id).getMessages2()){
				if(m.getSeen()==false){
					m.setSeen(true);
					count++;
					em.merge(m);
				}
			}
		} catch (Exception e) {
			tx.commit();
			em.close();
			// TODO Auto-generated catch block
			return -1;//User does not exist
		}
		tx.commit();
		em.close();
		return count;
	}
	@Override
	public int create(Message message) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		UserDAOImpl usrdao = new UserDAOImpl();
		try {
			em.persist(message);
			User usr1 = usrdao.find(message.getUser1().getId());
			List<Message> list = usr1.getMessages1();
			list.add(message);
			usr1.setMessages1(list);
			User usr2 = usrdao.find(message.getUser2().getId());
			list = usr2.getMessages2();
			list.add(message);
			usr2.setMessages2(list);
			em.merge(usr1);
			em.merge(usr2);
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
	public int delete_sent(Message message) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		User u = message.getUser1();
		u.removeMessages1(message);
		Message remover = em.merge(message);
		if(message.getDeleted()==1)
			em.remove(remover);
		else{
			message.setDeleted(1);
		}
		em.merge(u);
		tx.commit();
		em.close();
		return 200;
	}
	@Override
	public int delete_inbox(Message message) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "fifth" );     
		EntityManager em = emfactory.createEntityManager( );
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		User u = message.getUser2();
		u.removeMessages2(message);
		//deleted = 0 -> noone deleted it, 1 one deleted it so we remove it from DB
		Message remover = em.merge(message);
		if(message.getDeleted()==1)
			em.remove(remover);
		else{
			message.setDeleted(1);
		}
		em.merge(u);
		tx.commit();
		em.close();
		return 200;
	}
}
