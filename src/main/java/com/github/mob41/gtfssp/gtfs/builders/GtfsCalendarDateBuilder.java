package com.github.mob41.gtfssp.gtfs.builders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsCalendarDate;
import com.github.mob41.gtfssp.gtfs.GtfsData;

public class GtfsCalendarDateBuilder extends AbstractGtfsBuilder<GtfsCalendarDate> {

	public GtfsCalendarDateBuilder() {
		super("calendar_dates");
	}

	@Override
	public GtfsData parseMap(Map<String, String> map) {
		return new GtfsCalendarDate(map);
	}

	@Override
	public GtfsCalendarDate[] fromStream(InputStream in, boolean skipHeader) throws IOException {
		GtfsData[] data = dataFromStream(in, skipHeader);
		GtfsCalendarDate[] out = new GtfsCalendarDate[data.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = (GtfsCalendarDate) data[i];
		}
		return out;
	}

}
