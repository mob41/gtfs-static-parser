package com.github.mob41.gtfssp.gtfs.localized;

import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsAgency;

public class LocalizedAgencies extends LocalizedGtfs {

	public LocalizedAgencies(GtfsAgency[] defaultLocaleArray) {
		super(agenciesToMaps(defaultLocaleArray));
	}
	
	public boolean setLocale(String locale, GtfsAgency[] array) {
		return setLocale(locale, agenciesToMaps(array));
	}
	
	public static final Map<String, Object>[] agenciesToMaps(GtfsAgency[] array) {
		@SuppressWarnings("unchecked")
		Map<String, Object>[] out = new Map[array.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = array[i].getMap();
		}
		return out;
	}

}
