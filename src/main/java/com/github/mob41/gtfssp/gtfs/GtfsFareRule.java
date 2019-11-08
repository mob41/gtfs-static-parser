package com.github.mob41.gtfssp.gtfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GtfsFareRule extends GtfsData {
	
	public int fare_id;
	
	public int route_id;
	
	public int origin_id;
	
	public int destination_id;
	
	public GtfsFareRule() {
		
	}

	/***
	 * Get an array of GTFS-static-formatted fare rules from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @return An array of fare rules
	 * @throws IOException
	 */
	public static GtfsFareRule[] getFareRules(InputStream in) throws IOException {
		return getFareRules(in, true);
	}
	
	/***
	 * Get an array of GTFS-static-formatted fare rules from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @return An array of fare rules
	 * @throws IOException
	 */
	public static GtfsFareRule[] getFareRules(InputStream in, boolean skipHeader) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		
		if (skipHeader) {
			reader.readLine();
		}
		
		List<GtfsFareRule> list = new ArrayList<GtfsFareRule>();
		String[] splits;
		String line;
		GtfsFareRule obj;
		while ((line = reader.readLine()) != null) {
			splits = line.split(",");
			if (splits.length < 4) {
			    throw new IOException("Invalid fare_rules.txt structure with column length less than 7: " + splits.length);
			}
			
			obj = new GtfsFareRule();
			
			obj.fare_id = Integer.parseInt(splits[0]);
			obj.route_id = Integer.parseInt(splits[1]);
			obj.origin_id = Integer.parseInt(splits[2]);
			obj.destination_id = Integer.parseInt(splits[3]);
						
			list.add(obj);
		}
		
		reader.close();
		
		GtfsFareRule[] out = new GtfsFareRule[list.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = list.get(i);
		}
		
		return out;
	}
}
