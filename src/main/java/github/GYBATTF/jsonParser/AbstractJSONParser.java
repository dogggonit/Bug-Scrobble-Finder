package github.GYBATTF.jsonParser;

/**
 * Interface to enforce put and get methods
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
interface AbstractJSONParser {
	abstract void put(String key, JSONParser obj);
	
	abstract JSONParser get(String key);
}
