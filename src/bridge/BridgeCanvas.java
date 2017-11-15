
package bridge;

import java.awt.*;
import java.util.*; 
import java.net.URL; 
import support.SoundClips;

class BridgeCanvas extends Canvas {
	private static final long serialVersionUID = 485189025122342363L;
	Image  redCar;
	Image  blueCar;
	Image  bridge;
	Image  crashImage; 
	SoundClips clips;
	
	Vector<Car> cars = new Vector<Car>(); 
	String title = ""; 
	
	final static int initredX  = 5;
	final static int initredY  = 55;
	final static int initblueX = 440;
	final static int initblueY = 130;
	final static int bridgeY   = 90;
	
	final Vector<Point> crashed = new Vector<Point>(); 
	
	boolean newCrash = false; 
	
	Car prevBlueCar = null; 
	Car prevRedCar = null; 
	
	BridgeCanvas( Frame applet ) {
		super();
		
		clips = new SoundClips();
		
		MediaTracker mt;
		mt = new MediaTracker( this );
		
		redCar = getImage("redcar.gif" );
		mt.addImage( redCar, 0 );
		blueCar = getImage("bluecar.gif" );
		mt.addImage( blueCar, 1 );
		bridge = getImage("bridge.gif" );
		mt.addImage( bridge, 2 );
		crashImage = getImage("crash.gif" );
		mt.addImage( bridge, 3 );
		
		try {
			mt.waitForID( 0 );
			mt.waitForID( 1 );
			mt.waitForID( 2 );
			mt.waitForID( 3 );
		} catch ( java.lang.InterruptedException e ) {
			System.out.println( "Couldn't load one of the images" );
		} catch ( Exception e ) {
			System.out.println( "Unexpected error: "); 
			e.printStackTrace(); 
		}
		
		setSize( bridge.getWidth( null ),bridge.getHeight( null ) );
	}
	
	public synchronized void init( String newTitle ) { 
		cars.removeAllElements(); 
		crashed.removeAllElements(); 
		newCrash = false; 
		title = newTitle; 
		prevBlueCar = null; 
		prevRedCar = null; 
		repaint();
	}
	
	Image offscreen;
	Dimension offscreensize;
	Graphics offgraphics;
	
	public void backdrop() {
		Dimension d = getSize();
		if ( ( offscreen == null ) || ( d.width != offscreensize.width )
				|| ( d.height != offscreensize.height ) ) 
		{
			offscreen = createImage( d.width, d.height );
			offscreensize = d;
			offgraphics = offscreen.getGraphics();
			offgraphics.setFont( new Font( "Helvetica",Font.BOLD,18 ) );
			offgraphics.setColor( Color.white );
		}
		offgraphics.drawImage( bridge,0,0,this );
	}
	
	public void paint( Graphics g ) {
		update( g );
	}
	
	public Image getRedCar() {
		return redCar; 
	}
	
	public Image getBlueCar() {
		return blueCar; 
	}
	
	public void update( Graphics g ) {
		backdrop();
		offgraphics.drawString( title, 211, 20 );
		for (Car car: cars) {
			car.drawOffscreen( offgraphics, this ); 
		}
		
		for (Point point: crashed) {
			offgraphics.drawImage( crashImage, point.x-16, point.y-3, this ); 
			if (newCrash) {
				clips.crash();
				newCrash = false; 
			}
		}
		g.drawImage( offscreen, 0, 0, null );
	}
	
	public void crash( Point p ) {
		crashed.addElement( p ); 
		newCrash = true; 
	}
	
	public Rectangle getRectangle() {
		Dimension d = getSize(); 
		return new Rectangle( d.width, d.height ); 
	}
	
	public synchronized void initializeRed( RedCar c ) {
		Car old = prevRedCar; 
		prevRedCar = c; 
		c.initialize( initredX - 85, initredY, old ); 
	}
	
	public synchronized void initializeBlue( BlueCar c ) {
		Car old = prevBlueCar; 
		prevBlueCar = c; 
		c.initialize( initblueX + 85, initblueY, old ); 
	}
	

	public void registerCar( Car r ) {
		cars.addElement( r ); 
	}
	
	protected void crashTest( Car c ) {
		for (Car other: cars) {
			if ( other != c ) c.crashes( other ); 
		}
	}
	
	public void driveRed( RedCar c ) throws InterruptedException {
		int x = c.getX(); 
		int y = c.getY(); 
		x += 2;
		if ( x >=60 && x < 290 && y<bridgeY ) {
			++y;
		}
		if ( x >=290 && y>initredY ) {
			--y;
		}
		if ( x > 500 ) {
			initializeRed( c ); 
		} else {
			c.moveTo( x, y ); 
		}
		crashTest( c ); 
	}
	
	public void driveBlue( BlueCar c ) throws InterruptedException {
		int x = c.getX(); 
		int y = c.getY(); 
		x -= 2;
		if ( x <=370 && x > 130 && y>bridgeY ) {
			--y;
		}
		if ( x <=130 && y<initblueY ) {
			++y;
		}
		if ( x <= -80 ) {
			initializeBlue( c ); 
		} else {
			c.moveTo( x, y ); 
		}
		crashTest( c ); 
	}
	
	public boolean onTheBridge( Car c ) {
		int x = c.getX();  
		return ( x >= 25 && x < 400 ); 
	}

	static String imageDir = "/resources/";
	private Image getImage(String imageFile) {
		URL url = getClass().getResource(imageDir + imageFile);
		Image image = Toolkit.getDefaultToolkit().createImage(url);
		assertTrue( image != null );
		return image;
	}

	// Don't assume Java 5
	private void assertTrue(boolean b) {
		if (!b) {
			throw new Error("Assertion violation");
		}
	}
}
