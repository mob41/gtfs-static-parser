package com.github.mob41.gtfssp.gtfs;

import java.util.Map;

public class GtfsFareAttribute extends GtfsData {
	
	public String fare_id;
	
	public double price;
	
	public String currency_type;
	
	public int payment_method;
	
	public int transfers;
	
	public String agency_id;
	
	public int transfer_duration;
	
	public GtfsFareAttribute(Map<String, String> map) {
		super("fare_attributes", map);
		fare_id = map.get("fare_id");
		price = Double.parseDouble(map.get("price"));
		currency_type = map.get("currency_type");
		payment_method = Integer.parseInt(map.get("payment_method"));
		transfers = Integer.parseInt(map.get("transfers"));
		agency_id = map.get("agency_id");
		transfer_duration = map.containsKey("transfer_duration") ? Integer.parseInt(map.get("transfer_duration")) : -1;
	}
}
