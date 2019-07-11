package github.GYBATTF.jsonParser;

import java.util.HashMap;

/**
 * Simple method to store key value pairs from the JSON file
 * 
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public class JSONObject extends JSONParser {
	private HashMap<String, JSONParser> object;
	private boolean locked;

	/**
	 * Creates a new JSONObject
	 */
	protected JSONObject() {
		object = new HashMap<>();
		locked = false;
	}
	
	/**
	 * Locks the object
	 */
	protected void lock() {
		locked = true;
	}
	/**
	 * Retrieves a value by key
	 * @param key
	 * to to get value for
	 * @return
	 * the value at that key
	 */
	@Override
	public JSONParser get(String key) {
		return object.get(key);
	}
	
	/**
	 * Adds a value to this object
	 * @param key
	 * key to add to
	 * @param value
	 * value to add at that key
	 */
	@Override
	public void put(String key, JSONParser obj) {
		if (!locked) {
			object.put(key, obj);
		}
	}
}
