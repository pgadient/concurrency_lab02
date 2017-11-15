package readwrite;

@SuppressWarnings("serial")
public class WritersPriority extends SafeReadersWriters {
	/** hook factory method to instantiate policy
	 */
	protected ReadWritePolicy makePolicy( ReaderWriterPanel view ) {
		return new WritersPriorityPolicy( view ); 
	}
}
