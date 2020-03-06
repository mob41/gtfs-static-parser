package com.github.mob41.gtfssp.gtfs.row;

import java.util.HashMap;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;

public class GtfsFeedInfo extends GtfsData{

	public String feed_publisher_name;
	
	public String feed_publisher_url;
	
	public String feed_lang;
	
	public String feed_start_date;
	
	public String feed_end_date;
	
	public String feed_version;
	
	public String feed_contact_email;
	
	public String feed_contact_url;
	
	public GtfsFeedInfo(String feed_publisher_name, String feed_publisher_url, String feed_lang) {
		this(toMap(feed_publisher_name, feed_publisher_url, feed_lang, null, null, null, null, null));
	}
	
	public GtfsFeedInfo(String feed_publisher_name, String feed_publisher_url, String feed_lang, String feed_version) {
		this(toMap(feed_publisher_name, feed_publisher_url, feed_lang, null, null, feed_version, null, null));
	}
	
	public GtfsFeedInfo(String feed_publisher_name, String feed_publisher_url, String feed_lang, String feed_start_date, String feed_end_date, String feed_version, String feed_contact_email, String feed_contact_url) {
		this(toMap(feed_publisher_name, feed_publisher_url, feed_lang, feed_start_date, feed_end_date, feed_version, feed_contact_email, feed_contact_url));
	}
	
	public GtfsFeedInfo(Map<String, String> stringMap) {
		super("feed_info", stringMap);
		feed_publisher_name = stringMap.get("feed_publisher_name");
		feed_publisher_url = stringMap.get("feed_publisher_url");
		feed_lang = stringMap.get("feed_lang");
		feed_start_date = stringMap.get("feed_start_date");
		feed_end_date = stringMap.get("feed_end_date");
		feed_version = stringMap.get("feed_version");
		feed_contact_email = stringMap.get("feed_contact_email");
		feed_contact_url = stringMap.get("feed_contact_url");
	}
	
	private static Map<String, String> toMap(String feed_publisher_name, String feed_publisher_url, String feed_lang, String feed_start_date, String feed_end_date, String feed_version, String feed_contact_email, String feed_contact_url) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("feed_publisher_name", feed_publisher_name);
		map.put("feed_publisher_url", feed_publisher_url);
		map.put("feed_lang", feed_lang);
		map.put("feed_start_date", feed_start_date);
		map.put("feed_end_date", feed_end_date);
		map.put("feed_version", feed_version);
		map.put("feed_contact_email", feed_contact_email);
		map.put("feed_contact_url", feed_contact_url);
		return map;
	}
	
}
