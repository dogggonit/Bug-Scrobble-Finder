package github.GYBATTF.tracks;

import java.io.Serializable;
import java.util.HashMap;

import github.GYBATTF.main.Statics;

/**
 * Object to store information for a track and to determine if it is equal to another track
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public class Track implements Serializable {
	private static final long serialVersionUID = 1285291007479038158L;
	private HashMap<String, String> trackValues;
	
	/**
	 * Initializes all the values in the track
	 */
	public Track() {
		trackValues = new HashMap<>();
		
		String blank = "";
		trackValues.put(Statics.NAME, blank);
		trackValues.put(Statics.MBID, blank);
		trackValues.put(Statics.ARTIST, blank);
		trackValues.put(Statics.ARTIST_MBID, blank);
		trackValues.put(Statics.ALBUM, blank);
		trackValues.put(Statics.ALBUM_MBID, blank);
		trackValues.put(Statics.DATE, blank);
		trackValues.put(Statics.DATE_EPOCH, blank);
		trackValues.put(Statics.DURATION, blank);
		trackValues.put(Statics.DIFFERENCE, blank);
		trackValues.put(Statics.STATUS, "0");
	}
	
	/**
	 * Adds or corrects a value in the track
	 * @param key
	 * what to put the value under
	 * @param value
	 * value to put in the track
	 */
	public void put(String key, String value) {
		trackValues.put(key, value);
	}
	
	/**
	 * Retrieves a value from the track
	 * @param key
	 * key to use, valid keys are initialized, everything else will return null
	 * @return
	 * The track information
	 */
	public String get(String key) {
		return trackValues.get(key);
	}
	
	/**
	 * Converts the track to string
	 * @return
	 * string version of this track
	 */
	@Override
	public String toString() {
		return trackValues.get(Statics.NAME) + " : " +
			   trackValues.get(Statics.ARTIST) + " : " +
			   trackValues.get(Statics.ALBUM) + " : " +
			   trackValues.get("date");
	}
	
	/**
	 * Compares this object to another
	 * @param o
	 * object to compare against
	 * @return
	 * true if equal
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != this.getClass()) {
			return false;
		}
		
		boolean name = false;
		boolean artist = false;
		boolean album = false;
		Track t = (Track) o;
		

		if (!t.get(Statics.MBID).equals("") && t.get(Statics.MBID).toLowerCase().equals(this.get(Statics.MBID).toLowerCase())) {
			return true;
		} else if (t.get(Statics.NAME).toLowerCase().equals(this.get(Statics.NAME).toLowerCase())) {
			name = true;
		}
		
		if (!t.get(Statics.ARTIST_MBID).equals("") && t.get(Statics.ARTIST_MBID).toLowerCase().equals(this.get(Statics.ARTIST_MBID).toLowerCase())) {
			artist = true;
		} else if (t.get(Statics.ARTIST).toLowerCase().equals(this.get(Statics.ARTIST).toLowerCase())) {
			artist = true;
		}
		
		if (!t.get(Statics.ALBUM_MBID).equals("") && t.get(Statics.ALBUM_MBID).toLowerCase().equals(this.get(Statics.ALBUM_MBID).toLowerCase())) {
			album = true;
		} else if (t.get(Statics.ALBUM).toLowerCase().equals(this.get(Statics.ALBUM).toLowerCase())) {
			album = true;
		}
		
		return name && artist && album;
	}
	
	/**
	 * Since the information of a track from subsonic may not necessarily match subsonic's this allows equals to be more loosely based
	 * @param t
	 * track to compare against
	 * @return
	 * true if the tracks are most likely equal
	 */
	public boolean fuzzyEquals(Track t) {
		boolean name = false;
		boolean artist = false;
		
		String thisArtist = strip(this.get(Statics.ARTIST));
		String thisName = strip(this.get(Statics.NAME));
		String otherArtist = strip(t.get(Statics.ARTIST));
		String otherName = strip(t.get(Statics.NAME));
		
		
		

		if (otherName.contains(thisName) || thisName.contains(otherName)) {
			name = true;
		}
		
		if (otherArtist.contains(thisArtist) || thisArtist.contains(otherArtist)) {
			artist = true;
		}
		
		return name && artist;
	}
	
	/**
	 * Removes parts from a string that may cause it to be inequal to a track that actually matches
	 * @param s
	 * string to strip from
	 * @return
	 * stripped string
	 */
	private String strip(String s) {
		s = s.toLowerCase();
		String rtn = "";
		String chars = Statics.CHARS + " ";
		
		for (int i = 0; i < s.length(); i++) {
			String c = Character.toString(s.charAt(i));
			if (chars.contains(c)) {
				rtn += c;
			}
		}
		
		int theIndex = rtn.indexOf("the ");
		while (theIndex != -1) {
			rtn = rtn.substring(0, theIndex) + rtn.substring(theIndex + 4);
			
			theIndex = rtn.indexOf("the ");
		}
		
		String[] remove = {"feat", "ft", "album"};
		for (String term : remove) {
			int index = rtn.indexOf(term);
			if (index != -1) {
				rtn = rtn.substring(0, index);
			}
		}
		
		return rtn;
	}
	
	@Override
	public int hashCode() {
		return (get(Statics.ARTIST).hashCode() + get(Statics.NAME).hashCode() + get(Statics.ALBUM).hashCode()) / 3;
	}
}
