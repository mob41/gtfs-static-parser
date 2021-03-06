package com.github.mob41.gtfssp.gtfs.builders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.AbstractGtfsBuilder;
import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.row.GtfsCalendar;

public class GtfsCalendarBuilder extends AbstractGtfsBuilder<GtfsCalendar> {

	public GtfsCalendarBuilder() {
		super("calendar");
	}

	@Override
	public GtfsData parseMap(Map<String, String> map) {
		return new GtfsCalendar(map);
	}

	@Override
	public GtfsCalendar[] fromStream(InputStream in, boolean skipHeader) throws IOException {
		GtfsData[] data = dataFromStream(in, skipHeader);
		GtfsCalendar[] out = new GtfsCalendar[data.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = (GtfsCalendar) data[i];
		}
		return out;
	}

	@Override
	public String getHeaderType(String header) {
		if (header.equals("service_id")) {
			return "string";
		} else {
			return "int";
		}
	}

}
