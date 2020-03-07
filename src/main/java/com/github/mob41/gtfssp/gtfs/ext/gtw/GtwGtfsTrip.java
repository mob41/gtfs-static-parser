package com.github.mob41.gtfssp.gtfs.ext.gtw;

import java.util.Map;

import com.github.mob41.gtfssp.gtfs.row.GtfsTrip;

public class GtwGtfsTrip extends GtfsTrip {
	
	public String path_id;
	
	public int journey_secs;

	public GtwGtfsTrip(Map<String, String> map) {
		super(map);
		path_id = map.get("path_id");
		journey_secs = map.containsKey("journey_secs") ? Integer.parseInt(map.get("journey_secs")) : -1;
	}

}
