import java.io.IOException;
import java.util.Vector;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Launcher extends Thread {
	
	private static Logger logger = Logger.getLogger("warLogger");
	
	private String 				id;
	private boolean 			isHidden;
	private boolean 			isRunning;
	private Vector<Missile> 	missiles;
	private Lock 				locker;
	private FileHandler 		fileHandler;

	public Launcher(String id, boolean isHidden, Vector<Missile> missiles)
			throws SecurityException, IOException {
		super();
		this.id = id;
		this.isHidden = true;
		this.missiles = missiles;
		this.isRunning = true;
		this.locker = new ReentrantLock();
		
		fileHandler = new FileHandler("Launcher_" + this.id + ".txt", false);
		fileHandler.setFilter(new ObjectFilter(this));
		fileHandler.setFormatter(new MyFormatter());
		logger.addHandler(this.fileHandler);
		
	}

	public Launcher(String id, boolean isHidden) throws SecurityException,
			IOException {
		super();
		this.id = id;
		this.isHidden = isHidden;
		this.missiles = new Vector<Missile>();
		this.isRunning = true;
		this.locker = new ReentrantLock();
		
		fileHandler = new FileHandler("Launcher_" + this.id + ".txt", false);
		fileHandler.setFilter(new ObjectFilter(this));
		fileHandler.setFormatter(new MyFormatter());
		logger.addHandler(this.fileHandler);	
	}

	@Override
	public void run() {
		int size = this.getMissiles().size();
		synchronized (this) {
				for(int i = 0; i < size; i++) {
					this.getMissiles().get(i).start();
				}
		}
	}
	
	public String getLauncherId() {
		return id;
	}

	public Vector<Missile> getMissiles() {
		return missiles;
	}

	public void addMissile(String id2, String destination, int launchtime,
			int flytime, int damage) {
		Missile missile = new Missile(id2, destination, launchtime, flytime, damage, this.fileHandler, this);
		this.missiles.add(missile);
	}

}
