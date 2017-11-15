/*
 File: SafeReadWrite.java
 */

package readwrite;

public class SafeReadWritePolicy implements ReadWritePolicy {
	int readers = 0;
	boolean writing = false;
	ReaderWriterPanel view;
	
	public SafeReadWritePolicy( ReaderWriterPanel view ) {
		this.view = view;
	}
	
	public synchronized void acquireRead() throws InterruptedException {
		while ( writing ) {
			wait();
		}
		readers++;
		view.setReader( readers );
	}
	
	public synchronized void releaseRead() {
		readers--;
		view.setReader( readers );
		if ( readers == 0 ) {
			notifyAll();
		}
	}
	
	public synchronized void acquireWrite() throws InterruptedException {
		while ( readers > 0 || writing ) {
			wait();
		}
		writing = true;
		view.setWriter();
	}
	
	public synchronized void releaseWrite() {
		writing = false;
		view.clearWriter();
		notifyAll();
	}
}
