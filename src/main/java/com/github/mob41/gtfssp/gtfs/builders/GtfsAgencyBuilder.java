package com.github.mob41.gtfssp.gtfs.builders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.AbstractGtfsBuilder;
import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.row.GtfsAgency;

public class GtfsAgencyBuilder extends AbstractGtfsBuilder<GtfsAgency> {

	public GtfsAgencyBuilder() {
		super("agency");
	}

	@Override
	public GtfsData parseMap(Map<String, String> map) {
		return new GtfsAgency(map);
	}

	@Override
	public GtfsAgency[] fromStream(InputStream in, boolean skipHeader) throws IOException {
		GtfsData[] data = dataFromStream(in, skipHeader);
		GtfsAgency[] out = new GtfsAgency[data.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = (GtfsAgency) data[i];
		}
		return out;
	}

	@Override
	public String getHeaderType(String header) {
		return "string";
	}

}
