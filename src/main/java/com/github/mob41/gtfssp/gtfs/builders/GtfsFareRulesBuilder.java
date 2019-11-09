package com.github.mob41.gtfssp.gtfs.builders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.GtfsFareRule;

public class GtfsFareRulesBuilder extends AbstractGtfsBuilder<GtfsFareRule> {

	public GtfsFareRulesBuilder() {
		super("fare_rules");
	}

	@Override
	public GtfsData parseMap(Map<String, String> map) {
		return new GtfsFareRule(map);
	}

	@Override
	public GtfsFareRule[] fromStream(InputStream in, boolean skipHeader) throws IOException {
		GtfsData[] data = dataFromStream(in, skipHeader);
		GtfsFareRule[] out = new GtfsFareRule[data.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = (GtfsFareRule) data[i];
		}
		return out;
	}

}
