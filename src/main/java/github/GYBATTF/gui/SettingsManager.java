package github.GYBATTF.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class SettingsManager {
	private static File SETTINGS_FILE = MainWindow.PREF_FILE;
	
	@SuppressWarnings({ "unused", "serial", "unchecked" })
	private static HashMap<String, String> settings = new HashMap<String, String>() {{
		try {
			FileInputStream fis = new FileInputStream(SETTINGS_FILE);
			ObjectInputStream ois = new ObjectInputStream(fis);
			HashMap<String, String> in = (HashMap<String, String>) ois.readObject();
			ois.close();
			fis.close();
			
			for (String s : in.keySet()) {
				put(s, in.get(s));
			}
		} catch (Exception e) {}
	}};
	
	public static void flush() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(SETTINGS_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(settings);
			oos.close();
			fos.close();
		} catch (Exception e) {}
	}
	
	public static String get(String key) {
		return settings.get(key);
	}
	
	public static void set(String key, String value) {
		settings.put(key, value);
	}
}
