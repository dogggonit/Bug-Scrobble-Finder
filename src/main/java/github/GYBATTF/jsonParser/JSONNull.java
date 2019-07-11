package github.GYBATTF.jsonParser;

/**
 * Simple class to hold a JSON null
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public class JSONNull extends JSONValue {
	/**
	 * Creates a new json null
	 */
	protected JSONNull() {
		super("null");
	}
	
	/**
	 * Returns the ENUM type
	 * @return
	 * the ENUM type of null
	 */
	public JSONTypes getType() {
		return JSONTypes.NULL;
	}
	
	/**
	 * Returns this object as a null
	 * @return
	 * returns null
	 */
	@SuppressWarnings("unchecked")
	public Object get() {
		return null;
	}
}
