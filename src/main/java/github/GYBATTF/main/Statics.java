package github.GYBATTF.main;

/**
 * Class used to store static strings used throughout the program
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public class Statics {
	// Characters to generate the salt from
	public static final String CHARS = "abcdefghijklmnopqrstuvwxyz" + 
										"ABCDEFGHIJKLMNOPQRSTUVQXYZ" + 
										"0123456789";
	
	// Valid data for track files
	public static final String NAME = "name";
	public static final String MBID = "mbid";
	public static final String ARTIST = "artist";
	public static final String ARTIST_MBID = "artistMBID";
	public static final String ALBUM = "album";
	public static final String ALBUM_MBID = "albumMBID";
	public static final String DATE = "date";
	public static final String DATE_EPOCH = "dateEpoch";
	public static final String DURATION = "duration";
	public static final String DIFFERENCE = "difference";
	public static final String PAGE = "page";
	public static final String TOTAL_PAGES = "totalPages";
	public static final String STATUS = "status";
	
	// Encoding that the url formaters use
	public static final String ENCODING = "UTF-8";
	
	// Error message to display if user is currently scrobbling tracks
	public static final String SCROBBLING_ERROR = "\nSomething is currently scrobbling to your account!\r\n" + 
			   									  "Please stop playback while this is running!";
	
	// Last.fm information prompts
	public static final String LASTFM_USERNAME = "Enter your last.fm username: ";
	public static final String LASTFM_API_KEY = "Please enter your last.fm api key.\r\n" +
												"(If you don't have one you can create one at https://www.last.fm/api/account/create)\r\n>> ";
	
	// Subsonic information prompts
	public static final String SUBSONIC_REMINDER = "I recommend creating a new account with limited access,\r\n" +
												   "as anyone with access to this file could potentially make\r\n" +
												   "API calls to your SubSonic server.";
	public static final String SUBSONIC_USERNAME = "Enter your SubSonic username: ";
	public static final String SUBSONIC_PASSWORD = "Enter your SubSonic password: ";
	public static final String SUBSONIC_DOMAIN = "Enter your SubSonic domain.\r\n" + 
												 "(For example http://www.example.com/subsonic/)\r\n>> ";
	public static final String SUBSONIC_DELETE = "Would you like to delete the SubSonic login information on exit?\r\n" +
												 "Enter (y/n): ";
	
	// Directories to store files and the names to use for those files
	public static final String API_DIR = "API/";
	public static final String SUBSONIC = API_DIR + "SUBSONIC.SER";
	public static final String LASTFM = API_DIR + "LAST_FM.SER";
	public static final String SUBSONIC_MATCHES = API_DIR + "SUBMATCHES.SER";
	
	public static final String TMP_DIR = "TMP/";
	public static final String HISTORY = TMP_DIR + "HISTORY.SER";
	public static final String UNMATCHED_BACKUP = TMP_DIR + "UNMATCHED.SER";
	
	public static final String OUTPUT_DIR = "Results/";
	public static final String OUTPUT = OUTPUT_DIR + "RUNS_FOUND.html";
	
	// Tracks per page on the last.fm website
	public static final String TRACKS_PER_PAGE = "50";

	// Strings used when printing track lists
	public static final String HEADER = "\t<tr>\r\n\t\t<th><h3><a href=\"https://www.last.fm/user/%s/library?page=%s\" target=\"_blank\">PAGE %s</a></h3></th>";
	public static final String SUBHEADER = "\t<tr>\r\n\t\t" +
										   "<th>TIME DIFFERENCE</th>\r\n\t\t<th>DURATION" +
										   "</th>\r\n\t\t<th>DATE SCROBBLED</th>\r\n\t\t" +
										   "<th>NAME</th>\r\n\t\t<th>ARTIST</th>\r\n\t\t<th>ALBUM</th>\r\n\t</tr>";
	public static final String TRACKLINE = "<tr>\r\n\t\t<td>%s</td>\r\n\t\t<td>%s</td>\r\n\t\t<td>%s</td>\r\n\t\t<td>%s</td>\r\n\t\t<td>%s</td>\r\n\t\t<td>%s</td>\r\n\t</tr>";
	}
