//@author: j.n.magee 11/11/96
//adapted  acherman
package golf;

import java.awt.*;

/**
 *  Wrapper Class to visualize the state of an Allocator
 */
class DisplayAllocator extends Panel implements IAllocator {
	private static final long serialVersionUID = -6049075384091147256L;
	public final static Font TEXT_FONT = new Font("Helvetica",Font.BOLD,36);
	public final static Font TITLE_FONT = new Font("Times",Font.ITALIC+Font.BOLD,24);

	final Label contents; 
	final Label title; 

	final IAllocator alloc;
	private int hiredout  =  0;
	private int available;

	DisplayAllocator( IAllocator a ) {
		super(new BorderLayout());

		alloc  =  a;
		available = getCapacity();

		title = new Label(alloc.getTitle(), Label.CENTER); 
		title.setFont(TITLE_FONT); 
		contents = new Label("", Label.CENTER); 
		contents.setFont(TEXT_FONT); 
		add(title, BorderLayout.NORTH); 
		add(contents, BorderLayout.CENTER); 
		display(); 
	}

	private void display() {
		contents.setText( "available =  " + String.valueOf( available )
						+ "  hired out =  " + String.valueOf( hiredout ) );
	}

	/** required for IAllocator
	 */
	public void get( int n ) throws InterruptedException {
		 alloc.get( n );
		 synchronized ( this ) {
			 available -= n;
			 hiredout += n;
		 }
		 display();
	}

	/** required for IAllocator
	 */
	public void put( int n ) {
		 alloc.put( n );
		 synchronized ( this ) {
			 hiredout -= n;
			 available += n;
		 }
		 display();
	}

	public int getCapacity() {
		return alloc.getCapacity(); 
	}

	public String getTitle() {
		return title.getText(); 
	}
 }

