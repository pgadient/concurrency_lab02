package readwrite;

@SuppressWarnings("serial")
public class FairReadersWriters extends SafeReadersWriters {
	/** hook factory method to instantiate policy
	 */
	protected ReadWritePolicy makePolicy( ReaderWriterPanel view ) {
		return new FairReadWritePolicy( view ); 
	}
}
