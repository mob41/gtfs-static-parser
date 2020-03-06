package com.github.mob41.gtfssp.gtfs.builders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.row.GtfsTrip;

public class GtfsTripsBuilder extends AbstractGtfsBuilder<GtfsTrip> {

	public GtfsTripsBuilder() {
		super("trips");
	}

	@Override
	public GtfsData parseMap(Map<String, String> map) {
		return new GtfsTrip(map);
	}

	@Override
	public GtfsTrip[] fromStream(InputStream in, boolean skipHeader) throws IOException {
		GtfsData[] data = dataFromStream(in, skipHeader);
		GtfsTrip[] out = new GtfsTrip[data.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = (GtfsTrip) data[i];
		}
		return out;
	}

	@Override
	public String getHeaderType(String header) {
		return "string";
	}

}
