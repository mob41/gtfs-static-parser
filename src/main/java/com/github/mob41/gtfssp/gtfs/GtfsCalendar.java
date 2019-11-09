package com.github.mob41.gtfssp.gtfs;

import java.util.Map;

public class GtfsCalendar extends GtfsData {
	
	public String service_id;
	
	public int monday;
	
	public int tuesday;
	
	public int wednesday;
	
	public int thursday;
	
	public int friday;
	
	public int saturday;
	
	public int sunday;

	public int start_date;
	
	public int end_date;
	
	public GtfsCalendar(Map<String, String> map) {
		super("calendar", map);
		service_id = map.get("service_id");
		monday = Integer.parseInt(map.get("monday"));
		tuesday = Integer.parseInt(map.get("tuesday"));
		wednesday = Integer.parseInt(map.get("wednesday"));
		thursday = Integer.parseInt(map.get("thursday"));
		friday = Integer.parseInt(map.get("friday"));
		saturday = Integer.parseInt(map.get("saturday"));
		sunday = Integer.parseInt(map.get("sunday"));
		start_date = Integer.parseInt(map.get("start_date"));
		end_date = Integer.parseInt(map.get("end_date"));
	}
}