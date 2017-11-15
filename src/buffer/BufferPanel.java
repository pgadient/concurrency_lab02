/*
 File: BufferCanvas.java
 */

package buffer;

import java.awt.*;

class BufferPanel extends Panel {
	private static final long serialVersionUID = 6507824516121656333L;
	Font font = new Font("Times",Font.ITALIC+Font.BOLD,24);
	Label title;
	BufferCanvas bufCanvas;
	
	public BufferPanel( String title, int slots ) {
		this( new BorderLayout(), title, Color.cyan, slots );
	}
	
	public BufferPanel( String title, Color bgc, int slots ) {
		this( new BorderLayout(), title, bgc, slots );
	}
	
	public BufferPanel( LayoutManager layout, String title, int slots ) {
		this( layout, title, Color.cyan, slots );
	}
	
	public BufferPanel( LayoutManager layout, String title, Color bgc, int slots ) {
		super( layout );
		setBackground( bgc );
		
		this.title = new Label( title, Label.CENTER );
		this.title.setFont( font );
		add( this.title, BorderLayout.NORTH );
		
		bufCanvas = new BufferCanvas( slots, bgc );
		add( bufCanvas, BorderLayout.CENTER );
	}
	
	public void setValue( char[] buf, int in, int out ) {
		bufCanvas.setValue( buf, in, out );
	}
	
}

class BufferCanvas extends Canvas {
	private static final long serialVersionUID = 8524400770828019902L;
	int slots;
	int in = 0;
	int out = 0;
	char[] buf;
	Font font = new Font( "TimesRoman", Font.BOLD, 36 );
	
	BufferCanvas( int slots, Color bgc ) {
		super();
		this.slots = slots;
		buf = new char[slots];
		for ( int i = 0; i < slots; i++ ) {
			buf[i] = ' ';
		}
		setSize( 20 + 50 * this.slots, 144 );
		setBackground( bgc );
	}
	
	public void setValue( char[] buf, int in, int out ) {
		this.buf = buf;
		this.in = in;
		this.out = out;
		repaint();
	}
	
	public void paint( Graphics g ) {
		update( g );
	}
	
	Image offscreen;
	Dimension offscreensize;
	Graphics offgraphics;
	
	public synchronized void update( Graphics g ) {
		// prepare offgraphics
		Dimension d = getSize();
		if ( (offscreen == null) || 
				(d.width != offscreensize.width) || 
				(d.height != offscreensize.height) ) {
			offscreen = createImage(d.width, d.height);
			offscreensize = d;
			offgraphics = offscreen.getGraphics();
			offgraphics.setFont( getFont() );
		}
		
		offgraphics.setColor( getBackground() );
		offgraphics.fillRect( 0, 0, d.width, d.height );
		
		// Buffer Boxes
		int y = d.height/2 - 25;
		offgraphics.setColor( Color.white );
		offgraphics.fillRect( 10, y, 50 * slots, 50 );
		offgraphics.setColor( Color.black );
		offgraphics.setFont( font );
		for( int i = 0; i < slots; i++ ) {
			offgraphics.drawRect( 10 + 50 * i, y, 50, 50 );
			offgraphics.drawChars( buf, i, 1, 25 + 50 * i, y + 35 );
		}
		
		//Input and output Pointers
		offgraphics.setColor( Color.blue );
		offgraphics.fillOval( 28 + 50 * in, y - 20, 15, 15 );
		offgraphics.setColor( Color.yellow );
		offgraphics.fillOval( 28 + 50 * out, y + 55, 15, 15 );
		g.drawImage( offscreen, 0, 0, null );
	}
	
}

