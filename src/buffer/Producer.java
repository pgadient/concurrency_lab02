/*
 File: Producer.java
 */

package buffer;

import support.*;

public class Producer implements TriggerInterface {
	Buffer buffer;
	int charval;
	RotatingPanel rotator;
	
	public Producer( Buffer buffer, RotatingPanel rotator ) {
		this.buffer = buffer;
		charval = (int)'a';
		this.rotator = rotator;
		this.rotator.setTrigger( this );
	}
	
	public void start() {
		charval = (int)'a';
		rotator.start();
	}
	
	public void stop() {
		rotator.stop();
	}
	
	public void performAction() throws InterruptedException {
		buffer.put( new Character( (char)charval ) );
		charval++;
		if ( charval > (int)'z' ) {
			charval = (int)'a';
		}
	}
	
}
