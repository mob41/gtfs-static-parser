package com.github.mob41.gtfssp.gtfs.localized;

import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsStop;

public class LocalizedStops extends LocalizedGtfs {

	public LocalizedStops(GtfsStop[] defaultLocaleArray) {
		super(stopsToMaps(defaultLocaleArray));
	}
	
	public boolean setLocale(String locale, GtfsStop[] array) {
		return setLocale(locale, stopsToMaps(array));
	}
	
	public static final Map<String, Object>[] stopsToMaps(GtfsStop[] array) {
		@SuppressWarnings("unchecked")
		Map<String, Object>[] out = new Map[array.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = array[i].getMap();
		}
		return out;
	}

}
