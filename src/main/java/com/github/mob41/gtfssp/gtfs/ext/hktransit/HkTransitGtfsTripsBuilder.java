package com.github.mob41.gtfssp.gtfs.ext.hktransit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.builders.GtfsTripsBuilder;
import com.github.mob41.gtfssp.gtfs.ext.gtw.Console;
import com.github.mob41.gtfssp.gtfs.ext.gtw.GtwGtfsAgency;
import com.github.mob41.gtfssp.gtfs.row.GtfsAgency;
import com.github.mob41.gtfssp.gtfs.row.GtfsTrip;

public class HkTransitGtfsTripsBuilder extends GtfsTripsBuilder {
	
	public static boolean CONSOLE_LOGGING = true;
	
	@Override
	protected boolean isLocaleManaged(String locale) {
		return true;
	}
	
	private static GtfsTrip[] toInstances(List<Map<String, String>> list) {
		GtfsTrip[] out = new GtfsTrip[list.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = new GtfsTrip(list.get(i));
		}
		return out;
	}
	
	private static List<Map<String, String>> toList(GtfsTrip[] objs) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < objs.length; i++) {
			list.add(objs[i].getStringMap());
		}
		return list;
	}
	
	@Override
	protected GtfsTrip[] handle(String locale, GtfsTrip[] objs) {
		return toInstances(handle(locale, toList(objs)));
	}
	
	protected void reportMessage(String msg) {
		if (CONSOLE_LOGGING) {
			Console.sameLine(msg);
		}
	}
	
	protected void finishLine() {
		if (CONSOLE_LOGGING) {
			Console.resetLastMessage();
			Console.println("");
		}
	}
	
	@Override
	protected List<Map<String, String>> handle(String locale, List<Map<String, String>> objs) {
		reportMessage("Modifying " + objs.size() + " trips to support block_id and direction_id...");
		String[] splits;
		Map<String, String> map;
		for (int i = 0; i < objs.size(); i++) {
			map = objs.get(i);
			splits = map.get("trip_id").split("-");
			if (splits.length < 4) {
				continue;
			}
			map.put("block_id", splits[0] + "-" + splits[1]);
			map.put("direction_id", splits[1]);
		}
		finishLine();
		return objs;
	}
}
