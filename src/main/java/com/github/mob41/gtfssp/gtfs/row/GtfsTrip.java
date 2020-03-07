package com.github.mob41.gtfssp.gtfs.row;

import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;

public class GtfsTrip extends GtfsData {

	public String route_id;
	
	public String service_id;
	
	public String trip_id;
	
	public String trip_headsign;
	
	public String trip_short_name;
	
	public int direction_id;
	
	public String block_id;
	
	public String shape_id;
	
	public int wheelchair_accessible;
	
	public int bikes_allowed;
	
	public GtfsTrip(Map<String, String> map) {
		super("trips", map);
		
		route_id = map.get("route_id");
		service_id = map.get("service_id");
		trip_id = map.get("trip_id");
		trip_headsign = map.get("trip_headsign");
		trip_short_name = map.get("trip_short_name");
		direction_id = map.containsKey("direction_id") ? Integer.parseInt(map.get("direction_id")) : -1;
		block_id = map.get("block_id");
		shape_id = map.get("shape_id");
		wheelchair_accessible = map.containsKey("wheelchair_accessible") ? Integer.parseInt(map.get("wheelchair_accessible")) : -1;
		bikes_allowed = map.containsKey("bikes_allowed") ? Integer.parseInt(map.get("bikes_allowed")) : -1;
	}
}
