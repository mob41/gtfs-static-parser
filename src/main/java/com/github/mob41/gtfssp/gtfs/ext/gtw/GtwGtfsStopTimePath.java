package com.github.mob41.gtfssp.gtfs.ext.gtw;

import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;

public class GtwGtfsStopTimePath extends GtfsData {
	
	public String path_id;
	
	public String stop_id;
	
	public int stop_sequence;
	
	public double shape_dist_traveled;
	
	public GtwGtfsStopTimePath(Map<String, String> map) {
		super("stop_time_paths", map);
		
		path_id = map.get("path_id");
		stop_id = map.get("stop_id");
		stop_sequence = Integer.parseInt(map.get("stop_sequence"));
		shape_dist_traveled =  map.containsKey("shape_dist_traveled") ? Double.parseDouble(map.get("shape_dist_traveled")) : -1;
	}
}
