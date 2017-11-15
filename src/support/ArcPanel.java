/*
 File: ArcPanel.java
 */

package support;

import java.awt.*;
import java.awt.event.*;

public class ArcPanel extends Panel {
	private static final long serialVersionUID = 4545882506010547076L;
	Font font = new Font("Times",Font.ITALIC+Font.BOLD,24);
	Label title;
	ArcCanvas arcCanvas;
	static final int STEP = 6;
	Color arcColor = Color.blue;
	LineAdjustment l;
	
	public ArcPanel( String title ) {
		this( title, null, Color.cyan );
	}
	
	public ArcPanel( String title, CriticalSection controller ) {
		this( title, controller, Color.cyan );
	}
	
	public ArcPanel( String title, Color bgc ) {
		this( title, null, bgc );
	}
	
	public ArcPanel( String title, CriticalSection controller, Color bgc ) {
		super( new BorderLayout() );
		
		setBackground( bgc );
		
		this.title = new Label( title, Label.CENTER );
		this.title.setFont( font );
		add( this.title, BorderLayout.NORTH );
		
		Panel p = new Panel( new BorderLayout() );
		p.add( new Label( "90", Label.CENTER ), BorderLayout.NORTH );
		arcCanvas = new ArcCanvas( 0, 0, controller, arcColor );
		l = new LineAdjustment( arcCanvas );
		p.add( arcCanvas, BorderLayout.CENTER );
		p.add( new Label( "270", Label.CENTER ), BorderLayout.SOUTH );
		p.add( new Label( "0", Label.LEFT ), BorderLayout.EAST );
		p.add( new Label( "180", Label.RIGHT ), BorderLayout.WEST );
		add( p, BorderLayout.CENTER );
	}
	
	public void doStep() throws InterruptedException {
		arcCanvas.setAngle( arcCanvas.getAngle() + STEP );
	}
	
	public void rotate() throws InterruptedException {
		doStep();
	}
	
	public void setZero() throws InterruptedException {
		arcCanvas.start = 0;
		arcCanvas.setAngle( 0 );
	}
	
	public void enableChangeOfCriticalSection() {
		if ( arcCanvas.controller != null ) {
			arcCanvas.addMouseListener( l );
			arcCanvas.addMouseMotionListener( l );
			arcCanvas.enableChangeOfCriticalSection();
		}
	}
	
	public void disableChangeOfCriticalSection() throws InterruptedException {
		if ( arcCanvas.controller != null ) {
			arcCanvas.disableChangeOfCriticalSection();
			arcCanvas.removeMouseListener( l );
			arcCanvas.removeMouseMotionListener( l );
		}
	}
	
	public void setArcColor( Color c ) {
		arcColor = c;
		arcCanvas.setColor( c );
	}
	
	public Color getArcColor() {
		return arcColor;
	}
	
	public int getAngle() {
		return arcCanvas.getAngle();
	}
	
}

class LineAdjustment implements MouseListener, MouseMotionListener {
	ArcCanvas owner;
	boolean start;
	boolean stop;
	
	public LineAdjustment( ArcCanvas owner ) {
		this.owner = owner;
	}
	
	// MouseListerner
	
	public void mouseClicked( MouseEvent e ) { }
	
	public void mouseEntered( MouseEvent e ) { }
	
	public void mouseExited( MouseEvent e ) { }
	
	public void mousePressed( MouseEvent e ) {
		start = false;
		stop = false;
		
		// Is pointer near a line
		
		int selangle = owner.getSelectedAngle( e.getPoint() );
		if ( owner.isStartLineSelected( selangle ) &&
				(owner.looking == false) ) {
			start = true;
			owner.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
		} else {
			if ( owner.isStopLineSelected( selangle ) ) {
				stop = true;
				owner.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
			}
		}
	}
	
	public void mouseReleased( MouseEvent e ) {
		int newangle = owner.getSelectedAngle( e.getPoint() );
		newangle = newangle + ArcPanel.STEP - (newangle % ArcPanel.STEP);
		if ( start ) {
			owner.setStartCriticalSection( newangle );
		}
		if ( stop ) {
			owner.setStopCriticalSection( newangle );
		}
		owner.setCursor( Cursor.getDefaultCursor() );
	}
	
	// MouseMotionListener
	
	public void mouseDragged( MouseEvent e ) {
		int newangle = owner.getSelectedAngle( e.getPoint() );
		if ( start ) {
			owner.setStartCriticalSection( newangle );
		}
		if ( stop ) {
			owner.setStopCriticalSection( newangle );
		}
	}
	
	public void mouseMoved( MouseEvent e ) { }
}

class ArcCanvas extends Canvas {
	private static final long serialVersionUID = -2964481340101220083L;
	int start;
	int angle;
	Color arcColor;
	Color criticalSectionColor = Color.cyan;
	int startCriticalSection = 90;
	int stopCriticalSection = 270;
	final int EPSILON = 3;
	boolean canChangeCriticalSection = true;
	boolean repaint = false;
	CriticalSection controller;
	boolean inCriticalSection = false;
	boolean mustLock = false;
	boolean mustRelease = false;
	boolean looking = false;
	
	public ArcCanvas( int start, int angle, 
			CriticalSection controller,
			Color arcColor ) {
		super();
		this.start = start % 360;
		this.angle = angle % 360;
		this.arcColor = arcColor;
		setSize( 100, 100 );
		this.controller = controller;
	}
	
	public void enableChangeOfCriticalSection() {
		canChangeCriticalSection = true;
		mustLock = false;
		mustRelease = false;
	}
	
	public void disableChangeOfCriticalSection() throws InterruptedException {
		canChangeCriticalSection = false;
	}
	
	private void enterCriticalSection() throws InterruptedException {
		if ( controller != null ) {
			controller.enterCriticalSection();
			inCriticalSection = true;
		}
	}
	
	private void leaveCriticalSection() {
		if ( controller != null ) {
			controller.leaveCriticalSection();
			inCriticalSection = false;
		}
	}
	
	public void setStartCriticalSection( int angle ) {
		mustLock = false;
		mustRelease = false;
		if ( inCriticalSection ) {
			if ( (angle >= this.angle) &&(angle < stopCriticalSection) ) {
				mustRelease = true;
			}
			startCriticalSection = angle;
			repaint();
		} else {
			if ( !looking ) {
				if ( (angle < this.angle) || (stopCriticalSection <= angle) ) {
					mustLock = true;
				}
				startCriticalSection = angle;
				repaint();
			}
		}
	}
	
	public void setStopCriticalSection( int angle ) {
		mustLock = false;
		mustRelease = false;
		if ( inCriticalSection ) {
			if ( (angle <= this.angle) && (stopCriticalSection > startCriticalSection) ) {
				mustRelease = true;
			}
			stopCriticalSection = angle;
			repaint();
		} else {
			if ( (angle >= this.angle)
					&& ((this.angle > startCriticalSection)
							|| (angle < startCriticalSection)) )  {
				mustLock = true;
			}
			stopCriticalSection = angle;
			repaint();
		}
	}
	
	public int getSelectedAngle( Point point ) {
		int xDelta = point.x - (getSize().width / 2);
		int yDelta = ((getSize().height / 2) - point.y) ;
		double selAlpha = Math.atan2( (double)yDelta, (double)xDelta );
		return ((int)(selAlpha * 180.0 / Math.PI) + 360 ) % 360;
	}
	
	public boolean isStartLineSelected( int selangle ) {
		return ((startCriticalSection - EPSILON) <= selangle)
		&& ((startCriticalSection + EPSILON) >= selangle);
	}
	
	public boolean isStopLineSelected( int selangle ) {
		return ((stopCriticalSection - EPSILON) <= selangle)
		&& ((stopCriticalSection + EPSILON) >= selangle);
	}
	
	public void setStart( int start ) {
		this.start = start % 360;
		repaint();
	}
	
	public int getStart() {
		return start;
	}
	
	public void setAngle( int angle ) throws InterruptedException {
		// critical section has been changed manually
		if ( controller != null ) {
			if ( mustLock ) {
				enterCriticalSection();
				mustLock = false;
			}
			if ( mustRelease ) {
				leaveCriticalSection();
				mustRelease = false;
			}
		}
		
		if ( angle > 360 ) {
			this.angle = angle % 360;
			repaint = true;
		} else {
			this.angle = angle;
		}
		repaint();
		
		// critical section handling
		if ( controller != null ) {
			if ( !inCriticalSection
					&& (this.angle == startCriticalSection) ) {
				looking = true;
				enterCriticalSection();
				looking = false;
			}
			if ( inCriticalSection
					&& (this.angle == stopCriticalSection) ) {
				leaveCriticalSection();
			}
		}
	}
	
	public int getAngle() {
		return angle;
	}
	
	public void setColor( Color c ) {
		arcColor = c;
		repaint();
	}
	
	public void update( Graphics g ) {
		paint( g );
	}
	
	public void paint( Graphics g ) {
		Dimension d = getSize();
		
		// clear background if necessary
		if ( repaint || canChangeCriticalSection ) {
			super.paint( g );
			repaint = false;
		}
		
		if ( controller != null ) {
			// draw critical section
			g.setColor( criticalSectionColor );
			if ( startCriticalSection < stopCriticalSection ) {
				g.fillArc( 0, 0, d.width, d.height, 
						startCriticalSection, 
						stopCriticalSection - startCriticalSection );
			} else {
				g.fillArc( 0, 0, d.width, d.height, 
						startCriticalSection, 
						stopCriticalSection + 
						(360 - startCriticalSection) );
			}
			
			// draw actual arc
			g.setColor( arcColor );
			g.fillArc( 5, 5, d.width - 10, d.height - 10, start, angle );
			
			// draw limits
			g.setColor( Color.black );
			
			// start
			double alpha = 2 * Math.PI * startCriticalSection / 360.0;
			int r = getSize().width / 2;
			double x1 = Math.cos( alpha ) * r;
			double y1 = Math.sin( alpha ) * r;
			int zeroX = d.width / 2;
			int zeroY = d.height / 2;
			
			g.drawLine( zeroX, zeroY, zeroX + (int)x1, zeroY - (int)y1 );
			
			// stop
			alpha = 2 * Math.PI * stopCriticalSection / 360.0;
			x1 = Math.cos( alpha ) * r;
			y1 = Math.sin( alpha ) * r;
			
			g.drawLine( zeroX, zeroY, zeroX + (int)x1, zeroY - (int)y1 );
		} else {
			// draw actual arc
			g.setColor( arcColor );
			g.fillArc( 0, 0, d.width, d.height, start, angle );
		}
	}
	
}

