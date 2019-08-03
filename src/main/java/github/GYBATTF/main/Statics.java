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
	
	// String formatting
	public static final String STR = "%s";
	
	// Valid data for track files
	public static final String BLANK = "";
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
	// Last.fm API caller strings
	public static final String LAST_FM_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=%s&user=%s&api_key=%s%s&format=json";
	public static final String RECENT_TRACKS = "user.getrecenttracks";
	public static final String LIMIT = "&limit=";
	public static final String PAGE_TO_DL = "&page=%d";
	public static final String ATTR = "@attr";
	public static final String HISTORY_DOWNLOAD_MESSAGE = "Downloading page";
	public static final String RECENTTRACKS = "recenttracks";
	public static final String TRACK = "track";
	public static final String UTS = "uts";
	public static final String TEXT = "#text";
	
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
	public static final String TRACKS_PER_PAGE = "200";
	
	// Main strings
	public static final String FINISHED_MESSAGE = String.format("Done!\r\nYour results are in the %s directory.", Statics.OUTPUT_DIR);

	// Strings used when printing track lists
	public static final String PAGE_TOP = "<html>\r\n<head>\r\n<style>\r\ntable, th, td {\r\n\tborder: 1px solid black;\r\n}\r\n</style>\r\n</head>\r\n<body>\r\n" +
										  "<h1>List of Runs in your Last.FM History</h1>\r\n" +
										  "<p>This is a list of all the runs in your last.fm history.</p>\r\n" +
										  "<p><font color=\"red\">Red</font> means that the track was probably incorrecly scrobbled.</p>\r\n" +
										  "<p><font color=\"green\">Green</font> means the track was probaby correctly scrobbled.</p>\r\n" +
										  "<p><font color=\"blue\">Blue</font> means that the the program couldn't determine if the scrobble was correct, and that you need to manually check it.</p>\r\n" +
										  "<p>I recommend checking manually before deleting anything from your last.fm history.</p>\r\n" +
										  "<table style=\"width:100%%\">";
	public static final String HEADER = "\t<tr>\r\n\t\t<th><h3><a href=\"https://www.last.fm/user/%s/library?page=%s\" target=\"_blank\">PAGE %s</a></h3></th>";
	public static final String SUBHEADER = "\t<tr>\r\n\t\t" +
										   "<th>TIME DIFFERENCE</th>\r\n\t\t<th>DURATION" +
										   "</th>\r\n\t\t<th>DATE SCROBBLED</th>\r\n\t\t" +
										   "<th>NAME</th>\r\n\t\t<th>ARTIST</th>\r\n\t\t<th>ALBUM</th>\r\n\t</tr>";
	public static final String TRACKLINE = "<tr>\r\n\t\t<td>%s</td>\r\n\t\t<td>%s</td>\r\n\t\t<td>%s</td>\r\n\t\t<td>%s</td>\r\n\t\t<td>%s</td>\r\n\t\t<td>%s</td>\r\n\t</tr>";
	public static final String FOOTER = "\r\n\t</tr>\r\n</table>\r\n</body>\\r\\n</html>";
	}
