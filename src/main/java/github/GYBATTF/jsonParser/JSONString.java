package github.GYBATTF.jsonParser;

/**
 * Simple class to hold a JSON string
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public class JSONString extends JSONValue {
	/**
	 * Crates a new JSON string
	 * @param value
	 * String to hold
	 */
	protected JSONString(String value) {
		super(value);
	}
	
	/**
	 * Returns the ENUM type of this object
	 * @return
	 * string ENUM
	 */
	public JSONTypes getType() {
		return JSONTypes.STRING;
	}
	
	/**
	 * Return this string
	 * @return
	 * this string
	 */
	@SuppressWarnings("unchecked")
	public String get() {
		return toString();
	}
}
