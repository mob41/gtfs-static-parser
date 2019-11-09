package com.github.mob41.gtfssp.gtfs;

import java.util.Map;

public class GtfsFareRule extends GtfsData {
	
	public String fare_id;
	
	public String route_id;
	
	public String origin_id;
	
	public String destination_id;
	
	public String contains_id;
	
	public GtfsFareRule(Map<String, String> map) {
		super("fare_rules", map);
		
		fare_id = map.get("fare_id");
		route_id = map.get("route_id");
		origin_id = map.get("origin_id");
		destination_id = map.get("destination_id");
		contains_id = map.get("contains_id");
	}
}
