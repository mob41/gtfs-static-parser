package com.github.mob41.gtfssp.gtfs.row;

import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;

public class GtfsTranslation extends GtfsData{

	public String table_name;
	
	public String field_name;
	
	public String language;
	
	public String translation;
	
	public String record_id;
	
	public String record_sub_id;
	
	public String field_value;
	
	public GtfsTranslation(Map<String, String> stringMap) {
		super("translations", stringMap);
		table_name = stringMap.get("table_name");
		field_name = stringMap.get("field_name");
		language = stringMap.get("language");
		translation = stringMap.get("translation");
		record_id = stringMap.get("record_id");
		record_sub_id = stringMap.get("record_sub_id");
		field_value = stringMap.get("field_value");
	}

}
