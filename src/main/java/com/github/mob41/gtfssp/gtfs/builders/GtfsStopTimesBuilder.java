package com.github.mob41.gtfssp.gtfs.builders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.GtfsStopTime;

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

}