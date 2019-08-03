package github.GYBATTF.apiCaller;

import java.io.Console;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import github.GYBATTF.jsonParser.JSONArray;
import github.GYBATTF.jsonParser.JSONObject;
import github.GYBATTF.main.Statics;
import github.GYBATTF.tracks.Track;

/**
 * Class to make API calls to your instance of subsonic
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public class Subsonic extends AbstractDownloader implements Serializable {
	private static final long serialVersionUID = -666962305682379486L;
	
	private String baseUrl;
	
	private String password;
	private String salt;
	private String username;
	private String url;
	
	/**
	 * Blank constructor for serializable
	 */
	public Subsonic() {
		baseUrl = "";
		password = "";
		salt = "";
		username = "";
		url = "";
	}
	
	/**
	 * Creates a new object to make calls to subsonic
	 * @param username
	 * subsonic username to use
	 * @param domain
	 * the domain to make calls to in the format of http://www.example.com/subsonic/
	 * @param message
	 * message to use when prompting for the password
	 */
	public Subsonic(String username, String domain, String message) {
		if (domain.charAt(domain.length() - 1) != '/') {
			domain += '/';
		}
		
		baseUrl = domain + "rest/search3.view";
		baseUrl += "?u=" + username;
		String salt = genSalt(14);
		baseUrl += "&t=" + md5(promptForPassword(message), salt);
		baseUrl += "&s=" + salt;
		baseUrl += "&query=%s";
		baseUrl += "&albumCount=0&songCount=%d&artistCount=0";
		baseUrl += "&v=1.15.0";
		baseUrl += "&c=RunsFinder";
		baseUrl += "&f=json";
	}
	
	/**
	 * Searches your subsonic library for a specific song 
	 * @param match
	 * track to get search terms from
	 * @param results
	 * number of results to fetch
	 * @return 
	 * -1 if an exact match was found, else return the number of results found
	 * @throws UnsupportedEncodingException 
	 */
	public Track[] search(Track match, int results) {
		ArrayList<Track> rtn = new ArrayList<>();
		String query = null;
		try {
			query = match.get(Statics.NAME);
			query += " " + match.get(Statics.ARTIST);
			query = URLEncoder.encode(query, Statics.ENCODING);
		} catch (UnsupportedEncodingException e) {
			// Should never get here
		}
		String url = String.format(baseUrl, query, results);
		JSONObject returned = download(url);
		returned = (JSONObject) returned.get("subsonic-response").get("searchResult3");
		
		if (returned.get("song") == null) {
			return new Track[0];
		}
		
		results = ((JSONArray) returned.get("song")).size();
		Iterator<JSONObject> it = ((JSONArray) returned.get("song")).iterator();
		while (it.hasNext()) {
			JSONObject re = it.next();
			Track tr = new Track();
			
			tr.put(Statics.NAME, re.get("title").toString());
			tr.put(Statics.ARTIST, re.get(Statics.ARTIST).toString());
			tr.put(Statics.ALBUM, re.get(Statics.ALBUM).toString());
			tr.put(Statics.DURATION, re.get(Statics.DURATION).toString());
			
			rtn.add(tr);
 		}
		
		Track[] a = rtn.toArray(new Track[0]);
		return a;
	}
	
	/**
	 * Creates an md5 hash of the password from a salt for use in the api calls
	 * @param password
	 * your subsonic password
	 * @param salt
	 * the salt to hash with
	 * @return
	 * the hashed password
	 */
	private static String md5(String password, String salt) {
		String hashed = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes(Statics.ENCODING));
			md.update(salt.getBytes(Statics.ENCODING));

			for (byte b : md.digest()) {
				hashed += String.format("%02x", b);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashed;
	}
	
	/**
	 * Creates a random salt
	 * @param length
	 * how many characters long you want the salt to be
	 * @return
	 * the salt
	 */
	private static String genSalt(int length) {
		Random r = new Random();
		String gen = "";

		for (int i = 0; i <= length; i++) {
			gen += Statics.CHARS.charAt(r.nextInt(Statics.CHARS.length()));
		}
		
		return gen;
	}
	
	/**
	 * Prompts for the password.  If possible uses the console for secure reading else allows for insecure reading with a warning
	 * @param message
	 * message to use when prompting
	 * @return
	 * the password
	 */
	@SuppressWarnings("resource")
	private static String promptForPassword(String message) {
		String password;
		Console passIn = System.console();
		if (passIn == null) {
			System.out.println("!!!!!READING PASSWORD INSECURELY!!!!!");
			System.out.print(message);
			Scanner sc = new Scanner(System.in);
			password = sc.next();
		} else {
			password = "";
			char[] in = passIn.readPassword("%s", message);
			;
			for (char c : in) {
				password += c;
			}
			in = new char[0];
		}

		return password;
	}
	
	public String getPassword() {
		return password.equals("") ? "" : "********";
	}

	public void setPassword(String password) {
		salt = genSalt(14);
		this.password = md5(password, salt);
		buildURL();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		buildURL();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		buildURL();
	}
	
	private void buildURL() {
		baseUrl = "";
		
		if (url.length() > 0 && url.charAt(url.length() - 1) != '/') {
			url += '/';
		}
		
		baseUrl = url + "rest/search3.view";
		baseUrl += "?u=" + username;
		baseUrl += "&t=" + password;
		baseUrl += "&s=" + salt;
		baseUrl += "&query=%s";
		baseUrl += "&albumCount=0&songCount=%d&artistCount=0";
		baseUrl += "&v=1.15.0";
		baseUrl += "&c=RunsFinder";
		baseUrl += "&f=json";
	}
	
	
}
