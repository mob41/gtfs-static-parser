package com.github.mob41.gtfssp.gtfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GtfsTrip extends GtfsData {

	public int route_id;
	
	public int service_id;
	
	public String trip_id;
	
	public GtfsTrip() {
		
	}

	/***
	 * Get an array of GTFS-static-formatted trips from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @return An array of trips
	 * @throws IOException
	 */
	public static GtfsTrip[] getTrips(InputStream in) throws IOException {
		return getTrips(in, true);
	}
	
	/***
	 * Get an array of GTFS-static-formatted trips from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @return An array of trips
	 * @throws IOException
	 */
	public static GtfsTrip[] getTrips(InputStream in, boolean skipHeader) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		
		if (skipHeader) {
			reader.readLine();
		}
		
		List<GtfsTrip> list = new ArrayList<GtfsTrip>();
		String[] splits;
		String line;
		GtfsTrip obj;
		while ((line = reader.readLine()) != null) {
			splits = line.split(",");
			if (splits.length < 3) {
			    throw new IOException("Invalid trips.txt structure with column length less than 7: " + splits.length);
			}
			
			obj = new GtfsTrip();
			
			obj.route_id = Integer.parseInt(splits[0]);
			obj.service_id = Integer.parseInt(splits[1]);
			obj.trip_id = splits[2];
			
			list.add(obj);
		}
		
		reader.close();
		
		GtfsTrip[] out = new GtfsTrip[list.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = list.get(i);
		}
		
		return out;
	}
}
