package com.github.mob41.gtfssp.gtfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GtfsStopTime extends GtfsData {
	
	public int trip_id;
	
	public String arrival_time;
	
	public String departure_time;
	
	public int stop_id;
	
	public int stop_sequence;
	
	public String stop_headsign;
	
	public int pickup_type;
	
	public int drop_off_type;
	
	public int timepoint;
	
	public GtfsStopTime() {
		
	}

	/***
	 * Get an array of GTFS-static-formatted stop times from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @return An array of stop times
	 * @throws IOException
	 */
	public static GtfsStopTime[] getStopTimes(InputStream in) throws IOException {
		return getStopTimes(in, true);
	}
	
	/***
	 * Get an array of GTFS-static-formatted stop times from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @return An array of stop times
	 * @throws IOException
	 */
	public static GtfsStopTime[] getStopTimes(InputStream in, boolean skipHeader) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		
		if (skipHeader) {
			reader.readLine();
		}
		
		List<GtfsStopTime> list = new ArrayList<GtfsStopTime>();
		String[] splits;
		String line;
		GtfsStopTime obj;
		while ((line = reader.readLine()) != null) {
			splits = line.split(",");
			if (splits.length < 9) {
			    throw new IOException("Invalid stop_times.txt structure with column length less than 7: " + splits.length);
			}
			
			obj = new GtfsStopTime();
			
			obj.trip_id = Integer.parseInt(splits[0]);
			obj.arrival_time = splits[1];
			obj.departure_time = splits[2];
			obj.stop_id = Integer.parseInt(splits[3]);
			obj.stop_sequence = Integer.parseInt(splits[4]);
			obj.stop_headsign = splits[5];
			obj.pickup_type = Integer.parseInt(splits[6]);
			obj.drop_off_type = Integer.parseInt(splits[7]);
			obj.timepoint = Integer.parseInt(splits[8]);
			
			list.add(obj);
		}
		
		reader.close();
		
		GtfsStopTime[] out = new GtfsStopTime[list.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = list.get(i);
		}
		
		return out;
	}
}
