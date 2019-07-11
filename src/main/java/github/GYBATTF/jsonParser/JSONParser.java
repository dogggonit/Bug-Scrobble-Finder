package github.GYBATTF.jsonParser;

/**
 * Contains a method that parses the JSON string and
 * return a JSONObject if does not conform to JSON
 * at all but it works for the two APIs that I'm calling
 * 
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public class JSONParser implements AbstractJSONParser {
	/**
	 * Starts the parsing by passing the initial JSON to the Object creator.
	 * @param json
	 * The 
	 * @return 
	 * A JSON file in Object form
	 */
	public static JSONObject parse(String json) {
		return parseObj(json.trim().substring(1, json.length() - 1).trim());
	}
	
	/**
	 * Parses the JSON object until is has reached the end of it
	 * @param obj
	 * A JSON object in string form
	 * @return
	 * A JSON object in Object form
	 */
	private static JSONObject parseObj(String obj) {
		JSONObject rtn = new JSONObject();
		
		JSONObject tmpKey = new JSONObject();
		boolean buildKey = true;
		
		while (obj.length() != 0) {
			char firstChar = obj.charAt(0);
			
			if (buildKey && getType(firstChar) == JSONTypes.STRING) {
				tmpKey = new JSONObject();
				obj = extractString(obj, tmpKey, "key");
				buildKey = !buildKey;
			} else if (getType(firstChar) != JSONTypes.INVALID) {
				String key = tmpKey.get("key").toString();
				
				switch (getType(firstChar)) {
				case ARRAY:   obj = extractArray(obj, rtn, key); break;
				case BOOLEAN: obj = extractBoolean(obj, rtn, key); break;
				case NULL:    obj = extractNull(obj, rtn, key); break;
				case NUMBER:  obj = extractNumber(obj, rtn, key); break;
				case OBJECT:  obj = extractObject(obj, rtn, key); break;
				case STRING:  obj = extractString(obj, rtn, key); break;
				case INVALID: break;
				}
				
				buildKey = !buildKey;
			} else {
				obj = obj.substring(1).trim();
			}
		}
		
		rtn.lock();
		return rtn;
	}

	/**
	 * Extracts a null from the JSON string
	 * @param obj
	 * the object to add the null to
	 * @param rtn
	 * the JSON object to extract null value from
	 * @param tmpKey
	 * the key to add the null under
	 * @return
	 * the JSON string with the null removed
	 */
	private static String extractNull(String obj, JSONObject rtn, String tmpKey) {
		rtn.put(tmpKey, null);
		return obj.substring(5).trim();
	}

	/**
	 * Extracts an array from the JSON string
	 * @param obj
	 * the JSON string to extract the array from
	 * @param rtn
	 * the JSON object to add the array to
	 * @param tmpKey
	 * the key to add the array under
	 * @return
	 * the JSON string with the array removed
	 */
	private static String extractArray(String obj, JSONObject rtn, String tmpKey) {
		int lstEnd = -1;
		for (int j = 0, count = 0; j < obj.length(); j++) {
			char current = obj.charAt(j);
			if (current == '[') {
				count++;
			} else if (current == ']') {
				count--;
			}
			
			if (count == 0) {
				lstEnd = j;
				break;
			}
		}
		String list = obj.substring(1, lstEnd);
		rtn.put(tmpKey, parseLst(list));
		obj = obj.substring(lstEnd).trim();
		
		return obj;
	}

	/**
	 * Extracts a number from a JSON string
	 * @param obj
	 * the JSON string to extract the number from
	 * @param rtn
	 * the object to add the number to
	 * @param tmpKey
	 * the key to add the number under
	 * @return
	 * the JSON string with the number removed
	 */
	private static String extractNumber(String obj, JSONObject rtn, String tmpKey) {
		int end = obj.indexOf(',');
		rtn.put(tmpKey, new JSONNumber(obj.substring(0, end)));
		
		return obj.substring(end).trim();
	}

	/**
	 * Extracts a boolean from a JSON string
	 * @param obj
	 * the JSON string to extract the boolean from
	 * @param rtn
	 * the object to add the boolean to
	 * @param tmpKey
	 * the key to add the boolean under
	 * @return
	 * the JSON string with the boolean removed
	 */
	private static String extractBoolean(String obj, JSONObject rtn, String tmpKey) {
		rtn.put(tmpKey, new JSONBoolean((obj.charAt(0) == 't') ? "true" : "false"));
		
		return obj.substring(obj.indexOf('e') + 1).trim();
	}

	/**
	 * Extracts a JSON object from a string
	 * @param obj
	 * the JSON string to extract the object from
	 * @param rtn
	 * the object to add the object to
	 * @param tmpKey
	 * the key to add the object under
	 * @return
	 * the JSON string with the object removed
	 */
	private static String extractObject(String obj, JSONParser rtn, String tmpKey) {
		int objEnd = -1;
		for (int j = 0, count = 0; j < obj.length(); j++) {
			char current = obj.charAt(j);
			if (current == '{') {
				count++;
			} else if (current == '}') {
				count--;
			}
			
			if (count == 0) {
				objEnd = j;
				break;
			}
		}
		String object = obj.substring(1, objEnd);
		rtn.put(tmpKey, parseObj(object));
		obj = obj.substring(objEnd).trim();
		
		return obj;
	}

	/**
	 * Extract a string from the JSON string
	 * @param obj
	 * the JSON string to extract the string from
	 * @param rtn
	 * the object to add the string to
	 * @param tmpKey
	 * the key to add the string under
	 * @return
	 * the JSON string with the string removed
	 */
	private static String extractString(String obj, JSONObject rtn, String tmpKey) {
		int stringEnd = stringEnds(obj);
		String tmpValue = obj.substring(1, stringEnd);
		tmpValue = removeBackslashes(tmpValue);
		obj = obj.substring(++stringEnd).trim();
		rtn.put(tmpKey, new JSONString(tmpValue));
		
		return obj;
	}
	
	/**
	 * Remove backslashes from a string used to terminate characters
	 * @param tmpValue
	 * A string
	 * @return
	 * A string with escape characters removed
	 */
	private static String removeBackslashes(String tmpValue) {
		String rtn = "";
		
		for (int i = 0; i < tmpValue.length(); i++) {
			rtn += (tmpValue.charAt(i) == '\\') ? tmpValue.charAt(++i) : tmpValue.charAt(i);
		}

		return rtn;
	}

	/**
	 * Adds the elements in a JSON list to it
	 * @param lst
	 * the list of elements to add in text form
	 * @return
	 * a filled JSON array
	 */
	private static JSONArray parseLst(String lst) {
		JSONArray rtn = new JSONArray();
		
		while (lst.length() != 0) {
			lst = (getType(lst.charAt(0)) == JSONTypes.OBJECT) ? extractObject(lst, rtn, null) : lst.substring(1).trim();
		}
		
		rtn.lock();
		return rtn;
	}
	
	/**
	 * Finds the end of a JSON string ignoring escaped quotation marks
	 * @param s
	 * String to find the next quotation mark in
	 * @return
	 * Index of the next valid quotation mark
	 */
	private static int stringEnds(String s) {
		for (int i = 1; i < s.length(); i++) {
			char current = s.charAt(i);
			
			if (current == '\\') {
				i++;
			} else if (current == '"') {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Checks the current character and determines what type of object we are starting
	 * @param c
	 * The character to check
	 * @return
	 * The type it is in enum form
	 */
	private static JSONTypes getType(char c) {
		switch (c) {
		case '"': return JSONTypes.STRING;
		case '[': return JSONTypes.ARRAY;
		case '{': return JSONTypes.OBJECT;
		case 't': return JSONTypes.BOOLEAN;
		case 'f': return JSONTypes.BOOLEAN;
		case 'n': return JSONTypes.NULL;
		default: try {
					Integer.parseInt(Character.toString(c));
					return JSONTypes.NUMBER;
				} catch (NumberFormatException e) {
					return JSONTypes.INVALID;
				}
		}
	}
	
	/**
	 * Unused for this object
	 */
	public void put(String s, JSONParser j) {
		throw new IllegalStateException();
	}
	
	/**
	 * Unused for this object
	 */
	public JSONParser get(String s) {
		throw new IllegalStateException();
	}
}
