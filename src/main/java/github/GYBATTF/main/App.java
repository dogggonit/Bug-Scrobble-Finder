package github.GYBATTF.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;

import github.GYBATTF.apiCaller.LastFM;
import github.GYBATTF.apiCaller.Subsonic;
import github.GYBATTF.tracks.Track;
import github.GYBATTF.tracks.TrackList;

/**
 * Contains the main method of this program and methods to help it run
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public final class App {
	/**
	 * Main loop for the program.
	 * @param args
	 * this program does not take any args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);
		
		File lfmFile = new File(Statics.LASTFM);
		File ssFile = new File(Statics.SUBSONIC);
		File hFile = new File(Statics.HISTORY);
		File ssmFile = new File(Statics.SUBSONIC_MATCHES);
		File umFile = new File(Statics.UNMATCHED_BACKUP);

		System.out.println(Statics.SUBSONIC_REMINDER);
		Subsonic subsonic = getSubsonic(ssFile, in);
		deleteSubsonic(ssFile, in);
		LastFM lastFM = getLastFM(lfmFile, in);
		TrackList history = getHistory(hFile, lastFM);
		
		TrackList submatches = getSubsonicMatches(ssmFile);
		int submatchesOriginalSize = submatches.size();
		
		TrackList unmatched = getUnmatched(umFile, lastFM, subsonic, history, submatches);
		history.serialize(hFile);
		
		if (submatchesOriginalSize != submatches.size()) {
			submatches.serialize(ssmFile);
		}
		
		history.findRunsByDuration();
		System.out.println();
		
		unmatched.findRunsByDate();
		System.out.println();
		
		history.addAll(unmatched);
		history.sort();
		
		File output = new File(Statics.OUTPUT);
		
		File dir = new File(Statics.OUTPUT_DIR);
		dir.mkdir();
		printTrackListHTML(history, output, lastFM);
		
		new File(Statics.TMP_DIR).delete();
		hFile.delete();
		umFile.delete();
		
		System.out.println(Statics.FINISHED_MESSAGE);
	}
	
    /**
     * Prompts the user if they want their subsonic information cleared when the program quits.
     * @param ssFile
     * the file to potentially delete
     * @param in
     * System.in scanner to use to prompt the user
     */
	private static void deleteSubsonic(File ssFile, Scanner in) {
		System.out.print(Statics.SUBSONIC_DELETE);
		boolean delete = in.next().toLowerCase().contains("y");
		if (delete) {
			ssFile.deleteOnExit();
		}
	}
	
	/**
	 * Prints the list of tracks into an HTML document for easy reading
	 * @param list
	 * the list of runs found
	 * @param f
	 * the file to save to
	 * @throws Exception
	 */
	private static void printTrackListHTML(TrackList history, File f, LastFM lfm) throws Exception {
		PrintWriter pr = new PrintWriter(f);
		pr.println(Statics.PAGE_TOP);
		
		String pageNumber = "0";
		
		TrackList page = new TrackList();
		
		for (Track t : history) {
			if (!pageNumber.equals(t.get(Statics.PAGE))) {
				if (!pageNumber.equals("0")) {
					printPage(pr, page, pageNumber, lfm);
				}
				
				pageNumber = t.get(Statics.PAGE);
				
				page = new TrackList();
				page.add(t);
			} else {
				pageNumber = t.get(Statics.PAGE);
				page.add(t);
			}
		}
		
		
		printPage(pr, page, pageNumber, lfm);
		pr.println(Statics.FOOTER);
		pr.close();
	}
	
	/**
	 * Method to help print a page of runs
	 * @param pr
	 * the PrintWriter object to use
	 * @param page
	 * the page list to print
	 * @param pageNumber
	 * the page number we are printing
	 */
	private static void printPage(PrintWriter pr, TrackList page, String pageNumber, LastFM lfm) {
		pr.println(String.format(Statics.HEADER, lfm.getUser(), pageNumber, pageNumber));
		pr.println(Statics.SUBHEADER);
		for (Track l : page) {
			String color = "<font color=\"%s\">%%s</font>";
			if (l.get(Statics.STATUS).equals("+")) {
				color = String.format(color, "green");
			} else if (l.get(Statics.STATUS).equals("-")) {
				color = String.format(color, "red");
			} else if (l.get(Statics.STATUS).equals("0")) {
				color = String.format(color, "blue");
			}
			
			String duration = String.format(color, l.get(Statics.DURATION));
			String difference = String.format(color, l.get(Statics.DIFFERENCE));
			String date = String.format(color, l.get(Statics.DATE));
			String name = String.format(color, l.get(Statics.NAME));
			String artist = String.format(color, l.get(Statics.ARTIST));
			String album = String.format(color, l.get(Statics.ALBUM));
			pr.println(String.format(Statics.TRACKLINE, difference, duration, date, name, artist, album));
			pr.println();
		}
	}
	
	/**
	 * Since there's a lot of points in the program where it takes a decent amount of time to get through
	 * we backup everything at certain points so if the program quits before complete for whatever reason
	 * we can load it up and start again from that point. This method saves tracks that are unmatched.
	 * If there's no saved file it goes through the history file and gets durations/unmatched tracks.
	 * @param f
	 * file to save unmatched tracks to
	 * @param lfm
	 * last.fm connection to use
	 * @param ss
	 * subsonic connection to use
	 * @param hist
	 * history file to get durations for
	 * @param sm
	 * list of already cached durations
	 * @return
	 * list of unmatched tracks
	 */
	private static TrackList getUnmatched(File f, LastFM lfm, Subsonic ss, TrackList hist, TrackList sm) {
		TrackList rtn = new TrackList();
		try {
			rtn = TrackList.deserialize(f);
		} catch (Exception e) {
			rtn =  getDurations(lfm, ss, hist, sm);
			try {
				rtn.serialize(f);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return rtn;
	}

	/**
	 * Get the duration for the files in the history list
	 * @param lastFM
	 * last.fm connection to use
	 * @param subsonic
	 * subsonic connection to use
	 * @param history
	 * history to get durations for
	 * @param submatches
	 * list to cache found durations to
	 * @return
	 * list of tracks where a duration could not be found
	 */
	private static TrackList getDurations(LastFM lastFM, Subsonic subsonic, TrackList history, TrackList submatches) {
		TrackList unmatched = new TrackList();
		
		Iterator<Track> historator = history.iterator();
		TrackList unfound = new TrackList();
		int previousLength = 0;
		
		for (int i = 0, total = history.size(); historator.hasNext();) {
			Track searching = historator.next();
			for (int j = 0; j <= previousLength; j++) {
				System.out.print(" ");
			}
			System.out.print("\r");
			ProgressBar.progress(++i, total, "Getting duration of " + searching.get(Statics.NAME) + " : ");
			previousLength = searching.get(Statics.NAME).length() + 63;
			boolean success = false;
			boolean found = false;
			
			Track downloaded = submatches.find(searching);
			if (downloaded != null) {
				searching.put(Statics.DURATION, downloaded.get(Statics.DURATION));
				found = true;
			} else {
				Track giveup = unfound.find(searching);
				if (giveup == null) {
					success = downloadDuration(searching, lastFM, subsonic);
				} else {
					unfound.add(searching);
				}
			}
			
			if (success) {
				submatches.add(searching);
			} else if (!found) {
				searching.put(Statics.STATUS, "0");
				unmatched.add(searching);
				historator.remove();
			}
		}
		System.out.println();
		
		unmatched.sort();
		return unmatched;
	}
	
	/**
	 * Searches your subsonic library for matching tracks and adds the duration if found
	 * @param searching
	 * track we are searching for the duration for
	 * @param lastFM
	 * last.fm connection to use
	 * @param subsonic
	 * subsonic connection to use
	 * @return
	 * true if duration was found
	 */
	private static boolean downloadDuration(Track searching, LastFM lastFM, Subsonic subsonic) {
		Track[] results = new Track[0];
		boolean success = false;
		
		for (int i = 1, previousSize = -1; i <= 26; i += 5) {
			previousSize = results.length;
			results = subsonic.search(searching, i);
			int index = -1;
			
			for (int j = previousSize; j < results.length; j++) {
				if (searching.fuzzyEquals(results[j])) {
					index = j;
				} else {
					Track tmp = results[j];
					boolean artistCorrected = lastFM.matchArtist(tmp);
					boolean trackCorrected = lastFM.matchTrack(tmp);
					
					if ((artistCorrected || trackCorrected) && searching.fuzzyEquals(tmp)) {
						index = j;
					}
				}
			}
			if (index != -1) {
				success = true;
				
				searching.put(Statics.DURATION, results[index].get(Statics.DURATION));
				
				break;
			}
			
		}
		
		return success;
	}
	
	/**
	 * Prompts the user to gather information to create a last.fm object
	 * @param f
	 * file to save the information to
	 * @param in
	 * System.in scanner to use
	 * @return
	 * a last.fm file
	 * @throws Exception
	 */
	private static LastFM getLastFM(File f, Scanner in) throws Exception {
		LastFM lastFM = new LastFM();
		
		try {
			lastFM = (LastFM) lastFM.deserialize(f);
		} catch (Exception e) {
			System.out.print(Statics.LASTFM_API_KEY);
			String apiKey = in.next();
			
			System.out.print(Statics.LASTFM_USERNAME);
			String username = in.next();
			
			lastFM = new LastFM(apiKey, username);
			
			lastFM.serialize(f);
		}
		
		return lastFM;
	}
	
	/**
	 * Prompts the user to gather the information required to create the SubSonic connection
	 * @param f
	 * file to save the subsonic information to
	 * @param in
	 * System.in scanner to use for prompts
	 * @return
	 * a subsonic file
	 * @throws Exception
	 */
	private static Subsonic getSubsonic(File f, Scanner in) throws Exception {
		Subsonic subsonic = new Subsonic();
		
		try {
			subsonic = (Subsonic) subsonic.deserialize(f);
		} catch (Exception e) {
			System.out.print(Statics.SUBSONIC_USERNAME);
			String username = in.next();
			
			System.out.print(Statics.SUBSONIC_DOMAIN);
			String domain = in.next();
			
			subsonic = new Subsonic(username, domain, Statics.SUBSONIC_PASSWORD);
			
			File dir = new File(Statics.API_DIR);
			dir.mkdir();
			
			subsonic.serialize(f);
		}
		
		return subsonic;
	}
	
	/**
	 * Either gets the history from online or from file
	 * @param f
	 * file to either load from or to save to
	 * @param lastfm
	 * your last.fm connection
	 * @return
	 * a list of tracks
	 * @throws Exception
	 */
	private static TrackList getHistory(File f, LastFM lastfm) throws Exception {
		TrackList history = null;
		
		try {
			history = TrackList.deserialize(f);
		} catch (IOException e) {
			
			try {
				history = lastfm.downloadHistory();
				System.out.println();
			} catch (Exception e1) {
				System.out.println(Statics.SCROBBLING_ERROR);
				System.exit(-1);
			}

			history.findRuns();
			
			File dir = new File(Statics.TMP_DIR);
			dir.mkdir();
			
			history.serialize(f);
		}
		
		return history;
	}
	
	/**
	 * Gets the file containing durations for tracks downloaded from subsonic
	 * @param f
	 * file durations are saved to
	 * @return
	 * the list of tracks and their durations
	 */
	private static TrackList getSubsonicMatches(File f) {
		TrackList subMatches;
		
		try {
			subMatches = TrackList.deserialize(f);
		} catch (Exception e) {
			subMatches = new TrackList();
		}
		
		return subMatches;
	}
}
