package github.GYBATTF.apiCaller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import github.GYBATTF.jsonParser.JSONArray;
import github.GYBATTF.jsonParser.JSONObject;
import github.GYBATTF.jsonParser.JSONParser;
import github.GYBATTF.jsonParser.JSONString;
import github.GYBATTF.main.ProgressBar;
import github.GYBATTF.main.Statics;
import github.GYBATTF.tracks.Track;
import github.GYBATTF.tracks.TrackList;

/**
 * Class to make API calls to last.fm
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public class LastFM extends AbstractDownloader implements Serializable {
	private static final long serialVersionUID = -4994362501807665685L;

	private String fillInString;
	public String apiKey;
	public String username;
	
	/**
	 * Blank constructor for use by serializable.
	 * If used otherwise no calls will work
	 */
	public LastFM() {
		apiKey = Statics.BLANK;
		username = Statics.BLANK;
		fillInString = Statics.BLANK;
	}
	
	/**
	 * Creates a new object to make calls to last.fm
	 * @param apiKey
	 * your last.fm API key
	 * @param username
	 * the last.fm username to download history for
	 */
	public LastFM(String apiKey, String username) {
		this.apiKey = apiKey;
		this.username = username;
		fillInString = String.format(Statics.LAST_FM_BASE_URL, Statics.STR, username, apiKey, Statics.STR);
	}
	
	/**
	 * Downloads all scrobbled tracks from the user's last.fm history
	 * @return
	 * a list of all scrobbled tracks
	 * @throws NowPlayingException
	 * if a song is being a scrobbled
	 */
	public TrackList downloadHistory() throws NowPlayingException {
		TrackList history = new TrackList();
		String url = Statics.BLANK;
		String formatString = String.format(fillInString, Statics.RECENT_TRACKS, Statics.LIMIT + Statics.TRACKS_PER_PAGE + Statics.PAGE_TO_DL);
		
		int currentPage = 0;
		int totalPages  = 1;
		
		while (currentPage <= totalPages) {
			currentPage++;
			url = String.format(formatString, currentPage);
			JSONObject page = download(url);
			String strTotalPages = page.get(Statics.RECENTTRACKS).get(Statics.ATTR).get(Statics.TOTAL_PAGES).toString();
			totalPages = Integer.parseInt(strTotalPages);
			
			ProgressBar.progress(currentPage, totalPages, Statics.HISTORY_DOWNLOAD_MESSAGE);
			
			JSONArray tracks = (JSONArray) page.get(Statics.RECENTTRACKS).get(Statics.TRACK);
			for (JSONParser jo : tracks) {
				JSONObject currentTrack = (JSONObject) jo;
				
				if (currentTrack.get(Statics.ATTR) != null) {
					throw new NowPlayingException();
				}
				
				JSONObject tmp;
				
				String name = currentTrack.get(Statics.NAME).toString();
				String mbid = currentTrack.get(Statics.MBID).toString();
				
				tmp = (JSONObject) currentTrack.get(Statics.DATE);
				String dateEpoch = tmp.get(Statics.UTS).toString();
				String date = tmp.get(Statics.TEXT).toString();
				
				tmp = (JSONObject) currentTrack.get(Statics.ARTIST);
				String artist = tmp.get(Statics.TEXT).toString();
				String artistMBID = tmp.get(Statics.MBID).toString();
				
				tmp = (JSONObject) currentTrack.get(Statics.ALBUM);
				String album = tmp.get(Statics.TEXT).toString();
				String albumMBID = tmp.get(Statics.MBID).toString();
				
				Track t = new Track();
				t.put(Statics.NAME, name);
				t.put(Statics.MBID, mbid);
				t.put(Statics.DATE_EPOCH, dateEpoch);
				t.put(Statics.DATE, date);
				t.put(Statics.ARTIST, artist);
				t.put(Statics.ARTIST_MBID, artistMBID);
				t.put(Statics.ALBUM, album);
				t.put(Statics.ALBUM_MBID, albumMBID);
				t.put(Statics.PAGE, Integer.toString(currentPage));
				t.put(Statics.TOTAL_PAGES, strTotalPages);
				
				
				history.add(t);
			}
		}
		
		int count = 0;
		int page = 0;
		
		for (Track t : history) {
			if (count++ % 50 == 0) {
				page++;
			}
			
			t.put(Statics.PAGE, Integer.toString(page));
		}
				
		return history;
	}
	
	/**
	 * Makes a call to last.fm to see if the artist is correct or if it needs a correction
	 * @param matching
	 * the track we are checking the artist for
	 * @return
	 * true is the artist got corrected or false if not
	 */
	public boolean matchArtist(Track matching) {
		String artist = matching.get(Statics.ARTIST);
		
		String url = String.format(fillInString, "artist.getCorrection", Statics.STR);
		url = String.format(url, "&artist=%s");
		
		int start = url.indexOf("&user");
		int end = url.indexOf("&api_key");
		url = url.substring(0, start) + url.substring(end);
		
		
		try {
			artist = URLEncoder.encode(artist, Statics.ENCODING);
		} catch (UnsupportedEncodingException e) {
			// Shouldn't fail
		}
		url = String.format(url, artist);
		
		boolean corrected = false;
		JSONObject result = (JSONObject) download(url);
		
		try {
			JSONObject track = (JSONObject) ((JSONObject) result).get("corrections");
			track = (JSONObject) ((JSONObject) track).get("correction");
			
			if (track != null) {
				track = (JSONObject) ((JSONObject) track).get(Statics.ARTIST);
				
				JSONString name = (JSONString) ((JSONObject) track).get(Statics.NAME);
				JSONString mbid = (JSONString) ((JSONObject) track).get(Statics.MBID);
				
				if (name != null) {
					matching.put(Statics.ARTIST, name.toString());
				}
				if (mbid != null) {
					matching.put(Statics.ARTIST_MBID, mbid.toString());
				}
				corrected = true;
			}
		} catch (Exception e) {	}
		
		return corrected;
	}
	
	/**
	 * Gets the username
	 * @return
	 * the username stored
	 */
	public String getUser() {
		return username;
	}
	
	/**
	 * Checks to see if a track needs to be corrected
	 * @param matching
	 * the track we are checking for corrections for
	 * @return
	 * true if the track was corrected, false if otherwise
	 */
	public boolean matchTrack(Track matching) {
		String name = matching.get(Statics.NAME);
		String artist = matching.get(Statics.ARTIST);
		
		String url = String.format(fillInString, "track.getCorrection", "%s");
		url = String.format(url, "&artist=%s&track=%s");
		
		int start = url.indexOf("&user");
		int end = url.indexOf("&api_key");
		url = url.substring(0, start) + url.substring(end);
		
		try {
			name = URLEncoder.encode(name, Statics.ENCODING);
			artist = URLEncoder.encode(artist, Statics.ENCODING);
		} catch (UnsupportedEncodingException e) {
			// Shouldn't fail
		}
		url = String.format(url, artist, name);
		
		JSONObject result = download(url);
		JSONObject attr = (JSONObject) result.get("corrections").get("correction").get("@attr");
		JSONObject track = (JSONObject) result.get("corrections").get("correction").get("track");
		String trackcorrected = attr.get("trackcorrected").toString();
		
		if (toBoo(trackcorrected)) {
			matching.put(Statics.NAME, track.get(Statics.NAME).toString());
		}
		
		return toBoo(trackcorrected);
	}
	
	/**
	 * Simply converts 0 or 1 into a boolean value
	 * @param s
	 * the string to convert
	 * @return
	 * true for 1, false for 0
	 */
	private static boolean toBoo(String s) {
		if (s.equals("0")) {
			return false;
		} else {
			return true;
		}
	}
	
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
