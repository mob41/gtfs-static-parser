package com.github.mob41.gtfssp.gtfs.row;

import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;

public class GtfsFrequency extends GtfsData {
	
	public String trip_id;
	
	public String start_time;
	
	public String end_time;
	
	public int headway_secs;
	
	public int exact_times;
	
	public GtfsFrequency(Map<String, String> map) {
		super("frequencies", map);
		
		trip_id = map.get("trip_id");
		start_time = map.get("start_time");
		end_time = map.get("end_time");
		headway_secs = Integer.parseInt(map.get("headway_secs"));
		exact_times = map.containsKey("exact_times") ? Integer.parseInt(map.get("exact_times")) : -1;
	}
}
