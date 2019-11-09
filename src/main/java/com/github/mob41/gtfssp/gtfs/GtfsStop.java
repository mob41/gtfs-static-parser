package com.github.mob41.gtfssp.gtfs;

import java.util.Map;

public class GtfsStop extends GtfsData {
	
	public String stop_id;
	
	public String stop_code;
	
	public String stop_name;
	
	public String stop_desc;
	
	public boolean hasLatLon;
	
	public double stop_lat;
	
	public double stop_lon;
	
	public String zone_id;
	
	public String stop_url;
	
	public int location_type;
	
	public String parent_station;
	
	public String stop_timezone;
	
	public int wheelchair_boarding;
	
	public String level_id;
	
	public String platform_code;
	
	public GtfsStop(Map<String, String> map) {
		super("stops", map);
		
		stop_id = map.get("stop_id");
		stop_code = map.get("stop_code");
		stop_name = map.get("stop_name");
		stop_desc = map.get("stop_desc");
		if (hasLatLon = map.containsKey("stop_lat") && map.containsKey("stop_lon")) {
			stop_lat = Double.parseDouble(map.get("stop_lat"));
			stop_lon = Double.parseDouble(map.get("stop_lon"));
		}
		zone_id = map.get("zone_id");
		stop_url = map.get("stop_url");
		location_type = map.containsKey("location_type") ? Integer.parseInt(map.get("location_type")) : -1;
		parent_station = map.get("parent_station");
		stop_timezone = map.get("stop_timezone");
		wheelchair_boarding = map.containsKey("wheelchair_boarding") ? Integer.parseInt(map.get("wheelchair_boarding")) : -1;
		level_id = map.get("level_id");
		platform_code = map.get("platform_code");
	}
}
