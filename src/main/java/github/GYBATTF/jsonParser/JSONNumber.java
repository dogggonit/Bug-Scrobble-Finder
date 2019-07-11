package github.GYBATTF.jsonParser;

/**
 * Hold a simple JSON number
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public class JSONNumber extends JSONValue {
	/**
	 * Creates a new new JSON number from the string. DOES NOT HOLD DOUBLES!
	 * @param value
	 */
	protected JSONNumber(String value) {
		super(value);
	}
	
	/**
	 * ENUM type of this object
	 * @return
	 * the ENUM type
	 */
	public JSONTypes getType() {
		return JSONTypes.NUMBER;
	}
	
	/**
	 * Return this object as a number
	 * @return
	 * the number held as a long
	 */
	@SuppressWarnings("unchecked")
	public Long get() {
		return Long.parseLong(toString());
	}
}
