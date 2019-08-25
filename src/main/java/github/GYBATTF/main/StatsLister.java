package github.GYBATTF.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import github.GYBATTF.apiCaller.LastFM;
import github.GYBATTF.tracks.Track;
import github.GYBATTF.tracks.TrackList;

public class StatsLister {
	/**
	 * Main loop for the program.
	 * @param args
	 * this program does not take any args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);
				
		File lfmFile = new File(Statics.LASTFM);
		File hFile = new File(Statics.HISTORY);

		LastFM lastFM = getLastFM(lfmFile, in);
		TrackList history = getHistory(hFile, lastFM);
		
		history.serialize(hFile);
		
		File output = new File(StatsStatics.OUTPUT);
		
		File dir = new File(Statics.OUTPUT_DIR);
		dir.mkdir();
		
		new File(Statics.TMP_DIR).delete();
		//hFile.delete();
		
		HashMap<Track, Integer> trackPlays = new HashMap<>();
		for (Track t : history) {
			if (trackPlays.get(t) == null) {
				trackPlays.put(t, 1);
			} else {
				trackPlays.put(t, trackPlays.get(t) + 1);
			}
		}
		
		HashMap<String, Integer> artistPlays = new HashMap<>();
		for (Track t : history) {
			if (artistPlays.get(t.get(Statics.ARTIST)) == null) {
				artistPlays.put(t.get(Statics.ARTIST), 1);
			} else {
				artistPlays.put(t.get(Statics.ARTIST), artistPlays.get(t.get(Statics.ARTIST)) + 1);
			}
		}

		Track[] topSongs = new Track[trackPlays.size()];
		for (Track t : trackPlays.keySet()) {
			topSongs[trackPlays.get(t)] = t;
		}
		
		for (int i = trackPlays.size() - 1, ct = 0; i >= 0; i--) {
			if (ct <= 10) {
				if (topSongs[i] != null) {
					ct++;
					//System.out.print(topSongs[i]);
					//System.out.print(" : ");
					//System.out.println(trackPlays.get(topSongs[i]));
				} 
			} else {
				break;
			}
		}
		
		//System.out.println();
		
		String[] topArtists = new String[artistPlays.size()];
		for (String s : artistPlays.keySet()) {
			topArtists[artistPlays.get(s)] = s;
		}
		
		double avgMade = 0.0;
		for (int i = topArtists.length - 1, ct = 0; i >= 0; i--) {
			if (ct <= 100 && topArtists[i] != null) {
				ct++;
				System.out.print(topArtists[i]);
				System.out.print(" : ");
				System.out.print(artistPlays.get(topArtists[i]));
				System.out.print(" : ");
				System.out.printf("$%.02f\r\n", ((double) artistPlays.get(topArtists[i])) * 0.006);
			}
			
			if (topArtists[i] != null) {
				avgMade += ((double) artistPlays.get(topArtists[i])) * 0.006;
			}
		}
		System.out.println();
		System.out.printf("In total everyone I've ever listened to has made on average $%.02f\r\n", avgMade / (double) artistPlays.size());
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
				history = lastfm.downloadHistoryStats();
				System.out.println();
			} catch (Exception e1) {
				System.out.println(Statics.SCROBBLING_ERROR);
				System.exit(-1);
			}
			
			File dir = new File(Statics.TMP_DIR);
			dir.mkdir();
			
			history.serialize(f);
		}
		
		return history;
	}
}
