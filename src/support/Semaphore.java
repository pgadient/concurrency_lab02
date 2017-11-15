/*
 File: Semaphore.java
 */

package support;


/********************************************************/
//
//The Semaphore Class
//up() is the V operation
//down() is the P operation
//

public class Semaphore {
	private int value;
	public Semaphore( int initial ) {
		value = initial;
	}
	
	public synchronized void up() {
		++value;
		notifyAll();  // should be notify() but does not work in some browsers
	}
	
	public synchronized void down() throws InterruptedException {
		while ( value == 0 ) 
			wait();
		value--;
	}
}

