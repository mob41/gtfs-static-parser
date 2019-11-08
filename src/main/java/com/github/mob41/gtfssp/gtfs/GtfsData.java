package com.github.mob41.gtfssp.gtfs;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GtfsData {
	
	/***
	 * Returns this GtfsData as a HashMap.
	 * @return HashMap GTFS data.
	 */
	public Map<String, Object> getMap() {
		try {
			Map<String, Object> myObjectAsDict = new HashMap<>();    
			Field[] allFields = this.getClass().getDeclaredFields();
		    for (Field field : allFields) {
		        Object value = field.get(this);
		        myObjectAsDict.put(field.getName(), value);
		    }
		    return myObjectAsDict;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
