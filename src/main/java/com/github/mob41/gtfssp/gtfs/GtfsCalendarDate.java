package com.github.mob41.gtfssp.gtfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GtfsCalendarDate extends GtfsData {
	
	public int service_id;
	
	public int date;
	
	public int exception_type;
	
	public GtfsCalendarDate() {
		super("calendar_dates");
	}

	/***
	 * Get an array of GTFS-static-formatted dates from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @return An array of dates
	 * @throws IOException
	 */
	public static GtfsCalendarDate[] getDates(InputStream in) throws IOException {
		return getDates(in, true);
	}
	
	/***
	 * Get an array of GTFS-static-formatted dates from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @return An array of dates
	 * @throws IOException
	 */
	public static GtfsCalendarDate[] getDates(InputStream in, boolean skipHeader) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		
		if (skipHeader) {
			reader.readLine();
		}
		
		List<GtfsCalendarDate> list = new ArrayList<GtfsCalendarDate>();
		String[] splits;
		String line;
		GtfsCalendarDate obj;
		while ((line = reader.readLine()) != null) {
			splits = line.split(",");
			if (splits.length < 3) {
			    throw new IOException("Invalid calendar_dates.txt structure with column length less than 7: " + splits.length);
			}
			
			obj = new GtfsCalendarDate();
			
			obj.service_id = Integer.parseInt(splits[0]);
			obj.date = Integer.parseInt(splits[1]);
			obj.exception_type = Integer.parseInt(splits[2]);
			
			list.add(obj);
		}
		
		reader.close();
		
		GtfsCalendarDate[] out = new GtfsCalendarDate[list.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = list.get(i);
		}
		
		return out;
	}
	
}
