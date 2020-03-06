package com.github.mob41.gtfssp.gtfs.builders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.row.GtfsFrequency;

public class GtfsFrequenciesBuilder extends AbstractGtfsBuilder<GtfsFrequency> {

	public GtfsFrequenciesBuilder() {
		super("frequencies");
	}

	@Override
	public GtfsData parseMap(Map<String, String> map) {
		return new GtfsFrequency(map);
	}

	@Override
	public GtfsFrequency[] fromStream(InputStream in, boolean skipHeader) throws IOException {
		GtfsData[] data = dataFromStream(in, skipHeader);
		GtfsFrequency[] out = new GtfsFrequency[data.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = (GtfsFrequency) data[i];
		}
		return out;
	}

	@Override
	public String getHeaderType(String header) {
		if (header.equals("headway_secs") || header.equals("exact_times")){
			return "int";
		} else {
			return "string";
		}
	}

}
