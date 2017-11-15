/*
 File: CriticalSection.java
 */

package support;

public interface CriticalSection {
	public void enterCriticalSection() throws InterruptedException;
	public void leaveCriticalSection();
}
