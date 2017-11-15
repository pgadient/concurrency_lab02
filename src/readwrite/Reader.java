/*
 File: Reader.java
 */

package readwrite;

import support.*;

public class Reader implements CriticalSection {
	ReadWritePolicy reader;
	
	public Reader( ReadWritePolicy reader ) {
		this.reader = reader;
	}
	
	public void enterCriticalSection() throws InterruptedException {
		reader.acquireRead();
	}
	
	public void leaveCriticalSection() {
		reader.releaseRead();
	}
}
