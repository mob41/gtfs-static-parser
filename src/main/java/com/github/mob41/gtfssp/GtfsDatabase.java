package com.github.mob41.gtfssp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.localized.LocalizedGtfsData;

public class GtfsDatabase {

	private List<GtfsData> list;
	
	public GtfsDatabase() {
		list = new ArrayList<GtfsData>();
	}
	
	public void addData(GtfsData data) {
		list.add(data);
	}
	
	public void addData(GtfsData[] data) {
		for (GtfsData el : data) {
			addData(el);
		}
	}
	
	public void removeData(GtfsData data) {
		list.remove(data);
	}
	
	public void removeData(GtfsData[] data) {
		for (GtfsData el : data) {
			removeData(el);
		}
	}
	
	public int getTotalData() {
		return list.size();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Map<String, Object>> obj;
		Map<String, Object>[] array;
		String key;
		for (GtfsData data : list) {
			key = data.getDataType();
			
			if (!map.containsKey(key)) {
				map.put(key, obj = new ArrayList<Map<String, Object>>());
			} else {
				obj = (List<Map<String, Object>>) map.get(key);
			}
			
			if (data instanceof LocalizedGtfsData) {
				array = ((LocalizedGtfsData) data).getMaps();
				for (Map<String, Object> lgMap : array) {
					obj.add(lgMap);
				}
			} else {
				obj.add(data.getMap());
			}
		}
		
		return map;
	}

}
