package com.github.mob41.gtfssp.gtfs.builders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.row.GtfsStopTime;

public class GtfsStopTimesBuilder extends AbstractGtfsBuilder<GtfsStopTime> {

	public GtfsStopTimesBuilder() {
		super("stop_times");
	}

	@Override
	public GtfsData parseMap(Map<String, String> map) {
		return new GtfsStopTime(map);
	}

	@Override
	public GtfsStopTime[] fromStream(InputStream in, boolean skipHeader) throws IOException {
		GtfsData[] data = dataFromStream(in, skipHeader);
		GtfsStopTime[] out = new GtfsStopTime[data.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = (GtfsStopTime) data[i];
		}
		return out;
	}

	@Override
	public String getHeaderType(String header) {
		if (header.equals("shape_dist_traveled")){
			return "float";
		} else if (header.equals("stop_sequence") || header.equals("pickup_type") || header.equals("drop_off_type") || header.equals("timepoint")){
			return "int";
		} else {
			return "string";
		}
	}

}
