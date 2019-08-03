package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import github.GYBATTF.apiCaller.LastFM;
import github.GYBATTF.apiCaller.Subsonic;

public class Preferences extends JFrame implements ActionListener {
	private static final long serialVersionUID = -5053101460206138890L;
	
	private JFrame frame;
	
	private JTextField apiKey;
	private JTextField lfmUserName;
	
	private JTextField ssURL;
	private JTextField ssUserName;
	private JPasswordField ssPassword;
	
	private ArrayList<Object> prefs;
	
	public Preferences(ArrayList<Object> prefs) {
		this.prefs = prefs;
	}

	@SuppressWarnings("serial")
	@Override
	public void actionPerformed(ActionEvent ae) {
		frame = new JFrame("Settings");
		
		frame.setSize(300, 300);
		frame.setResizable(false);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}

		JPanel area = new JPanel();
		
		area.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JTabbedPane settings = new JTabbedPane();
		
		JComponent general = general();
		//settings.addTab("General", general);
		
		JComponent lfm = lastFmSettings();
		settings.addTab("Last.FM", lfm);
		
		JComponent subsonic = subsonic();
		settings.addTab("SubSonic", subsonic);

		c.gridx = 0;
		c.gridy = 0;
		area.add(settings, c);
		c.gridy = 1;
		area.add(new JButton("Save") {{
			addActionListener(new SaveFields());
		}}, c);
		
		frame.add(area);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	@SuppressWarnings("serial")
	private JComponent general() {
		JPanel gen = new JPanel();
		gen.setLayout(new GridLayout(4, 2));
		
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridwidth = GridBagConstraints.REMAINDER;
		JLabel todo = new JLabel("To-do");
		gen.add(todo, grid);
		return gen;
	}
	
	private JComponent lastFmSettings() {
		JPanel lfm = new JPanel();
		
		lfm.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel apikeyLabel = new JLabel("Last.FM API key:");
		apiKey = new JTextField(((LastFM) prefs.get(0)).getApiKey(), 20);
		
		JLabel lfmUserNameLabel = new JLabel("Last.FM username:");
		lfmUserName = new JTextField(((LastFM) prefs.get(0)).getUsername(), 20);

		c.gridx = 0;
		c.gridy = 0;
		lfm.add(apikeyLabel, c);
		c.gridx = 1;
		lfm.add(apiKey, c);
		c.gridx = 0;
		c.gridy = 1;
		lfm.add(lfmUserNameLabel, c);
		c.gridx = 1;
		lfm.add(lfmUserName, c);
		
		return lfm;
	}
	
	private JComponent subsonic() {
		JPanel ss = new JPanel();
		
		ss.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel urlLabel = new JLabel("Subsonic URL:");
		ssURL = new JTextField(((Subsonic) prefs.get(1)).getUrl(), 20);
		
		JLabel ssUserNameLabel = new JLabel("Subsonic username:");
		ssUserName = new JTextField(((Subsonic) prefs.get(1)).getUsername(), 20);
		
		JLabel ssPasswordLabel = new JLabel("Subsonic password:");
		ssPassword = new JPasswordField(((Subsonic) prefs.get(1)).getPassword(), 20);

		c.gridx = 0;
		c.gridy = 0;
		ss.add(urlLabel, c);
		c.gridx = 1;
		ss.add(ssURL, c);
		c.gridx = 0;
		c.gridy = 1;
		ss.add(ssUserNameLabel, c);
		c.gridx = 1;
		ss.add(ssUserName, c);
		c.gridx = 0;
		c.gridy = 2;
		ss.add(ssPasswordLabel, c);
		c.gridx = 1;
		ss.add(ssPassword, c);
		
		return ss;
	}
	
	private class SaveFields implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			((LastFM) prefs.get(0)).setApiKey(apiKey.getText());
			((LastFM) prefs.get(0)).setUsername(lfmUserName.getText());
			
			((Subsonic) prefs.get(1)).setUrl(ssURL.getText());
			((Subsonic) prefs.get(1)).setUsername(ssUserName.getText());
			if (!(new String(ssPassword.getPassword()).equals("********"))) {
				((Subsonic) prefs.get(1)).setPassword(new String(ssPassword.getPassword()));
			}
			try {
				MainWindow.writePrefs();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			frame.dispose();
		}
	}
}
