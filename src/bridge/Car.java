package bridge;

import java.awt.*;
import java.awt.image.*;

abstract class Car implements Runnable {
	final static int minDelay = 20; 
	final static int maxDelay = 50; 
	BridgeCanvas display;
	IBridge control;
	int id = 0;
	int delay; 
	boolean started = false; 
	
	/** this is the predecessor Car 
	 */
	Car otherCar;  
	Thread myThread; 
	Rectangle rec; 
	Image img; 
	
	/** a Car is an active Object,  and is run in its own thread
	 */
	Car( IBridge b, BridgeCanvas d ) {
		display = d;
		control = b;
		initPosition(); 
		resetDelay(); 
	}
	
	
	public int getId() {
		return id; 
	}
	
	private void resetDelay() {
		delay = minDelay + (int)( Math.random() * ( maxDelay-minDelay )); 
	}
	
	public void drawOffscreen( Graphics g, ImageObserver o  ) {
		g.drawImage( img, getX(), getY(), o ); 
	}
	
	/** needs to assign the image and position 
	 */
	protected abstract void initPosition(); 
	
	public int getX() {
		return rec.x; 
	}
	
	public int getY() {
		return rec.y;
	}
	
	public synchronized void initialize( int x, int y, Car predecessor ) {
		rec.setLocation( x, y); 		
		otherCar = predecessor; 
		resetDelay(); 
		if ( predecessor != null ) 
			id = predecessor.getId() + 1; 
	}
	
	public void moveTo( int x, int y  ) {
		synchronized ( this ) {
			rec.setLocation( x, y ); 
			notifyAll(); 
		}
		display.repaint(); 
	}
	
	public synchronized void start() {
		if ( started ) {
			return; 
		}
		stop(); 
		myThread = new Thread( this ); 
		myThread.setPriority( Thread.MIN_PRIORITY ); 
		myThread.start(); 
		started = true; 
	}
	
	public synchronized void stop() {
		if ( myThread != null ) { 
			Thread t = myThread; 
			myThread = null; 
			try {
				if (t != null) {
					t.interrupt(); 
				}
			} catch (Exception e) {
				System.err.println("Error while interrupting " + this); 
				e.printStackTrace(); 
			}
		}
	}
	
	public void crashes( Car other ) {
		if ( !stillDriving() ) {
			return; 
		}
		synchronized (this) {
			Rectangle intersect = rec.intersection( other.rec ); 
			if ( !intersect.isEmpty() && 
					( intersect.x > 0 ) &&
					( intersect.x < display.getRectangle().width )) 
			{
				Point here = new Point( intersect.x, ( rec.y + other.rec.y ) / 2 ); 
				other.stop(); 
				display.crash( here ); 
				myThread = null; 
			}
		}
	}
	
	public synchronized boolean stillDriving() {
		return myThread != null; 
	}
	
	protected boolean inQueue() {
		return ( otherCar != null && 
				otherCar.stillDriving() &&
				otherCar.getId() < id &&
				Math.abs( rec.x - otherCar.rec.x ) < 120);  
	}
	
	protected abstract void drive() throws InterruptedException; 
	
	protected void driveInQueue() throws InterruptedException {
		if ( !inQueue() ) {
			drive();
		} else {
			synchronized ( otherCar ) {
				otherCar.wait();  
			} 
		}
		Thread.sleep( delay );
	}; 
	
	public abstract String getCarType(); 
	
	protected abstract void enter() throws InterruptedException; 
	
	protected abstract void exit(); 
	
	protected final boolean onTheBridge() {
		return (display.onTheBridge( this )); 
	}
	
	public void run()  {
		try {
			while( myThread != null ) {
				while( stillDriving() && !onTheBridge() ) {
					driveInQueue(); 
				}
				if (stillDriving()) {
					enter();
				}
				while( stillDriving() && onTheBridge() ) {
					driveInQueue(); 
				}
				if (stillDriving()) {
					exit();
				}
			}
		} catch ( InterruptedException e ) { }
	}
	
	public String toString() {
		String s = ""; 
		if ( otherCar != null ) {
			s = " behind " + otherCar.getId(); 
		}
		return getCarType() + " " + getId() + " at [" + rec.x + "]" + s;
	}
}

