package com.github.mob41.gtfssp.gtfs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class GtfsLocalizedSource {
	
	private final String language;
	
	private final InputStream in;
	
	public GtfsLocalizedSource(String language, String url) throws IOException {
		this(language, initInputStream(url));
	}

	public GtfsLocalizedSource(String language, InputStream in) {
		this.language = language;
		this.in = in;
	}
	
	private static InputStream initInputStream(String str) throws IOException{
		URL url = new URL(str);
		URLConnection conn = url.openConnection();
		
		return conn.getInputStream();
	}

	public String getLanguage() {
		return language;
	}

	public InputStream getInputStream() {
		return in;
	}

}
