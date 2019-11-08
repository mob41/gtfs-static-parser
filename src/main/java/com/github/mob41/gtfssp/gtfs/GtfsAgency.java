package com.github.mob41.gtfssp.gtfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GtfsAgency extends GtfsData{
	
	public GtfsAgency() {
		super("agencies");
	}

	public String agency_id;

	public String agency_name;
	
	public String agency_url;
	
	public String agency_timezone;
	
	public String agency_lang;
	
	public String agency_phone;
	
	public String agency_email;

	/***
	 * Get an array of GTFS-static-formatted agencies from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @return An array of agencies
	 * @throws IOException
	 */
	public static GtfsAgency[] getAgencies(InputStream in) throws IOException {
		return getAgencies(in, true);
	}
	
	/***
	 * Get an array of GTFS-static-formatted agencies from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @return An array of agencies
	 * @throws IOException
	 */
	public static GtfsAgency[] getAgencies(InputStream in, boolean skipHeader) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		
		if (skipHeader) {
			reader.readLine();
		}
		
		List<GtfsAgency> list = new ArrayList<GtfsAgency>();
		String[] splits;
		String line;
		GtfsAgency agency;
		while ((line = reader.readLine()) != null) {
			splits = line.split(",");
			if (splits.length < 7) {
			    throw new IOException("Invalid agency.txt structure with column length less than 7: " + splits.length);
			}
			
			agency = new GtfsAgency();
			
			agency.agency_id = splits[0];
			agency.agency_name = splits[1];
			agency.agency_url = splits[2];
			agency.agency_timezone = splits[3];
			agency.agency_lang = splits[4];
			agency.agency_phone = splits[5];
			agency.agency_email = splits[6];
			
			list.add(agency);
		}
		
		reader.close();
		
		GtfsAgency[] agencies = new GtfsAgency[list.size()];
		for (int i = 0; i < agencies.length; i++) {
			agencies[i] = list.get(i);
		}
		
		return agencies;
	}
	
}
