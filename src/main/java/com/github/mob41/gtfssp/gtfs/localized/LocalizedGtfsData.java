package com.github.mob41.gtfssp.gtfs.localized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;

public class LocalizedGtfsData extends GtfsData{
	
	private Map<String, Object>[] defaultArray;
	
	private Map<String, Map<String, Object>[]> map;

	public LocalizedGtfsData(String dataType, Map<String, Object>[] defaultLocaleArray) {
		super(dataType);
		map = new HashMap<String, Map<String, Object>[]>();
		defaultArray = defaultLocaleArray;
		map.put("en", defaultLocaleArray);
	}
	
	public boolean setLocale(String locale, Map<String, Object>[] array) {
		if (defaultArray.length != array.length) {
			return false;
		}
		map.put(locale, array);
		return true;
	}
	
	/***
	 * <b>Warning:</b> <code>getMap</code> cannot be called on LocalizedGtfsData. Calling this will throw an IllegalStateException.
	 * If you want to access its data, call <code>getMaps</code> and iterate its children.
	 */
	@Override
	public Map<String, Object> getMap() {
		throw new IllegalStateException("getMap cannot be called on LocalizedGtfsData");
	}
	
	/***
	 * Finds the deltas in each localized GTFS data and combines them into one <code>HashMap</code> array.
	 * @return HashMap array
	 */
	public Map<String, Object>[] getMaps() {
		List<String> deltas = new ArrayList<String>();
		
		String localeKey;
		String mapKey;
		Map<String, Object> defMap;
		Map<String, Object> forMap;
		Map<String, Object>[] current;
		Iterator<String> localeIt = map.keySet().iterator();
		Iterator<String> objIt;
		int i;
		while (localeIt.hasNext()) {
			localeKey = localeIt.next();
			current = map.get(localeKey);
			for (i = 0; i < defaultArray.length; i++) {
				defMap = defaultArray[i];
				forMap = current[i];
				objIt = defMap.keySet().iterator();
				while (objIt.hasNext()) {
					mapKey = objIt.next();
					if (!defMap.get(mapKey).equals(forMap.get(mapKey))) {
						deltas.add(mapKey);
					}
				}
			}
		}

		@SuppressWarnings("unchecked")
		Map<String, Object>[] out = new Map[defaultArray.length];
		Map<String, Object> valMap;
		
		for (i = 0; i < defaultArray.length; i++) {
			valMap = new HashMap<String, Object>();
			valMap.putAll(defaultArray[i]);
			
			for (String deltaKey : deltas) {
				localeIt = map.keySet().iterator();
				while (localeIt.hasNext()) {
					localeKey = localeIt.next();
					valMap.put(deltaKey + "_" + localeKey, map.get(localeKey)[i].get(deltaKey));
				}
			}
			
			out[i] = valMap;
		}
		return out;
	}

}
