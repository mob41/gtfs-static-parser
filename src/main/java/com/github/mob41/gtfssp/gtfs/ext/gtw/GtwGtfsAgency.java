package com.github.mob41.gtfssp.gtfs.ext.gtw;

import java.util.Map;

import com.github.mob41.gtfssp.gtfs.row.GtfsAgency;

public class GtwGtfsAgency extends GtfsAgency {
	
	public String agency_long_name;
	
	public String agency_short_name;

	public GtwGtfsAgency(Map<String, String> stringMap) {
		super(stringMap);
		agency_long_name = stringMap.get("agency_long_name");
		agency_short_name = stringMap.get("agency_short_name");
	}

}
