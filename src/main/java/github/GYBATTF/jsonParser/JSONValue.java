package github.GYBATTF.jsonParser;

/**
 * A simple class used to store a string value from the JSON
 * Needed to use this method to allow objects and arrays to 
 * also be store in a JSONObject
 * 
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
abstract class JSONValue extends JSONParser {
	private String value;

	/**
	 * Creates an object with the specified value
	 * @param value
	 */
	protected JSONValue(String value) {
		this.value = value;
	}
	
	/**
	 * Returns this object in string form
	 * @return 
	 * string version of this object
	 */
	@Override
	public String toString() {
		return value;
	}
	
	/**
	 * Gets the object as whatever type it is
	 * @return
	 * the object
	 */
	abstract <T> T get();
}
