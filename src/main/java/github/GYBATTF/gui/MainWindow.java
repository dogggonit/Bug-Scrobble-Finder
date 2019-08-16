package github.GYBATTF.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import github.GYBATTF.apiCaller.LastFM;
import github.GYBATTF.apiCaller.Subsonic;
import github.GYBATTF.tracks.TrackList;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = -8581459749153609355L;
	
	static final File PREF_FILE = new File("PREFERENCES.SER");
	private static ArrayList<Object> prefCache;
	
	private static JProgressBar progress;
	private static JLabel progressStatus;
	private static JFrame frame;
	
	private static JPanel buttons;
	private static JButton startBtn;
	
	private static TrackList runs;
	static TrackList ssMatches;
	
	public MainWindow() {}

	public static void main(String[] args) throws Exception {
		mainWindow();
	}
	
	private static void mainWindow() throws Exception {
		prefCache = getPrefs();
		
		frame = new JFrame("Bug-Scrobble-Finder");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		
		JMenuItem preferences = new JMenuItem("Settings");
		Preferences pref = new Preferences(prefCache);
		preferences.addActionListener(pref);
		menu.add(preferences);
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new Exit());
		menu.add(exit);
		
		JPanel area = new JPanel();
		JPanel main = new JPanel();
		buttons = new JPanel();
		area.setLayout(new GridBagLayout());
		buttons.setLayout(new GridBagLayout());
		main.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		progress = new JProgressBar(0, 100);
		Dimension dim = progress.getPreferredSize();
		dim.width = 300;
		progress.setPreferredSize(dim);
		progress.setIndeterminate(false);
		
		progressStatus = new JLabel("Halted");
		
		startBtn = new JButton("Start");
		startBtn.addActionListener(new Start());
		
		c.gridx = 0;
		c.gridy = 0;
		main.add(progress, c);
		c.gridy = 1;
		main.add(progressStatus, c);
		c.gridy = 2;
		buttons.add(startBtn, c);
		
		c.gridx = 0;
		c.gridy = 0;
		area.add(main, c);
		c.gridy = 1;
		area.add(buttons, c);
		frame.add(area);
		
		frame.getContentPane().add(BorderLayout.NORTH, menuBar);
		frame.pack();
		frame.repaint();
		frame.setVisible(true);
	}
	
	@SuppressWarnings({ "unchecked", "serial" })
	static ArrayList<Object> getPrefs() throws Exception {
		if (PREF_FILE.exists()) {
			FileInputStream fis = new FileInputStream(PREF_FILE);
			ObjectInputStream ois = new ObjectInputStream(fis);
			ArrayList<Object> read = (ArrayList<Object>) ois.readObject();
			ois.close();
			fis.close();
			
			return read;
		}
		
		return new ArrayList<Object>() {{
			add(new LastFM());
			add(new Subsonic());
			add(new TrackList());
			add(new TrackList());
		}};
	}
	

	protected static void writePrefs() throws Exception {
		FileOutputStream fos = new FileOutputStream(PREF_FILE);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		oos.writeObject(prefCache);
		oos.close();
		fos.close();
	}

	private static class Exit implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
		}
	}

	private static class Start implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			LastFM lfm = (LastFM) prefCache.get(0);
			Subsonic ss = (Subsonic) prefCache.get(1);
			TrackList hist = (TrackList) prefCache.get(2);
			ssMatches = (TrackList) prefCache.get(3);

			progressStatus.setText("Checking settings...");
			if (lfm.getApiKey().equals("") || lfm.getUser().equals("")
				|| ss.getUrl().equals("") || ss.getPassword().equals("") || ss.getUsername().equals("")) {
				try {
					SettingsError.settingsError();
				} catch (Exception e) {}
			}
			progressStatus.setText("Settings loaded!");

			buttons.remove(startBtn);
			if (!(hist.size() == 0)) {
				progressStatus.setText("Do you want to reload your Last.FM history?");
				JButton yes = new JButton("Yes");
				yes.addActionListener(new YesNo.Yes());
				JButton no = new JButton("No");
				no.addActionListener(new YesNo.No());
				buttons.add(yes);
				buttons.add(no);
			}
			
			System.out.println("Here?");
		}
	}
	
	
}

class SettingsError implements ActionListener {
	private static JFrame settingsError;
	
	static void settingsError() throws Exception {
		settingsError = new JFrame("Error");
		
		settingsError = new JFrame("Bug-Scrobble-Finder");
		settingsError.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		settingsError.setResizable(false);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		JPanel errorElements = new JPanel();
		errorElements.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel errorMessage = new JLabel("Please complete settings in order for the program to run.");
		JButton ok = new JButton("ok");
		ok.addActionListener(new Preferences(MainWindow.getPrefs()));
		ok.addActionListener(new SettingsError());
		
		c.gridx = 0;
		c.gridy = 0;
		errorElements.add(errorMessage, c);
		c.gridy = 1;
		errorElements.add(ok, c);
		
		settingsError.add(errorElements);
		settingsError.pack();
		settingsError.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		settingsError.dispose();
	}
}

class YesNo {
	static class Yes implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
		}
	}
	
	static class No implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
		}
	}
}
