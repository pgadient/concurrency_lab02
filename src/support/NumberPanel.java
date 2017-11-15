/*
 File: NumberPanel.java
 */

package support;

import java.awt.*;

public class NumberPanel extends Panel {
	private static final long serialVersionUID = 5150921847173840137L;
	Label value;
	Label title;
	Font f1 = new Font("Helvetica",Font.BOLD,36);
	Font f2 = new Font("Times",Font.ITALIC+Font.BOLD,24);
	
	public NumberPanel( String title ) {
		this( new BorderLayout(), title, Color.cyan );
	}
	
	public NumberPanel( String title, Color bgc ) {
		this( new BorderLayout(), title, bgc );
	}
	
	public NumberPanel( LayoutManager layout, String title ) {
		this( layout, title, Color.cyan );
	}
	
	public NumberPanel( LayoutManager layout, String title, Color bgc ) {
		super( layout );
		setBackground( bgc );
		value = new Label( "0", Label.CENTER );
		value.setFont( f1 );
		this.title = new Label( title, Label.CENTER );
		this.title.setFont( f2 );
		add( this.title, "North" );
		add( value, "South" );
	}
	
	public void setValue( int value ) {
		this.value.setText( String.valueOf( value ) );
	}
	
	public int getValue() {
		return (new Integer( value.getText() ) ).intValue();
	}
	
	public void setTitle( String title ) {
		this.title.setText( title );
	}
	
}
