package com.github.mob41.gtfssp.gtfs.row;

import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;

public class GtfsTranslations extends GtfsData{

	public String table_name;
	
	public String field_name;
	
	public String language;
	
	public String translation;
	
	public GtfsTranslations(Map<String, String> stringMap) {
		super("translations", stringMap);
		table_name = stringMap.get(table_name);
		field_name = stringMap.get(field_name);
		language = stringMap.get(language);
		translation = stringMap.get(translation);
	}

}
