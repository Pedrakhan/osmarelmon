package util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Responsible for downloading data from a specified sources. Code from
 * http://www.unixboard.de/vb3/showthread.php?41527-Java-Datei-downloaden
 * 
 * @author sebastian
 * 
 */
public final class FileDownloader {

	private static final Log log = LogFactory.getLog(FileDownloader.class);

	private FileDownloader() {
	}

	/**
	 * Downloads the complete relation. Code from
	 * http://www.unixboard.de/vb3/showthread.php?41527-Java-Datei-downloaden.
	 * 
	 * @param url
	 *            The URL to download.
	 * @return The path to the location of the downloaded file or null if an
	 *         error occured.
	 */
	public static synchronized String download(String query, String name) {
		name = name.replaceAll(" ", "");
		String path = util.Constants.HOME_DIR + name + "~";
		final String url = "http://overpass-api.de/api/interpreter?data=";
		try {
			FileOutputStream fos = new FileOutputStream(path);
			log.info("Downloading: " + url + query);
			downloadFile(url + query, fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			path = null;
		}
		return path;
	}

	/**
	 * Downloads the file from a specified URL.
	 * 
	 * @param url_str
	 *            The specified URL.
	 * @param os
	 *            The outputstream
	 * @throws IllegalStateException
	 * @throws MalformedURLException
	 * @throws ProtocolException
	 * @throws IOException
	 */
	private static void downloadFile(String url_str, OutputStream fos)
			throws IllegalStateException, MalformedURLException,
			ProtocolException, IOException {

		URL url = new URL(url_str.replace(" ", "%20"));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();
		int responseCode = conn.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			byte tmp_buffer[] = new byte[4096];
			long counter = 0;
			InputStream is = conn.getInputStream();
			int n;
			while ((n = is.read(tmp_buffer)) > 0) {
				counter = counter + 4096;
				// funktioniert das, bzw stürzt der irgendwann mal ab?
				if (counter > 9999999999L) {
					log.error("To big data download");
					throw new IllegalStateException("File was too big");
				}
				fos.write(tmp_buffer, 0, n);
				fos.flush();
			}
		} else {
			throw new IllegalStateException("HTTP response: " + responseCode);
		}
	}

}
