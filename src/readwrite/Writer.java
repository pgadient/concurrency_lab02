/*
 File: Writer.java
 */

package readwrite;

import support.*;

public class Writer implements CriticalSection {
	ReadWritePolicy writer;
	
	public Writer( ReadWritePolicy writer ) {
		this.writer = writer;
	}
	
	public void enterCriticalSection() throws InterruptedException {
		writer.acquireWrite();
	}
	
	public void leaveCriticalSection() {
		writer.releaseWrite();
	}
}
