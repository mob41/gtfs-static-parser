package com.github.mob41.gtfssp.gtfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GtfsFrequency extends GtfsData {
	
	public int trip_id;
	
	public String start_time;
	
	public String end_time;
	
	public int headway_secs;
	
	public GtfsFrequency() {
		
	}

	/***
	 * Get an array of GTFS-static-formatted frequencies from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @return An array of frequencies
	 * @throws IOException
	 */
	public static GtfsFrequency[] getFrequencies(InputStream in) throws IOException {
		return getFrequencies(in, true);
	}
	
	/***
	 * Get an array of GTFS-static-formatted frequencies from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @return An array of frequencies
	 * @throws IOException
	 */
	public static GtfsFrequency[] getFrequencies(InputStream in, boolean skipHeader) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		
		if (skipHeader) {
			reader.readLine();
		}
		
		List<GtfsFrequency> list = new ArrayList<GtfsFrequency>();
		String[] splits;
		String line;
		GtfsFrequency obj;
		while ((line = reader.readLine()) != null) {
			splits = line.split(",");
			if (splits.length < 3) {
			    throw new IOException("Invalid frequencies.txt structure with column length less than 7: " + splits.length);
			}
			
			obj = new GtfsFrequency();
			
			obj.trip_id = Integer.parseInt(splits[0]);
			obj.start_time = splits[1];
			obj.end_time = splits[2];
			
			list.add(obj);
		}
		
		reader.close();
		
		GtfsFrequency[] out = new GtfsFrequency[list.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = list.get(i);
		}
		
		return out;
	}
}
