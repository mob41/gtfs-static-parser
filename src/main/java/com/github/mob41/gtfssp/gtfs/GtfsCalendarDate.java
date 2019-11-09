package com.github.mob41.gtfssp.gtfs;

import java.util.Map;

public class GtfsCalendarDate extends GtfsData {
	
	public String service_id;
	
	public int date;
	
	public int exception_type;
	
	public GtfsCalendarDate(Map<String, String> map) {
		super("calendar_dates", map);
		service_id = map.get("service_id");
		date = Integer.parseInt(map.get("date"));
		exception_type = Integer.parseInt(map.get("exception_type"));
	}
}
