package com.github.mob41.gtfssp.gtfs.ext.gtw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.GtfsFeed;
import com.github.mob41.gtfssp.gtfs.GtfsTableSource;
import com.github.mob41.gtfssp.gtfs.row.GtfsFeedInfo;
import com.github.mob41.gtfssp.gtfs.row.GtfsStopTime;
import com.github.mob41.gtfssp.gtfs.row.GtfsTrip;

public class GtwGtfsFeed extends GtfsFeed{
	
	public static boolean CONSOLE_LOGGING = true;
	
	private List<String> pathGroupIds;
	
	private List<List<GtfsStopTime>> groups;
	
	private List<Set<String>> groupsAssocIds;
	
	private Map<String, Integer> journeySecs;

	public GtwGtfsFeed(GtfsFeedInfo feedInfo, GtfsTableSource<?>[] sources) {
		super(feedInfo, sources);
	}
	
	@Override
	protected void postFetchFeed(Map<String, GtfsData[]> map) throws IOException{
		Iterator<String> it = map.keySet().iterator();
		String key;
		GtfsData[] newStopTimePaths = null;
		GtfsData[] newTrips = null;
		while (it.hasNext()) {
			key = it.next();
			if (key.equals("stop_times")) {
				List<Map<String, String>> list = groupTrips((GtfsStopTime[]) map.get(key));
				newStopTimePaths = new GtfsData[list.size()];
				for (int i = 0; i < newStopTimePaths.length; i++) {
					newStopTimePaths[i] = new GtwGtfsStopTimePath(list.get(i));
				}
				calculateJourneySecs((GtfsStopTime[]) map.get(key));
			} else if (key.equals("trips")) {
				List<Map<String, String>> list = assignPathIdToTrips((GtfsTrip[]) map.get(key));
				newTrips = new GtfsData[list.size()];
				for (int i = 0; i < newTrips.length; i++) {
					newTrips[i] = new GtwGtfsTrip(list.get(i));
				}
			}
		}
		
		if (newStopTimePaths != null) {
			map.put("stop_time_paths", newStopTimePaths);
		}
		
		if (newTrips != null) {
			map.put("trips", newTrips);
		}
	}
	
	public static void reportMessage(String msg) {
		if (CONSOLE_LOGGING) {
			Console.sameLine(msg);
		}
	}
	
	public static void finishLine() {
		if (CONSOLE_LOGGING) {
			Console.resetLastMessage();
			Console.println("");
		}
	}
	
	private static boolean isEquals(List<GtfsStopTime> a, List<GtfsStopTime> b) {
		if (a.size() != b.size()) {
			return false;
		}
		
		for (int i = 0; i < a.size(); i++) {
			if (//(a.get(i).arrival_time != null && b.get(i).arrival_time != null && !a.get(i).arrival_time.equals(b.get(i).arrival_time)) ||
				//(a.get(i).departure_time != null && b.get(i).departure_time != null && !a.get(i).departure_time.equals(b.get(i).departure_time)) ||
				a.get(i).drop_off_type != b.get(i).drop_off_type ||
				a.get(i).pickup_type != b.get(i).pickup_type ||
				a.get(i).shape_dist_traveled != b.get(i).shape_dist_traveled ||
				(a.get(i).stop_headsign != null && b.get(i).stop_headsign != null && !a.get(i).stop_headsign.equals(b.get(i).stop_headsign)) ||
				!a.get(i).stop_id.equals(b.get(i).stop_id) ||
				a.get(i).stop_sequence != b.get(i).stop_sequence ||
				a.get(i).timepoint != b.get(i).timepoint
					) {
				return false;
			}
		}
		
		return true;
	}
	
	private Map<String, List<GtfsStopTime>> sortStopTime(GtfsStopTime[] stopTimes) {
		reportMessage("Pairing stop times with same trip IDs...");
		Map<String, List<GtfsStopTime>> byTripId = new HashMap<String, List<GtfsStopTime>>();
		GtfsStopTime stopTime;
		List<GtfsStopTime> byTripIdList;
		for (int i = 0; i < stopTimes.length; i++) {
			stopTime = stopTimes[i];
			if (!byTripId.containsKey(stopTime.trip_id)) {
				byTripId.put(stopTime.trip_id, new ArrayList<GtfsStopTime>());
			}
			byTripIdList = byTripId.get(stopTime.trip_id);
			byTripIdList.add(stopTime);
		}

		reportMessage("Sorting trip stop times...");
		Iterator<String> sortIt = byTripId.keySet().iterator();
		while (sortIt.hasNext()) {
			Collections.sort(byTripId.get(sortIt.next()), new Comparator<GtfsStopTime>() {
				
				@Override
				public int compare(GtfsStopTime o1, GtfsStopTime o2) {
					return o1.stop_sequence - o2.stop_sequence;
				}
				
			});
		}
		return byTripId;
	}
	
	private void calculateJourneySecs(GtfsStopTime[] stopTimes) {
		Map<String, List<GtfsStopTime>> byTripId = sortStopTime(stopTimes);
		
		journeySecs = new HashMap<String, Integer>();
		
		Iterator<String> it = byTripId.keySet().iterator();
		String key;
		List<GtfsStopTime> list;
		GtfsStopTime first;
		GtfsStopTime last;
		String[] arrivalTimeSplits;
		String[] departureTimeSplits;
		int arrivalTimeCalc;
		int departureTimeCalc;
		int calc;
		int lastP = -1;
		int p;
		int done = 0;
		int count = byTripId.keySet().size();
		while(it.hasNext()) {
			done++;
			
			p = (int) Math.floor(done / (float) count * 100);
			if (lastP == -1 || lastP != p) {
				lastP = p;
				reportMessage("Calculated journey seconds: " + p + "%: " + done + "/" + count);
			}
			
			key = it.next();
			list = byTripId.get(key);
			
			if (list.size() < 2) {
				continue;
			}
			
			first = list.get(0);
			last = list.get(list.size() - 1);
			
			if (first.arrival_time == null ||
					first.departure_time == null ||
					last.arrival_time == null ||
					last.arrival_time == null
					) {
				continue;
			}
			
			departureTimeSplits = first.departure_time.split(":");
			arrivalTimeSplits = last.arrival_time.split(":");
			
			if (departureTimeSplits.length < 3 || arrivalTimeSplits.length < 3) {
				continue;
			}
			
			try {
				arrivalTimeCalc = 
						Integer.parseInt(arrivalTimeSplits[0]) * 3600 + 
						Integer.parseInt(arrivalTimeSplits[1]) * 60 + 
						Integer.parseInt(arrivalTimeSplits[2]);
				departureTimeCalc = 
						Integer.parseInt(departureTimeSplits[0]) * 3600 + 
						Integer.parseInt(departureTimeSplits[1]) * 60 + 
						Integer.parseInt(departureTimeSplits[2]);
			} catch (NumberFormatException e) {
				System.out.println("Could not parse arrival/departure time for " + first.trip_id + " stop times");
				continue;
			}
			
			calc = arrivalTimeCalc - departureTimeCalc;
			
			if (calc < 0) {
				System.out.println("Negative difference of arrival, departure time in " + first.trip_id + " stop times");
				continue;
			}
			
			journeySecs.put(first.trip_id, calc);
		}
	}
	
	private List<Map<String, String>> groupTrips(GtfsStopTime[] stopTimes) {
		Map<String, List<GtfsStopTime>> byTripId = sortStopTime(stopTimes);
		
		reportMessage("Grouping trips with same path...");
		
		groups = new ArrayList<List<GtfsStopTime>>();
		groupsAssocIds = new ArrayList<Set<String>>();
		
		Set<String> groupedTripIds = new HashSet<String>();
		
		int count = byTripId.keySet().size();
		
		List<GtfsStopTime> group;
		Set<String> groupIds;
		Iterator<String> outIt = byTripId.keySet().iterator();
		int done = 0;
		int last = -1;
		while (outIt.hasNext()) {
			String outKey = outIt.next();

			if (groupedTripIds.contains(outKey)) {
				continue;
			}
			
			groupIds = new HashSet<String>();
			group = byTripId.get(outKey);

			groupIds.add(outKey);
			groupedTripIds.add(outKey);
			
			Iterator<String> inIt = byTripId.keySet().iterator();
			while (inIt.hasNext()) {
				String inKey = inIt.next();

				if (groupedTripIds.contains(inKey)) {
					continue;
				}
				
				if (isEquals(group, byTripId.get(inKey))) {
					groupedTripIds.add(inKey);
					groupIds.add(inKey);
					done++;
				}
			}
			
			groups.add(group);
			groupsAssocIds.add(groupIds);
			done++;
			int calc = (int) Math.floor(done / (float) count * 100);
			if (last == -1 || last != calc) {
				last = calc;
				reportMessage("Grouped Stop Times " + calc + "%: " + done + "/" + count + " -> " + groups.size());
			}
		}
		finishLine();
		
		reportMessage("Assigning path group IDs...");
		pathGroupIds = new ArrayList<String>();
		List<Map<String, String>> out = new ArrayList<Map<String, String>>();
		List<GtfsStopTime> els;
		Map<String, String> map;
		String key;
		for (int i = 0; i < groups.size(); i++) {
			key = Integer.toString(i);
			pathGroupIds.add(key);
			els = groups.get(i);
			for (GtfsStopTime el : els) {
				map = el.getStringMap();
				map.remove("trip_id");
				map.remove("arrival_time");
				map.remove("departure_time");
				map.put("path_id", key);
				out.add(map);
			}
			int calc = (int) Math.floor((i + 1) / (float) groups.size() * 100);
			reportMessage("Assigned IDs " + calc + "%: " + (i + 1) + "/" + groups.size());
		}
		finishLine();
		return out;
	}
	
	private List<Map<String, String>> assignPathIdToTrips(GtfsTrip[] trips) {
		List<Map<String, String>> out = new ArrayList<Map<String, String>>();
		
		int tripIndex = -1;
		Map<String, String> map;
		for (int i = 0; i < trips.length; i++) {
			for (int j = 0; j < groupsAssocIds.size(); j++) {
				if (groupsAssocIds.get(j).contains(trips[i].trip_id)) {
					tripIndex = j;
					break;
				}
			}
			
			if (tripIndex == -1) {
				System.err.println("Error: Could not find associated group with trip ID: " + trips[i].trip_id);
				continue;
			}
			
			map = trips[i].getStringMap();
			map.put("path_id", Integer.toString(tripIndex));
			
			if (journeySecs.containsKey(trips[i].trip_id)) {
				map.put("journey_secs", Integer.toString(journeySecs.get(trips[i].trip_id)));
			}
			
			out.add(map);
			int calc = (int) Math.floor((i + 1) / (float) trips.length * 100);
			reportMessage("Assigned Trips " + calc + "%: " + (i + 1) + "/" + trips.length);
		}
		finishLine();
		return out;
	}

}
