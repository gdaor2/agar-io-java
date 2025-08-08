package game;

public class CountDownLatch {
	private int countdown;

	public CountDownLatch(int countdown) {
		this.countdown = countdown;
	}

	public synchronized void countdown() {
		if (countdown == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		countdown--;
		notifyAll();
	}

	public synchronized void await() {
		while (countdown > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
