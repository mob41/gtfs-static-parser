package com.github.mob41.gtfssp.gtfs.localized;

import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsRoute;

public class LocalizedRoutes extends LocalizedGtfs {

	public LocalizedRoutes(GtfsRoute[] defaultLocaleArray) {
		super(routesToMaps(defaultLocaleArray));
	}
	
	public boolean setLocale(String locale, GtfsRoute[] array) {
		return setLocale(locale, routesToMaps(array));
	}
	
	public static final Map<String, Object>[] routesToMaps(GtfsRoute[] array) {
		@SuppressWarnings("unchecked")
		Map<String, Object>[] out = new Map[array.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = array[i].getMap();
		}
		return out;
	}

}
