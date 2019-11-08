package com.github.mob41.gtfssp.gtfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GtfsCalendar extends GtfsData {
	
	public int service_id;
	
	public int monday;
	
	public int tuesday;
	
	public int wednesday;
	
	public int thursday;
	
	public int friday;
	
	public int saturday;
	
	public int sunday;

	public int start_date;
	
	public int end_date;
	
	public GtfsCalendar() {
		super("calendars");
	}

	/***
	 * Get an array of GTFS-static-formatted calendars from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @return An array of calendars
	 * @throws IOException
	 */
	public static GtfsCalendar[] getCalendars(InputStream in) throws IOException {
		return getCalendars(in, true);
	}
	
	/***
	 * Get an array of GTFS-static-formatted calendars from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @return An array of calendars
	 * @throws IOException
	 */
	public static GtfsCalendar[] getCalendars(InputStream in, boolean skipHeader) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		
		if (skipHeader) {
			reader.readLine();
		}
		
		List<GtfsCalendar> list = new ArrayList<GtfsCalendar>();
		String[] splits;
		String line;
		GtfsCalendar obj;
		while ((line = reader.readLine()) != null) {
			splits = line.split(",");
			if (splits.length < 10) {
			    throw new IOException("Invalid calendar.txt structure with column length less than 7: " + splits.length);
			}
			
			obj = new GtfsCalendar();
			
			obj.service_id = Integer.parseInt(splits[0]);
			obj.monday = Integer.parseInt(splits[1]);
			obj.tuesday = Integer.parseInt(splits[2]);
			obj.wednesday = Integer.parseInt(splits[3]);
			obj.thursday = Integer.parseInt(splits[4]);
			obj.friday = Integer.parseInt(splits[5]);
			obj.saturday = Integer.parseInt(splits[6]);
			obj.sunday = Integer.parseInt(splits[7]);
			obj.start_date = Integer.parseInt(splits[8]);
			obj.end_date = Integer.parseInt(splits[9]);
			
			list.add(obj);
		}
		
		reader.close();
		
		GtfsCalendar[] out = new GtfsCalendar[list.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = list.get(i);
		}
		
		return out;
	}
}
