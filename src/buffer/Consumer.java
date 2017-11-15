/*
 File: Consumer.java
 */

package buffer;

import support.*;

public class Consumer implements TriggerInterface {
	Buffer buffer;
	RotatingPanel rotator;
	
	public Consumer( Buffer buffer, RotatingPanel rotator ) {
		this.buffer = buffer;
		this.rotator = rotator;
		this.rotator.setTrigger( this );
	}
	
	public void start() {
		rotator.start();
	}
	
	public void stop() {
		rotator.stop();
	}
	
	public void performAction() throws InterruptedException {
		buffer.get();
	}
	
}
