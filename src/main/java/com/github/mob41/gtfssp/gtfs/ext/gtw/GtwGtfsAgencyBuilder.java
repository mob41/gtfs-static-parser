package com.github.mob41.gtfssp.gtfs.ext.gtw;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.builders.GtfsAgencyBuilder;
import com.github.mob41.gtfssp.gtfs.row.GtfsAgency;

public class GtwGtfsAgencyBuilder extends GtfsAgencyBuilder {

	@Override
	public GtfsData parseMap(Map<String, String> map) {
		return new GtwGtfsAgency(map);
	}

}
