package com.github.mob41.gtfssp.gtfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GtfsStop extends GtfsData {
	
	public int stop_id;
	
	public String stop_name;
	
	public double stop_lat;
	
	public double stop_lon;
	
	public int zone_id;
	
	public int location_type;
	
	public String stop_timezone;
	
	public GtfsStop() {
		
	}

	/***
	 * Get an array of GTFS-static-formatted stops from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @return An array of stops
	 * @throws IOException
	 */
	public static GtfsStop[] getStops(InputStream in) throws IOException {
		return getStops(in, true);
	}
	
	/***
	 * Get an array of GTFS-static-formatted stops from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @return An array of stops
	 * @throws IOException
	 */
	public static GtfsStop[] getStops(InputStream in, boolean skipHeader) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		
		if (skipHeader) {
			reader.readLine();
		}
		
		List<GtfsStop> list = new ArrayList<GtfsStop>();
		String[] splits;
		String line;
		GtfsStop obj;
		while ((line = reader.readLine()) != null) {
			splits = line.split(",");
			if (splits.length < 7) {
			    throw new IOException("Invalid stops.txt structure with column length less than 7: " + splits.length);
			}
			
			obj = new GtfsStop();
			
			obj.stop_id = Integer.parseInt(splits[0]);
			obj.stop_name = splits[1];
			obj.stop_lat = Double.parseDouble(splits[2]);
			obj.stop_lon = Double.parseDouble(splits[3]);
			obj.zone_id = Integer.parseInt(splits[4]);
			obj.location_type = Integer.parseInt(splits[5]);
			obj.stop_timezone = splits[6];
						
			list.add(obj);
		}
		
		reader.close();
		
		GtfsStop[] out = new GtfsStop[list.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = list.get(i);
		}
		
		return out;
	}
}
