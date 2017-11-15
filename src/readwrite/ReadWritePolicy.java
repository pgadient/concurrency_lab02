/*
 File: ReadWrite.java
 */

package readwrite;

//************************************************************
//the interface for ReadWrite monitor implementations

interface ReadWritePolicy {
	public void acquireRead() throws InterruptedException;
	public void releaseRead();
	public void acquireWrite() throws InterruptedException;
	public void releaseWrite();
}
