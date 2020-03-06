package com.github.mob41.gtfssp.gtfs.builders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.AbstractGtfsBuilder;
import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.row.GtfsStop;

public class GtfsStopsBuilder extends AbstractGtfsBuilder<GtfsStop> {

	public GtfsStopsBuilder() {
		super("stops");
	}

	@Override
	public GtfsData parseMap(Map<String, String> map) {
		return new GtfsStop(map);
	}

	@Override
	public GtfsStop[] fromStream(InputStream in, boolean skipHeader) throws IOException {
		GtfsData[] data = dataFromStream(in, skipHeader);
		GtfsStop[] out = new GtfsStop[data.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = (GtfsStop) data[i];
		}
		return out;
	}

	@Override
	public String getHeaderType(String header) {
		if (header.equals("stop_lat") || header.equals("stop_lon")){
			return "float";
		} else if (header.equals("location_type") || header.equals("wheelchair_boarding")){
			return "int";
		} else {
			return "string";
		}
	}

}
