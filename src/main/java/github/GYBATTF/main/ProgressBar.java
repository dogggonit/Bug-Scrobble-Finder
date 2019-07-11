package github.GYBATTF.main;

/**
 * A simple method for creating a progressbar
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
public class ProgressBar {
	/**
	 * Prints a progress bar
	 * @param completed
	 * the current progress
	 * @param total
	 * the total progress
	 * @param message
	 * message to print with the progressbar
	 */
	public static void progress(int completed, int total, String message) {
		double progress = completed / (double) total;
		String done = "--------------------";
		
		if (progress > .05 && progress < .1) {
			done = "=-------------------";
		} else if (progress >= .1 && progress < .15) {
			done = "==------------------";
		} else if (progress >= .15 && progress < .2) {
			done = "===-----------------";
		} else if (progress >= .2 && progress < .25) {
			done = "====----------------";
		} else if (progress >= .25 && progress < .3) {
			done = "=====---------------";
		} else if (progress >= .3 && progress < .35) {
			done = "======--------------";
		} else if (progress >= .35 && progress < .4) {
			done = "=======-------------";
		} else if (progress >= .4 && progress < .45) {
			done = "========------------";
		} else if (progress >= .45 && progress < .5) {
			done = "=========-----------";
		} else if (progress >= .5 && progress < .55) {
			done = "==========----------";
		} else if (progress >= .55 && progress < .6) {
			done = "===========---------";
		} else if (progress >= .6 && progress < .65) {
			done = "============--------";
		} else if (progress >= .65 && progress < .7) {
			done = "=============-------";
		} else if (progress >= .7 && progress < .75) {
			done = "==============------";
		} else if (progress >= .75 && progress < .8) {
			done = "===============-----";
		} else if (progress >= .8 && progress < .85) {
			done = "================----";
		} else if (progress >= .85 && progress < .9) {
			done = "=================---";
		} else if (progress >= .9 && progress < .95) {
			done = "==================--";
		} else if (progress >= .95 && progress < 1) {
			done = "===================-";
		} else if (progress >= 1) {
			done = "====================";
		}

		String numSize = Integer.toString(Integer.toString(total).length());
		String msg = String.format("%%s %%%sd out of %%%sd    [%%s] %%.2f%%%%\r", numSize, numSize);
		System.out.printf(msg, message, 
						  completed, total, done, (completed/(double) total) * 100.0);
	}
}
