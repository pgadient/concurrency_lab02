/*
  File: Buffer.java
*/

package buffer;

public interface Buffer {
	public void put( Object o ) throws InterruptedException; 
	public Object get() throws InterruptedException;
}
