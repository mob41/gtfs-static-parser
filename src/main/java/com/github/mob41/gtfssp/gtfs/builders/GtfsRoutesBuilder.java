package com.github.mob41.gtfssp.gtfs.builders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.AbstractGtfsBuilder;
import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.row.GtfsRoute;

public class GtfsRoutesBuilder extends AbstractGtfsBuilder<GtfsRoute> {

	public GtfsRoutesBuilder() {
		super("routes");
	}

	@Override
	public GtfsData parseMap(Map<String, String> map) {
		return new GtfsRoute(map);
	}

	@Override
	public GtfsRoute[] fromStream(InputStream in, boolean skipHeader) throws IOException {
		GtfsData[] data = dataFromStream(in, skipHeader);
		GtfsRoute[] out = new GtfsRoute[data.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = (GtfsRoute) data[i];
		}
		return out;
	}

	@Override
	public String getHeaderType(String header) {
		if (header.equals("route_type") || header.equals("route_sort_order")){
			return "int";
		} else {
			return "string";
		}
	}

}
