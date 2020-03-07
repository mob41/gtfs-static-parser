package com.github.mob41.gtfssp.gtfs.ext.hktransit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.GtfsFeed;
import com.github.mob41.gtfssp.gtfs.GtfsLocalizedSource;
import com.github.mob41.gtfssp.gtfs.GtfsTableSource;
import com.github.mob41.gtfssp.gtfs.ext.gtw.Console;
import com.github.mob41.gtfssp.gtfs.ext.gtw.GtwGtfsFeed;
import com.github.mob41.gtfssp.gtfs.ext.gtw.GtwGtfsStopTimePath;
import com.github.mob41.gtfssp.gtfs.ext.gtw.GtwGtfsTrip;
import com.github.mob41.gtfssp.gtfs.row.GtfsAgency;
import com.github.mob41.gtfssp.gtfs.row.GtfsFeedInfo;
import com.github.mob41.gtfssp.gtfs.row.GtfsRoute;
import com.github.mob41.gtfssp.gtfs.row.GtfsStop;
import com.github.mob41.gtfssp.gtfs.row.GtfsTranslation;
import com.github.mob41.gtfssp.gtfs.row.GtfsTrip;

public class HkTransitGtfsFeed extends GtwGtfsFeed {
	
	private static final String PREFIX = "https://static.data.gov.hk/td/pt-headway-";
	
	private static final String SUFFIX = ".txt";
	
	private static final String[] LANG_LOCAL = {
			"en",
			"zh-HK",
			"zh-CN"
	};
	
	private static final String[] LANG_ONLINE = {
			"en",
			"tc",
			"sc"
	};

	public HkTransitGtfsFeed() {
		this(generateFeedInfo(), generateSources());
	}

	public HkTransitGtfsFeed(GtfsTableSource<?>[] sources) {
		this(generateFeedInfo(), sources);
	}

	public HkTransitGtfsFeed(GtfsFeedInfo feedInfo, GtfsTableSource<?>[] sources) {
		super(feedInfo, sources);
	}
	
	@Override
	protected void postFetchFeed(Map<String, GtfsData[]> map) throws IOException{
		super.postFetchFeed(map);
		
		groupSimilarRoutes((GtfsRoute[]) map.get("routes"));
		GtfsRoute[] outRoutes = new GtfsRoute[newRoutes.size()];
		for (int i = 0; i < outRoutes.length; i++) {
			outRoutes[i] = newRoutes.get(i);
		}
		map.put("routes", outRoutes);
		
		modifyTrips(map);
	}
	
	private Map<String, List<GtfsRoute>> routeGroups;
	private List<GtfsRoute> newRoutes;
	
	private void modifyTranslations(Map<String, GtfsData[]> map, String routeId, String tripId) {
		GtfsTranslation[] origin = (GtfsTranslation[]) map.get("translations");
		List<GtfsTranslation> list = new ArrayList<GtfsTranslation>();
		GtfsTranslation trans;
		for (int i = 0; i < origin.length; i++) {
			trans = origin[i];
			if (!trans.table_name.equals("routes") ||
					!trans.record_id.equals(routeId)) {
				list.add(trans);
				continue;
			}
			
			if (trans.field_name.equals("route_long_name")) {
				trans.table_name = "trips";
				trans.record_id = tripId;
				trans.field_name = "trip_headsign";
				trans.getStringMap().put("table_name", "trips");
				trans.getStringMap().put("record_id", tripId);
				trans.getStringMap().put("field_name", "trip_headsign");
				list.add(trans);
			}
		}
		GtfsTranslation[] outTrans = new GtfsTranslation[origin.length];
		for (int i = 0; i < outTrans.length; i++) {
			outTrans[i] = list.get(i);
		}
		map.put("translations", outTrans);
	}
	
	private static GtwGtfsTrip getTripByTripId(GtwGtfsTrip[] trips, String tripId) {
		for (int i = 0; i < trips.length; i++) {
			if (trips[i].trip_id.equals(tripId)) {
				return trips[i];
			}
		}
		return null;
	}
	
	private static GtwGtfsStopTimePath[] getFirstLastStopByPathId(Map<String, GtfsData[]> map, String pathId){
		GtwGtfsStopTimePath[] paths = (GtwGtfsStopTimePath[]) map.get("stop_time_paths");
		
		Arrays.sort(paths, new Comparator<GtwGtfsStopTimePath>() {
			@Override
			public int compare(GtwGtfsStopTimePath o1, GtwGtfsStopTimePath o2) {
				return o1.stop_sequence - o2.stop_sequence;
			}
		});
		
		Arrays.sort(paths, new Comparator<GtwGtfsStopTimePath>() {
			@Override
			public int compare(GtwGtfsStopTimePath o1, GtwGtfsStopTimePath o2) {
				return o1.path_id.compareTo(o2.path_id);
			}
		});
		
		List<GtwGtfsStopTimePath> path = new ArrayList<GtwGtfsStopTimePath>();
		for (int i = 0; i < paths.length; i++) {
			if (paths[i].path_id.equals(pathId)) {
				path.add(paths[i]);
			}
		}
		
		if (path.size() < 2) {
			return null;
		}
		
		GtwGtfsStopTimePath[] out = new GtwGtfsStopTimePath[] {
			path.get(0),
			path.get(path.size() - 1)
		};
		return out;
	}
	
	private void modifyTrips(Map<String, GtfsData[]> map) {
		GtfsData[] data = map.get("trips");
		GtwGtfsTrip[] trips = new GtwGtfsTrip[data.length];
		for (int i = 0; i < trips.length; i++) {
			trips[i] = (GtwGtfsTrip) data[i];
		}
		Iterator<String> it = routeGroups.keySet().iterator();
		String key;
		List<GtfsRoute> list;
		GtfsRoute route;
		int calc;
		int last = -1;
		int count = routeGroups.size();
		int done = 0;
		GtwGtfsStopTimePath[] originFirstLastStops;
		GtwGtfsStopTimePath[] targetFirstLastStops;
		GtwGtfsTrip originTrip;
		while (it.hasNext()) {
			key = it.next();
			done++;
			
			list = routeGroups.get(key);
			originTrip = getTripByTripId(trips, key);
			originFirstLastStops = getFirstLastStopByPathId(map, originTrip.path_id);
			if (originFirstLastStops == null) {
				System.out.println("Error finding origin path ID " + originTrip.path_id + " for " + key + " to get direction ID");
				continue;
			}
			for (int i = 0; i < list.size(); i++) {
				route = list.get(i);
				for (int j = 0; j < trips.length; j++) {
					if (trips[j].route_id.equals(route.route_id)) {
						targetFirstLastStops = getFirstLastStopByPathId(map, trips[j].path_id);
						
						if (targetFirstLastStops == null) {
							System.out.println("Error finding path ID " + trips[j].path_id + " for " + trips[j].trip_id + " to get direction ID");
							continue;
						}
						
						int directionId;
						if (originFirstLastStops[0].stop_id.equals(targetFirstLastStops[0].stop_id)) {
							directionId = 1;
						} else if (originFirstLastStops[1].stop_id.equals(targetFirstLastStops[0].stop_id)) {
							directionId = 2;
						} else {
							directionId = 1;
							System.out.println("Unknown direction ID for " + trips[j].trip_id);
						}
						
						String blockId = key + "-" + directionId;
						
						trips[j].route_id = key;
						trips[j].trip_headsign = route.route_long_name;
						trips[j].block_id = blockId;
						trips[j].direction_id = directionId;
						trips[j].getStringMap().put("route_id", key);
						trips[j].getStringMap().put("trip_headsign", route.route_long_name);
						trips[j].getStringMap().put("block_id", blockId);
						trips[j].getStringMap().put("direction_id", Integer.toString(directionId));
						modifyTranslations(map, key, trips[j].trip_id);
					}
				}
			}
			
			calc = (int) Math.floor(done / (float) count * 100);
			if (last == -1 || last != calc) {
				last = calc;
				reportMessage("Modifying trips and translations: " + calc + "%: " + done + "/" + count);
			}
		}
		finishLine();
	}
	
	private void groupSimilarRoutes(GtfsRoute[] routes) {
		Set<String> ids = new HashSet<String>();
		routeGroups = new HashMap<String, List<GtfsRoute>>();
		newRoutes = new ArrayList<GtfsRoute>();
		List<GtfsRoute> list;
		int calc;
		int last = -1;
		for (int i = 0; i < routes.length; i++)  {
			if (ids.contains(routes[i].route_id)) {
				continue;
			}
			
			newRoutes.add(routes[i]);
			list = new ArrayList<GtfsRoute>();
			
			for (int j = 0; j < routes.length; j++) {
				if (ids.contains(routes[j].route_id)) {
					continue;
				}
				
				if (routes[i].agency_id.equals(routes[j].agency_id) &&
						routes[i].route_short_name.equals(routes[j].route_short_name)) {
					ids.add(routes[j].route_id);
					list.add(routes[j]);
				}
			}
			
			if (list.size() > 0) {
				routeGroups.put(routes[i].route_id, list);
			}
			
			calc = (int) Math.floor((i + 1) / (float) routes.length * 100);
			if (last == -1 || last != calc) {
				last = calc;
				reportMessage("Grouping Routes: " + calc + "%: " + (i + 1) + "/" + routes.length);
			}
		}
		finishLine();
	}
	
	public static GtfsFeedInfo generateFeedInfo() {
		return new GtfsFeedInfo(
				"DATA.GOV.HK (generated from gtfs-static-parser)",
				"https://data.gov.hk",
				"en",
				Long.toString(System.currentTimeMillis())
				);
	}
	
	public static GtfsTableSource<?>[] generateSources() {
		GtfsTableSource<?>[] tables = new HkTransitGtfsTableSource<?>[GtfsFeed.TABLES.length];
		GtfsLocalizedSource[] localizedSources;
		for (int i = 0; i < GtfsFeed.TABLES.length; i++) {
			boolean translationAllowed = 
					GtfsFeed.TABLES[i].equals("stops") ||
					GtfsFeed.TABLES[i].equals("routes") || 
					GtfsFeed.TABLES[i].equals("trips") || 
					GtfsFeed.TABLES[i].equals("agency");
			localizedSources = new GtfsLocalizedSource[translationAllowed ? LANG_LOCAL.length : 1];
			for (int j = 0; j < LANG_LOCAL.length; j++) {
				try {
					localizedSources[j] = new GtfsLocalizedSource(
							LANG_LOCAL[j], 
							PREFIX + LANG_ONLINE[j] + "/" + GtfsFeed.TABLES[i] + SUFFIX
							);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (!translationAllowed) {
					break;
				}
			}
			tables[i] = new HkTransitGtfsTableSource<>(GtfsFeed.TABLES[i], localizedSources);
		}
		return tables;
	}

}
