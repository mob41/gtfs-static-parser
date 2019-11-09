package com.github.mob41.gtfssp.gtfs;

import java.util.Map;

public class GtfsStopTime extends GtfsData {
	
	public String trip_id;
	
	public String arrival_time;
	
	public String departure_time;
	
	public String stop_id;
	
	public int stop_sequence;
	
	public String stop_headsign;
	
	public int pickup_type;
	
	public int drop_off_type;
	
	public double shape_dist_traveled;
	
	public int timepoint;
	
	public GtfsStopTime(Map<String, String> map) {
		super("stop_times", map);
		
		trip_id = map.get("trip_id");
		arrival_time = map.get("arrival_time");
		departure_time = map.get("departure_time");
		stop_id = map.get("stop_id");
		stop_sequence = Integer.parseInt(map.get("stop_sequence"));
		stop_headsign = map.get("stop_headsign");
		pickup_type = map.containsKey("pickup_type") ? Integer.parseInt(map.get("pickup_type")) : -1;
		drop_off_type = map.containsKey("drop_off_type") ? Integer.parseInt(map.get("drop_off_type")) : -1;
		shape_dist_traveled =  map.containsKey("shape_dist_traveled") ? Double.parseDouble(map.get("shape_dist_traveled")) : -1;
		timepoint =  map.containsKey("timepoint") ? Integer.parseInt(map.get("timepoint")) : -1;
	}
}
