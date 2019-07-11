package github.GYBATTF.apiCaller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import github.GYBATTF.jsonParser.JSONObject;
import github.GYBATTF.jsonParser.JSONParser;
/**
 * Class to download from various APIs used in this project.
 * @author GYBATTF
 * https://github.com/GYBATTF/Bug-Scrobble-Finder
 */
abstract class AbstractDownloader implements Serializable {
	private static final long serialVersionUID = 66973918080873787L;

	/**
	 * Downloads a URL pointing toward a json object and parses it. If the download fails, waits a seconds and then tries again
	 * @param url 
	 * url to download
	 * @return
	 * JSONObject that was downloaded
	 */
	protected JSONObject download(String url) {
		String tmp = "";
		
		try {
			URL apiLink = new URL(url);
			InputStream in = apiLink.openStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			
			String next;
			while ((next = r.readLine()) != null) {
				tmp += next;
			}
		} catch (IOException e) {
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return download(url);

		}
		return JSONParser.parse(tmp);
	}
	
	/**
	 * General method used to serialize subclasses to a file
	 * @param f
	 * file to save to
	 * @throws Exception
	 */
	public void serialize(File f) throws Exception {
		FileOutputStream fos = new FileOutputStream(f);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		oos.writeObject(this);
		oos.close();
		fos.close();
	}
	
	/**
	 * General method to read a downloader file from disk
	 * @param f
	 * the file to read from
	 * @return
	 * the downloader object
	 * @throws Exception
	 */
	public AbstractDownloader deserialize(File f) throws Exception {
    	FileInputStream fis = new FileInputStream(f);
		ObjectInputStream ois = new ObjectInputStream(fis);
		AbstractDownloader read = (AbstractDownloader) ois.readObject();
		ois.close();
		fis.close();
		
		return read;
	}
}
