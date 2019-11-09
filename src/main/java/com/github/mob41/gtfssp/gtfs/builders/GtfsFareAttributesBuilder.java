package com.github.mob41.gtfssp.gtfs.builders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.GtfsFareAttribute;

public class GtfsFareAttributesBuilder extends AbstractGtfsBuilder<GtfsFareAttribute> {

	public GtfsFareAttributesBuilder() {
		super("fare_attributes");
	}

	@Override
	public GtfsData parseMap(Map<String, String> map) {
		return new GtfsFareAttribute(map);
	}

	@Override
	public GtfsFareAttribute[] fromStream(InputStream in, boolean skipHeader) throws IOException {
		GtfsData[] data = dataFromStream(in, skipHeader);
		GtfsFareAttribute[] out = new GtfsFareAttribute[data.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = (GtfsFareAttribute) data[i];
		}
		return out;
	}
}
