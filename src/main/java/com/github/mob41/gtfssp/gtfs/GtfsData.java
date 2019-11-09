package com.github.mob41.gtfssp.gtfs;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GtfsData {
	
	private final String dataType;
	
	private final Map<String, String> stringMap;
	
	public GtfsData(String dataType, Map<String, String> stringMap) {
		this.dataType = dataType;
		this.stringMap = stringMap;
	}

	/***
	 * Returns this GtfsData's String HashMap.
	 * @return String HashMap
	 */
	public Map<String, String> getStringMap() {
		return stringMap;
	}
	
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

	/***
	 * Returns the data type/originated file without extension (e.g. agency, routes, stops)
	 * @return
	 */
	public String getDataType() {
		return dataType;
	}

}
