/*
 File: ReadersWriters.java
 */

package readwrite;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import support.*;

public class SafeReadersWriters extends Frame {
	private static final long serialVersionUID = -6859635043836986581L;
	RotatingPanel read1;
	RotatingPanel read2;
	RotatingPanel write1;
	RotatingPanel write2;
	ReadWritePolicy readerwriter;
	ReaderWriterPanel view;
	
	/** hook factory method to instantiate policy
	 */
	protected ReadWritePolicy makePolicy( ReaderWriterPanel view ) {
		return new SafeReadWritePolicy( view ); 
	}
	
	public SafeReadersWriters() {
		this("Readers Writers (Readers Priority)");
	}
	
	public SafeReadersWriters(String title) {
		super(title);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				read1.stop();
				read2.stop();
				write1.stop();
				write2.stop();
				dispose();
			}
		});
		setLayout( new BorderLayout() );
		
		view = new ReaderWriterPanel();
		add( view, BorderLayout.NORTH );
		readerwriter = makePolicy( view );  
		
		Panel p = new Panel();
		read1 = new RotatingPanel( "Reader 1", null, new Reader( readerwriter ) );
		read2 = new RotatingPanel( "Reader 2", null,  new Reader( readerwriter ) );
		write1 = new RotatingPanel( "Writer 1", null, new Writer( readerwriter ) );
		write1.setRotatorColor( Color.yellow );
		write2 = new RotatingPanel( "Writer 2", null, new Writer( readerwriter ) );
		write2.setRotatorColor( Color.yellow );
		p.add( read1 );
		p.add( read2 );
		p.add( write1 );
		p.add( write2 );
		add( p, BorderLayout.CENTER );
		
		this.setSize(800, 320);
		setResizable(false);
		this.setVisible(true);
		
		read1.start();
		read2.start();
		write1.start();
		write2.start();
	}
	
	public static void main(String[] args) {
		new SafeReadersWriters();
	}
}

