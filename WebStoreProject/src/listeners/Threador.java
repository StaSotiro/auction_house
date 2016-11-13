package listeners;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import daos.AuctionDAO;
import daos.AuctionDAOImpl;

public class Threador extends Thread implements Runnable {
	private boolean keep = true;
	@Override
	public void run() {
		AuctionDAOImpl adao = new AuctionDAOImpl();
		adao.find_ended();
		System.out.println("Sample Task checking for ending auctions");
		
		
	}
	public void killer(){
		System.out.println("i'm out bitches killer style");
		keep = false;
	}
}
