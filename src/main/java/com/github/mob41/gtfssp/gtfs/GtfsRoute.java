package com.github.mob41.gtfssp.gtfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GtfsRoute extends GtfsData {

	public int route_id;
	
	public String agency_id;
	
	public String route_short_name;
	
	public String route_long_name;
	
	public int route_type;
	
	public String route_url;
	
	public GtfsRoute() {
		
	}

	/***
	 * Get an array of GTFS-static-formatted routes from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @return An array of routes
	 * @throws IOException
	 */
	public static GtfsRoute[] getRoutes(InputStream in) throws IOException {
		return getRoutes(in, true);
	}
	
	/***
	 * Get an array of GTFS-static-formatted routes from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @return An array of routes
	 * @throws IOException
	 */
	public static GtfsRoute[] getRoutes(InputStream in, boolean skipHeader) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		
		if (skipHeader) {
			reader.readLine();
		}
		
		List<GtfsRoute> list = new ArrayList<GtfsRoute>();
		String[] splits;
		String line;
		GtfsRoute route;
		while ((line = reader.readLine()) != null) {
			splits = line.split(",");
			if (splits.length < 6) {
			    throw new IOException("Invalid routes.txt structure with column length less than 7: " + splits.length);
			}
			
			route = new GtfsRoute();
			
			route.route_id = Integer.parseInt(splits[0]);
			route.agency_id = splits[1];
			route.route_short_name = splits[2];
			route.route_long_name = splits[3];
			route.route_type = Integer.parseInt(splits[4]);
			route.route_url = splits[5];
			
			list.add(route);
		}
		
		reader.close();
		
		GtfsRoute[] out = new GtfsRoute[list.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = list.get(i);
		}
		
		return out;
	}
}
