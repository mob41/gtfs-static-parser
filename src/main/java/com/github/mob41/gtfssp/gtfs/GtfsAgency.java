package com.github.mob41.gtfssp.gtfs;

import java.util.Map;

public class GtfsAgency extends GtfsData{
	
	public GtfsAgency(Map<String, String> stringMap) {
		super("agency", stringMap);
		agency_id = stringMap.get("agency_id");
		agency_name = stringMap.get("agency_name");
		agency_url = stringMap.get("agency_url");
		agency_timezone = stringMap.get("agency_timezone");
		agency_lang = stringMap.get("agency_lang");
		agency_phone = stringMap.get("agency_phone");
		agency_fare_url = stringMap.get("agency_fare_url");
		agency_email = stringMap.get("agency_email");
	}

	public String agency_id;

	public String agency_name;
	
	public String agency_url;
	
	public String agency_timezone;
	
	public String agency_lang;
	
	public String agency_phone;
	
	public String agency_fare_url;
	
	public String agency_email;
	
}
