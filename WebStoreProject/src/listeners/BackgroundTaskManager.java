package listeners;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class BackgroundTaskManager implements ServletContextListener {
	private ScheduledExecutorService scheduler;



	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("NAI MWRH");
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Threador(), 0, 5, TimeUnit.MINUTES);
	}
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//threador.interrupt();
		//threador.killer();
		scheduler.shutdown();
	}



}
