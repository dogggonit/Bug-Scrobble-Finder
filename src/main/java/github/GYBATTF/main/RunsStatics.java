package github.GYBATTF.main;

public class RunsStatics {
	
	
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
