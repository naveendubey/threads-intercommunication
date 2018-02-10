package naveen.thread.intercommunication;

import java.util.Random;

/**
 * 
 * @author :naveen
 */
public class BatsmanBowlerSync {

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String... args) {
		Printer print = new Printer();
		Thread bowlerThread = new Thread(new TaskCricket(print, false));
		bowlerThread.setName("Bowler");
		Thread batsmapThread = new Thread(new TaskCricket(print, true));
		batsmapThread.setName("Batsman");
		bowlerThread.start();
		batsmapThread.start();
	}

}

/**
 * 
 * @author :naveen
 * 
 */
class TaskCricket implements Runnable {

	// max number
	private final int BALLS_IN_OVER = 6;
	private Random random;
	// to print
	private Printer print;
	// for even number flag
	private boolean bowler;

	/**
	 * 
	 */
	public TaskCricket() {
		random = new Random();
	}

	/**
	 * 
	 * @param print
	 * @param bowler
	 */
	TaskCricket(Printer print, boolean bowler) {
		this();
		this.print = print;
		this.bowler = bowler;
	}

	@Override
	public void run() {

		int currentBall = 1;
		// till number is less than max
		while (currentBall <= BALLS_IN_OVER) {

			if (bowler) {
				// printing even number
				try {
					print.printBatsmanRun(currentBall, random.nextInt(6), Thread.currentThread().getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
					// Restore interrupted state...
					Thread.currentThread().interrupt();
				}
			} else {
				// printing odd number
				try {
					print.printBowlerBall(currentBall, Thread.currentThread().getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
					// Restore interrupted state...
					Thread.currentThread().interrupt();
				}
			}

			// Increment
			currentBall += 1;
		}

	}

}

/**
 * 
 * @author :naveen
 *
 */
class Printer {

	boolean bowler = false;

	/**
	 * 
	 * @param currentBall
	 * @param run
	 * @param threadName
	 * @throws InterruptedException
	 */
	synchronized void printBatsmanRun(int currentBall, int run, String threadName) throws InterruptedException {

		while (!bowler) {
			wait();

		}
		System.out.println(threadName + " - on ball - " + currentBall + " hits run: " + run);

		bowler = false;
		// notify waiting thread
		notifyAll();
	}

	/**
	 * 
	 * @param currentBall
	 * @param threadName
	 * @throws InterruptedException
	 */
	synchronized void printBowlerBall(int currentBall, String threadName) throws InterruptedException {
		while (bowler) {
			wait();

		}
		System.out.println(threadName + " - ball is: " + currentBall);
		bowler = true;
		// notify waiting thread
		notifyAll();
	}

}
