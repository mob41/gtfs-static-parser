package com.github.mob41.gtfssp.gtfs;

import java.util.Map;

public class GtfsTrip extends GtfsData {

	public String route_id;
	
	public String service_id;
	
	public String trip_id;
	
	public GtfsTrip(Map<String, String> map) {
		super("trips", map);
		
		route_id = map.get("route_id");
		service_id = map.get("service_id");
		trip_id = map.get("trip_id");
	}
}
