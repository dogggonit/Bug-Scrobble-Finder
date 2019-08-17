package github.GYBATTF.tracks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import github.GYBATTF.main.ProgressBar;
import github.GYBATTF.main.Statics;

/**
 * Object for storing an ordered list of tracks and methods to do work on it
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public class TrackList implements Iterable<Track>, Serializable {
	private static final long serialVersionUID = 1296296404492313423L;
	
	private Node head;
	private Node tail;
	private int size;
	
	/**
	 * Initializes an empty TrackList
	 */
	public TrackList() {
		head = new Node(null);
		tail = new Node(null);
		size = 0;
		
		head.next = tail;
		head.previous = tail;
		tail.previous = head;
		tail.next = head;
	}
	
	/**
	 * Creates a new TrackList from elements in an ArrayList
	 * @param a
	 * ArrayList to add elements from
	 */
	public TrackList(ArrayList<Track> a) {
		head = new Node(null);
		tail = new Node(null);
		size = 0;
		
		head.next = tail;
		head.previous = tail;
		tail.previous = head;
		tail.next = head;
		
		addAll(a);
	}
	
	/**
	 * Adds a tracks to the list
	 * @param t
	 * track to add
	 */
	public void add(Track t) {
		size++;
		Node tmp = new Node(t);
		
		tmp.previous = tail.previous;
		tmp.next = tail;
		
		tail.previous.next = tmp;
		tail.previous = tmp;
	}
	
	/**
	 * Gets a track at the specified index
	 * @param index
	 * index to get the track from
	 * @return
	 * track at the index
	 */
	public Track get(int index) {
		if (index >= size) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		Node current = head.next;
		
		for (int i = 0; i < index; i++) {
			current = current.next;
		}
		
		return current.data;
	}
	
	/**
	 * Attempts to find a track in the list. If it is found it moves it to the front of the list and then returns the track.
	 * Since we are using this to find a matching track usually in a run of multiple moving it to the front prevents us 
	 * from having to search the entire list for the same track multiple times in a row.
	 * @param t
	 * track to search for
	 * @return
	 * the track if found, null if not found
	 */
	public Track find(Track t) {
		Node current = head.next;
		
		while (current.data != null) {
			if (t.equals(current.data)) {
				if (size > 1) {
					current.previous.next = current.next;
					current.next.previous = current.previous;
					
					current.next = head.next;
					head.next.previous = current;
					current.previous = head;
					head.next = current;
				}
				
				return current.data;
			}
			
			current = current.next;
		}
		
		return null;
	}
	
	/**
	 * Returns how many tracks are in the list
	 * @return
	 * size of the list
	 */
	public int size() {
		return size;
	}

	/**
	 * Serializes this TrackList to the specified file
	 * @param f
	 * file to save to
	 * @throws Exception
	 */
	public void serialize(File f) throws Exception {
		FileOutputStream fos = new FileOutputStream(f);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		
		
		oos.writeObject(toArrayList());
		oos.close();
		fos.close();
	}
	
	private ArrayList<Track> toArrayList() {
		ArrayList<Track> write = new ArrayList<>();
		if (size != 0) {
			for (Track t : this) {
				write.add(t);
			} 
		}
		return write;
	}
	
	public TrackList shallowCopy() {
		TrackList copy = new TrackList();
		
		for (Track t : this) {
			copy.add(t);
		}
		
		return copy;
	}

	/**
	 * Loads a TrackList from disk
	 * @param f
	 * the file to load from
	 * @return
	 * the opened tracklist
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static TrackList deserialize(File f) throws Exception {
		ArrayList<Track> read = new ArrayList<>();
    	FileInputStream fis = new FileInputStream(f);
		ObjectInputStream ois = new ObjectInputStream(fis);
		read = (ArrayList<Track>) ois.readObject();
		ois.close();
		fis.close();
		
		return new TrackList(read);
	}
	
	/**
	 * Finds runs of tracks in this list and removes tracks that are not in a run
	 */
	public void findRuns() {
		int i = 0;
		int initialSize = size;
		for (Iterator<Node> it = head.iterator(); it.hasNext();) {
			Node nd = it.next();
			
			if (nd.data == null) {
				continue;
			}
			
			boolean previous = nd.data.equals(nd.previous.data);
			boolean next = nd.data.equals(nd.next.data);
			if (!(previous || next)) {
				it.remove();
			}
			
			ProgressBar.progress(++i, initialSize, "Finding runs in Last.FM history");
		}
		System.out.println();
	}
	
	/**
	 * Finds runs by determining if the song was scrobbled multiple times in its own playtime. Adds it to specified list if it is bad and removes it from this list.
	 * @param bugged
	 * the list to add bad scrobbles to
	 */
	public void findRunsByDuration() {
		int i = 0;
		for (Iterator<Node> it = head.iterator(); it.hasNext();) {
			Node nd = it.next();
			
			if (nd.data == null) {
				continue;
			}
			
			boolean previous = nd.data.equals(nd.previous.data);
			boolean next = nd.data.equals(nd.next.data);

			long length = Long.parseLong(nd.data.get(Statics.DURATION));
			long currentEpoch = Long.parseLong(nd.data.get(Statics.DATE_EPOCH));
			
			if (previous) {
				long previousEpoch = Long.parseLong(nd.previous.data.get(Statics.DATE_EPOCH));
				
				long diff = previousEpoch - currentEpoch;
				nd.data.put("difference", Long.toString(diff));
				if (diff < length || nd.data.get(Statics.DATE).equals(nd.previous.data.get(Statics.DATE))) {
					nd.data.put(Statics.STATUS, "-");
				} else {
					nd.data.put(Statics.STATUS, "+");
				}
			} else if (next) {
				long nextEpoch = Long.parseLong(nd.next.data.get(Statics.DATE_EPOCH));
				
				long diff = currentEpoch - nextEpoch;
				nd.data.put("difference", Long.toString(diff));
				if (diff < length || nd.data.get(Statics.DATE).equals(nd.next.data.get(Statics.DATE))) {
					nd.data.put(Statics.STATUS, "+");
				} else {
					nd.data.put(Statics.STATUS, "+");
				}
			}
			
			ProgressBar.progress(++i, size, "Finding runs by duration of song");
		}
	}
	
	/**
	 * Determines if a scrobble is bad by if it was scrobbled at the same time as a track next to it
	 * @param bugged
	 * list to add bad scrobbles to
	 */
	public void findRunsByDate() {
		int i = 0;
		for (Iterator<Node> it = head.iterator(); it.hasNext();) {
			Node nd = it.next();
			nd.data.put(Statics.DURATION, "0");
			
			if (nd.data == null) {
				continue;
			}
			
			boolean previous = nd.data.equals(nd.previous.data);
			boolean next = nd.data.equals(nd.next.data);

			String currentDate = nd.data.get(Statics.DATE);
			long currentEpoch = Long.parseLong(nd.data.get(Statics.DATE_EPOCH));
			if (previous) {
				long previousEpoch = Long.parseLong(nd.previous.data.get(Statics.DATE_EPOCH));
				long diff = previousEpoch - currentEpoch;
				nd.data.put("difference", Long.toString(diff));
				
				String previousDate = nd.previous.data.get(Statics.DATE);
				if (currentDate.equals(previousDate)) {
					nd.data.put(Statics.STATUS, "-");
				}
			} else if (next) {
				long nextEpoch = Long.parseLong(nd.next.data.get(Statics.DATE_EPOCH));
				long diff = currentEpoch - nextEpoch;
				nd.data.put("difference", Long.toString(diff));
				
				String nextDate = nd.next.data.get(Statics.DATE);
				
				if (currentDate.equals(nextDate)) {
					nd.data.put(Statics.STATUS, "+");
				}
			}
			
			ProgressBar.progress(++i, size, "Finding runs by scrobble time");
		}
	}

	/**
	 * Return an iterator for this list
	 * @return
	 * an iterator
	 */
	@Override
	public Iterator<Track> iterator() {
		return new LinkedIterator();
	}
	
	/**
	 * Resets this list
	 */
	public void clear() {
		size = 0;
		
		head.next = tail;
		head.previous = tail;
		tail.previous = head;
		tail.next = head;
	}
	
	/**
	 * Adds all elements from an ArrayList
	 * @param a
	 * ArrayList to add from
	 */
	public void addAll(ArrayList<Track> a) {
		for (Track t : a) {
			add(t);
		}
	}
	
	/**
	 * Adds all elements from a TrackList
	 * @param tl
	 * tracklist to add from
	 */
	public void addAll(TrackList tl) {
		for (Track t : tl) {
			add(t);
		}
	}
	
	/**
	 * Sorts all the elements in this list by the epoch play time
	 */
	public void sort() {
		ArrayList<Track> a = new ArrayList<>();
		
		for (Track t : this) {
			a.add(t);
		}
		
		a.sort(new TrackComparator());
		clear();
		addAll(a);
	}
	
	/**
	 * Node used to store information in this list
	 */
	private class Node implements Iterable<Node> {
		public Node previous;
		public Node next;
		
		public Track data;
		
		public Node(Track t) {
			previous = null;
			next = null;
			data = t;
		}

		@Override
		public Iterator<Node> iterator() {
			return new NodeIterator();
		}
		
		private class NodeIterator implements Iterator<Node> {
			private Node cursor = head.next;
			private Node pending = null;
			
			@Override
			public boolean hasNext() {
				return cursor != tail;
			}

			@Override
			public Node next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				
				pending = cursor;
				cursor = cursor.next;
				return pending;
			}
			
			@Override
			public void remove() {
				if (pending == null) {
					throw new IllegalStateException();
				}
				
				pending.previous.next = pending.next;
				pending.next.previous = pending.previous;
				size--;
				pending = null;
			}
		}
	}
	
	/**
	 * Iterator for the main class that iterates over Tracks
	 */
	private class LinkedIterator implements Iterator<Track> {
		private Node cursor = head.next;
		private Node pending = null;
		
		@Override
		public boolean hasNext() {
			return cursor.data != null;
		}

		@Override
		public Track next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			
			pending = cursor;
			cursor = cursor.next;
			return pending.data;
		}
		
		@Override
		public void remove() {
			if (pending == null) {
				throw new IllegalStateException();
			}
			
			pending.previous.next = pending.next;
			pending.next.previous = pending.previous;
			size--;
			pending = null;
		}
	}
	
	/**
	 * Compares tracks based off of play time
	 */
	private class TrackComparator implements Comparator<Track> {

		@Override
		public int compare(Track arg0, Track arg1) {
			String es0 = arg0.get(Statics.DATE_EPOCH);
			String es1 = arg1.get(Statics.DATE_EPOCH);
			
			long e0 = Long.parseLong(es0);
			long e1 = Long.parseLong(es1);
			
			int rtn = (int) (e1 - e0);
			
			return rtn;
		}

	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(toArrayList());
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		ArrayList<Track> read = new ArrayList<>();
		read = (ArrayList<Track>) in.readObject();
		addAll(read);
	}
}
