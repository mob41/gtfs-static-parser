package com.github.mob41.gtfssp.gtfs.ext.gtw;

import java.util.Map;

import com.github.mob41.gtfssp.gtfs.row.GtfsTrip;

public class GtwGtfsTrip extends GtfsTrip {
	
	public String path_id;

	public GtwGtfsTrip(Map<String, String> map) {
		super(map);
		path_id = map.get("path_id");		
	}

}
