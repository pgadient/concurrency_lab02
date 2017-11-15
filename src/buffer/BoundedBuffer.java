/*
 File: BoundedBuffer.java
 */

package buffer;

public class BoundedBuffer implements Buffer {
	private Object buffer[];
	private int size;
	private int count;
	private int in;
	private int out;
	
	public BoundedBuffer( int size ) {
		this.size = size;
		init();
	}
	
	public void init() {
		buffer = new Object[size];
		count = 0;
		in = 0;
		out = 0;
	}
	
	public synchronized void put( Object o ) throws InterruptedException {
		buffer[in] = o;
		count++;
		in = (in + 1) % size;
	}
	
	public synchronized Object get() throws InterruptedException {
		Object o = buffer[out];
		buffer[out] = null;
		count--;
		out = (out + 1) % size;
		return o;
	}
	
	int getIn() {
		return in;
	}
	
	int getOut() {
		return out;
	}
	
	char[] getCharBuffer() {
		char[] result = new char[size];
		
		for ( int i = 0; i < size; i++ ) {
			if ( buffer[i] != null ) {
				try {
					result[i] = ((Character)buffer[i]).charValue();
				} catch (Exception e) {
					result[i] = ' ';
				}
			} else {
				result[i] = ' ';
			}
		}
		return result;
	}
	
}
