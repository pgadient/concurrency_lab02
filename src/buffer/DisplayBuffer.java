/*
 File: DisplayBuffer.java
 */

package buffer;

import support.*;

public class DisplayBuffer implements Buffer {
	private BufferPanel view;
	private BoundedBuffer buffer;
	private Semaphore full;  // counts number of items
	private Semaphore empty; // counts number of spaces
	private int size;
	
	public DisplayBuffer( int size ) {
		this.size = size;
		view = new BufferPanel( "Buffer", size );
		buffer = new BoundedBuffer( size );
		full = new Semaphore( 0 );
		empty = new Semaphore( this.size );
	}
	
	public synchronized void put( Object o ) throws InterruptedException {
		empty.down();
		buffer.put( o );
		updateView();
		full.up();
	}
	
	public synchronized Object get() throws InterruptedException {
		full.down();
		Object o = buffer.get();
		updateView();
		empty.up();
		return o;
	}
	
	public void init() {
		full = new Semaphore( 0 );
		empty = new Semaphore( size );
		buffer.init();
		updateView();
	}
	
	private void updateView() {
		view.setValue( buffer.getCharBuffer(), 
				buffer.getIn(), 
				buffer.getOut() );
	}
	
	BufferPanel getView() {
		return view;
	}
	
	Buffer getBuffer() {
		return buffer;
	}
	
}
