package naveen.thread.intercommunication;

/**
 * 
 * @author :naveen
 */
public class PrintEvenOdd {

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String... args) {
		int maxNumber = 100;
		Printer print = new Printer();
		Thread oddThread = new Thread(new TaskEvenOdd(print, maxNumber, false));
		oddThread.setName("ODD THREAD");
		Thread evenThread = new Thread(new TaskEvenOdd(print, maxNumber, true));
		evenThread.setName("EVEN THREAD");
		oddThread.start();
		evenThread.start();
	}

}

/**
 * 
 * @author :naveen
 * 
 */
class TaskEvenOdd implements Runnable {

	// max number
	private int max;
	// to print
	private Printer print;
	// for even number flag
	private boolean evenNumber;

	/**
	 * 
	 * @param print
	 * @param max
	 * @param evenNumber
	 */
	TaskEvenOdd(Printer print, int max, boolean evenNumber) {
		this.print = print;
		this.max = max;
		this.evenNumber = evenNumber;
	}

	@Override
	public void run() {

		int number = evenNumber ? 2 : 1;
		// till number is less than max
		while (number <= max) {

			if (evenNumber) {
				// printing even number
				try {
					print.printEven(number, Thread.currentThread().getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
					// Restore interrupted state...
					Thread.currentThread().interrupt();
				}
			} else {
				// printing odd number
				try {
					print.printOdd(number, Thread.currentThread().getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
					// Restore interrupted state...
					Thread.currentThread().interrupt();
				}
			}

			// Increment
			number += 2;
		}

	}

}

/**
 * 
 * @author :naveen
 *
 */
class Printer {

	boolean isOdd = false;

	/**
	 * 
	 * @param number
	 * @param threadName
	 * @throws InterruptedException
	 */
	synchronized void printEven(int number, String threadName) throws InterruptedException {

		while (!isOdd) {
			wait();

		}
		System.out.println(threadName + " - " + number);

		isOdd = false;
		// notify waiting thread
		notifyAll();
	}

	/**
	 * 
	 * @param number
	 * @param threadName
	 * @throws InterruptedException
	 */
	synchronized void printOdd(int number, String threadName) throws InterruptedException {
		while (isOdd) {
			wait();

		}
		System.out.println(threadName + " - " + number);
		isOdd = true;
		// notify waiting thread
		notifyAll();
	}

}
