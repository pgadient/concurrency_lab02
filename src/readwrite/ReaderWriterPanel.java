/*
 File: ReaderWriterPanel.java
 */

package readwrite;

import java.awt.*;

public class ReaderWriterPanel extends Panel {
	private static final long serialVersionUID = -258487467326230796L;
	Font font = new Font("Times",Font.ITALIC+Font.BOLD,24);
	Label readers;
	Label writers;
	
	public ReaderWriterPanel() {
		setBackground( Color.cyan );
		setFont( font );
		add( new Label( "Readers: " ) );
		readers = new Label( "0" );
		add( readers );
		add( new Label( "	 " ) );
		add( new Label( "Writer: " ) );
		writers = new Label( "false" );
		add( writers );
	}
	
	public void setWriter() {
		writers.setText( "true" );
	}
	
	public void clearWriter() {
		writers.setText( "false" );
	}
	
	public void setReader( int n ) {
		readers.setText( String.valueOf( n ) );
	}
}
