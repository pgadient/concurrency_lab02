/********************************************************/
//
// SimpleAllocator Class
//
package golf;

public class SimpleAllocator implements IAllocator {
	int available;
	final int capacity; 

	public SimpleAllocator(int n) { 
		capacity = n; 
		available = n; 
	}

	synchronized public void get(int n) throws InterruptedException {
		while ( n > available ) {
			wait();
		}
		available -= n;
	}

	synchronized public void put(int n) {
		available += n;
		notifyAll();
	}

	public int getCapacity() {
		return capacity; 
	}

	public String getTitle() {
		return this.getClass().getName();
	}
 }
