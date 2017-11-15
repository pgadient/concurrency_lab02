/*
 File: RotatorPanel.java
 */

package support;

import java.awt.*;
import java.awt.event.*;

public class RotatingPanel extends Panel implements Runnable {
	private static final long serialVersionUID = -8611926547357188270L;
	ArcPanel arcPanel;
	Button run;
	Button pause;
	Scrollbar slider;
	static Color runcolor = Color.green;
	static Color pausecolor = Color.red;
	Thread thread;
	boolean running;
	boolean canrotate;
	int speed = 100;
	TriggerInterface obj;
	
	public RotatingPanel( String title ) {
		this( title, null, null );
	}
	
	public RotatingPanel( String title, TriggerInterface obj ) {
		this( title, obj, null );
	}
	
	public RotatingPanel( String title, TriggerInterface obj,	CriticalSection controller ) {
		super( new BorderLayout() );
		this.obj = obj;
		slider = new Scrollbar( Scrollbar.HORIZONTAL, 100, 5, 5, 200  );
		slider.addAdjustmentListener( new SliderListener( this ) );
		Panel p = new Panel();
		run = new Button( "Run" );
		run.addActionListener( new RunListener( this ) );
		pause = new Button( "Pause" );
		pause.addActionListener( new PauseListener( this ) );
		p.add( run );
		p.add( pause );
		
		arcPanel = new ArcPanel( title, controller );
		add( arcPanel, BorderLayout.NORTH );
		add( slider, BorderLayout.CENTER );
		add( p, BorderLayout.SOUTH );
	}
	
	public void run() {
		while (running) {
			try {
				synchronized(this) {
					while( !canrotate ) {
						wait();
					}
					// synchronization lock is released
					// and thread is suspended
				}
				arcPanel.rotate();
				if ( obj != null ) {
					if ( arcPanel.getAngle() == 360 ) {
						// performAction may block and raise
						// the exception InterruptedException
						obj.performAction();
					}
				}
				Thread.sleep( speed );
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	public void setSpeed( int speed ) {
		this.speed = speed;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setTrigger( TriggerInterface trigger ) {
		obj = trigger;
	}
	
	public void setRotatorColor( Color c ) {
		arcPanel.setArcColor( c );
	}
	
	public Color getRotatorColor() {
		return arcPanel.getArcColor();
	}
	
	public void setPauseColor( Color c ) {
		pausecolor = c;
		repaint();
	}
	
	public Color getPauseColor() {
		return pausecolor;
	}
	
	public void setZero() throws InterruptedException {
		arcPanel.setZero();
		setInActive();
	}
	
	public synchronized void setActive() {
		try {
			canrotate = true;
			run.setEnabled( false );
			pause.setEnabled( true );
			arcPanel.setBackground( runcolor );
			arcPanel.disableChangeOfCriticalSection();
			notifyAll();
		} catch ( InterruptedException e ) {
			return;
		}
	}
	
	public synchronized void setInActive() {
		canrotate = false;
		run.setEnabled( true );
		pause.setEnabled( false );
		arcPanel.setBackground( pausecolor );
		arcPanel.enableChangeOfCriticalSection();
	}
	
	public void start() {
		try {
			setZero();
			slider.setValue( 100 );
			setSpeed( 100 );
			running = true;
			canrotate = false;
			thread = new Thread( this );
			thread.start();
		}
		catch ( InterruptedException e ) {
			System.out.println( "start of rotating panel failed!" );
			return;
		}
	}
	
	public void stop() {
		running = false;
	}
	
	class RunListener implements ActionListener {
		RotatingPanel owner;
		public RunListener( RotatingPanel owner ) {
			this.owner = owner;
		}
		public void actionPerformed( ActionEvent evt ) {
			owner.setActive();
		}
	}
	
	class PauseListener implements ActionListener {
		RotatingPanel owner;
		public PauseListener( RotatingPanel owner ) {
			this.owner = owner;
		}
		public void actionPerformed( ActionEvent evt ) {
			owner.setInActive();
		}
	}
	
	class SliderListener implements AdjustmentListener {
		RotatingPanel owner;
		public SliderListener( RotatingPanel owner ) {
			this.owner = owner;
		}
		public void adjustmentValueChanged( AdjustmentEvent evt ) {
			owner.setSpeed( evt.getValue() );
		}
	}
}
