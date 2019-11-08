package com.github.mob41.gtfssp.gtfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GtfsFareAttribute extends GtfsData {
	
	public int fare_id;
	
	public double price;
	
	public String currency_type;
	
	public int payment_method;
	
	public int transfers;
	
	public String agency_id;
	
	public GtfsFareAttribute() {
		super("fare_attributes");
	}

	/***
	 * Get an array of GTFS-static-formatted fare attributes from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @return An array of fare attributes
	 * @throws IOException
	 */
	public static GtfsFareAttribute[] getFareAttributes(InputStream in) throws IOException {
		return getFareAttributes(in, true);
	}
	
	/***
	 * Get an array of GTFS-static-formatted fare attributes from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @return An array of fare attributes
	 * @throws IOException
	 */
	public static GtfsFareAttribute[] getFareAttributes(InputStream in, boolean skipHeader) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		
		if (skipHeader) {
			reader.readLine();
		}
		
		List<GtfsFareAttribute> list = new ArrayList<GtfsFareAttribute>();
		String[] splits;
		String line;
		GtfsFareAttribute obj;
		while ((line = reader.readLine()) != null) {
			splits = line.split(",");
			if (splits.length < 4) {
			    throw new IOException("Invalid fare_attributes.txt structure with column length less than 7: " + splits.length);
			}
			
			obj = new GtfsFareAttribute();
			
			obj.fare_id = Integer.parseInt(splits[0]);
			obj.price = Double.parseDouble(splits[1]);
			obj.currency_type = splits[2];
			obj.payment_method = Integer.parseInt(splits[3]);
			obj.transfers = Integer.parseInt(splits[4]);
			obj.agency_id = splits[5];
						
			list.add(obj);
		}
		
		reader.close();
		
		GtfsFareAttribute[] out = new GtfsFareAttribute[list.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = list.get(i);
		}
		
		return out;
	}
}
