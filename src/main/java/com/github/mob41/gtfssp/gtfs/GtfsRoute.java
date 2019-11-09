package com.github.mob41.gtfssp.gtfs;

import java.util.Map;

public class GtfsRoute extends GtfsData {

	public String route_id;
	
	public String agency_id;
	
	public String route_short_name;
	
	public String route_long_name;
	
	public String route_desc;
	
	public int route_type;
	
	public String route_url;
	
	public String route_color;
	
	public int route_sort_order;
	
	public GtfsRoute(Map<String, String> map) {
		super("routes", map);
		
		route_id = map.get("route_id");
		agency_id = map.get("agency_id");
		route_short_name = map.get("route_short_name");
		route_long_name = map.get("route_long_name");
		route_desc = map.get("route_desc");
		route_type = Integer.parseInt(map.get("route_type"));
		route_url = map.get("route_url");
		route_color = map.get("route_color");
		route_sort_order = map.containsKey("route_sort_order") ? Integer.parseInt(map.get("route_sort_order")) : -1;
	}
}
