package github.GYBATTF.jsonParser;

/**
 * Simple class to hold a JSON boolean
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public class JSONBoolean extends JSONValue {
	/**
	 * Creates a new JSON boolean from the given string representation
	 * @param value
	 * string of "true" or "false"
	 */
	protected JSONBoolean(String value) {
		super(value);
	}
	
	/**
	 * Returns the ENUM type of this object
	 * @return
	 * the ENUM type
	 */
	public JSONTypes getType() {
		return JSONTypes.BOOLEAN;
	}
	
	/**
	 * Returns the value held by this object as a boolean
	 * @return
	 * the boolean this object is holding
	 */
	@SuppressWarnings("unchecked")
	public Boolean get() {
		return Boolean.parseBoolean(toString());
	}
}
