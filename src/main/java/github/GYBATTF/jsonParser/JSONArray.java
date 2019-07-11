package github.GYBATTF.jsonParser;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple object used to store a JSON array as a 
 * subtype of the JSON parser
 * 
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public class JSONArray extends JSONParser implements Iterable<JSONObject> {
	private ArrayList<JSONObject> array;
	private boolean locked;

	/**
	 * Creates a new JSONArray
	 */
	protected JSONArray() {
		array = new ArrayList<>();
		locked = false;
	}
	
	/**
	 * Locks the array from adding elements
	 */
	protected void lock() {
		locked = true;
	}
	
	/**
	 * Adds an element to the array
	 * @param anything
	 * required by the interface, does nothing for this method
	 * @param obj
	 * JSON object to add to this list
	 */
	@Override
	public void put(String anything, JSONParser obj) {
		put(obj);
	}
	
	/**
	 * Correct method for adding to a JSONArray
	 * @param obj
	 * object to add to the array
	 */
	public void put(JSONParser obj) {
		if (!locked) {
			array.add((JSONObject) obj);
		}
	}
	
	/**
	 * Gets an element from the array
	 * @param i
	 * index to get from
	 * @return
	 * the object at that index
	 */
	public JSONParser get(int i) {
		return array.get(i);
	}
	
	/**
	 * Returns the object at the string representation of the index
	 * @param i
	 * index to get from
	 * @return
	 * object at that index
	 */
	@Override
	public JSONParser get(String i) {
		return get(Integer.parseInt(i));
	}
	
	/**
	 * Gets the size of the array
	 * @return
	 * size of the array
	 */
	public int size() {
		return array.size();
	}

	/**
	 * Simply passes the iterator from the underlying arraylist
	 * @return
	 * an iterator
	 */
	@Override
	public Iterator<JSONObject> iterator() {
		return array.iterator();
	}
}
