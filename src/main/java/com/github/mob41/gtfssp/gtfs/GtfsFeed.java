package com.github.mob41.gtfssp.gtfs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.github.mob41.gtfssp.gtfs.row.GtfsFeedInfo;
import com.github.mob41.gtfssp.gtfs.row.GtfsTranslation;
import com.google.gson.Gson;

public class GtfsFeed {
	
	public static final String[] TABLES = new String[] {
		"agency",
		"calendar",
		"calendar_dates",
		"fare_attributes",
		"fare_rules",
		"frequencies",
		"routes",
		"stops",
		"stop_times",
		"trips"
	};
	
	private final GtfsTableSource<?>[] sources;

	private final GtfsFeedInfo feedInfo;
	
	public GtfsFeed(GtfsTableSource<?>[] sources) {
		this(null, sources);
	}
	
	public GtfsFeed(GtfsFeedInfo feedInfo, GtfsTableSource<?>[] sources) {
		this.sources = sources;
		this.feedInfo = feedInfo;
	}
	
	protected void preFetchFeed(Map<String, GtfsData[]> map) throws IOException {
		
	}
	
	protected void postFetchFeed(Map<String, GtfsData[]> map) throws IOException {
		
	}
	
	public Map<String, GtfsData[]> fetchFeed() throws IOException {
		Map<String, GtfsData[]> out = new HashMap<String, GtfsData[]>();
		
		preFetchFeed(out);
		
		int i;
		GtfsTableSource<?> source;
		for (i = 0; i < sources.length; i++) {
			source = sources[i];
			System.out.println("Feed fetching from table source (" + (i + 1) + "/" + sources.length + ") " + source.getTableName());
			out.put(source.getTableName(), (GtfsData[]) sources[i].fetchData());
		}
		
		out.put("translations", this.getTranslations());
		if (feedInfo != null) {
			out.put("feed_info", new GtfsData[] { feedInfo });
		}
		
		postFetchFeed(out);
		
		return out;
	}

	public GtfsTableSource<?>[] getSources() {
		return sources;
	}
	
	public GtfsTranslation[] getTranslations() {
		List<GtfsTranslation> list = new ArrayList<GtfsTranslation>();
		GtfsTranslation[] t;
		for (int i = 0; i < sources.length; i++) {
			t = sources[i].getTranslations();
			for (int j = 0; j < t.length; j++) {
				list.add(t[j]);
			}
		}
		GtfsTranslation[] out = new GtfsTranslation[list.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = list.get(i);
		}
		return out;
	}
	
	public static void makeZip(String fileName, Map<String, GtfsData[]> feed) throws IOException {
		if (!fileName.endsWith(".zip")) {
			fileName += ".zip";
		}
		
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(fileName));
		ZipEntry zipEntry;
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(zipOut, StandardCharsets.UTF_8), true);
        
        String key;
        Object val;
        Iterator<String> headersIt; 
        String header;
        List<String> headers;
        GtfsData[] data;
        GtfsData datum;
        Map<String, Object> map;
        Iterator<String> it = feed.keySet().iterator();
        while (it.hasNext()) {
        	key = it.next();
        	
        	data = feed.get(key);
        	if (data.length == 0) {
        		continue;
        	}
        	
        	zipEntry = new ZipEntry(key + ".txt");
        	zipOut.putNextEntry(zipEntry);
        	
        	headers = new ArrayList<String>();
        	headersIt = data[0].getMap().keySet().iterator();
        	while (headersIt.hasNext()) {
        		header = headersIt.next();
        		headers.add(header);
        		writer.print(header);
        		if (headersIt.hasNext()) {
        			writer.print(",");
        		}
        	}
        	writer.println();
        	
        	for (int i = 0; i < data.length; i++) {
        		datum = data[i];
        		map = datum.getMap();
        		for (int j = 0; j < headers.size(); j++) {
        			val = map.get(headers.get(j));
        			if (val != null && !val.equals(-1)){
        				writer.print(val);
        			}
        			
            		if (j != headers.size() - 1) {
            			writer.print(",");
            		}
        		}
        		writer.println();
        	}
        }
        writer.close();
        zipOut.close();
	}

}
