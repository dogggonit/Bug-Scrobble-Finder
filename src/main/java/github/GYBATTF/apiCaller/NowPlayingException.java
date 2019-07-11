package github.GYBATTF.apiCaller;

/**
 * An exception to throw if the user is currently listening to music
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public class NowPlayingException extends Exception {
	private static final long serialVersionUID = 1567905524766564448L;

	public NowPlayingException() {
		super();
	}
	
	public NowPlayingException(String s) {
		super(s);
	}
}
