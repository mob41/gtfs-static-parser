package com.github.mob41.gtfssp.gtfs.ext.hktransit;

import com.github.mob41.gtfssp.gtfs.AbstractGtfsBuilder;
import com.github.mob41.gtfssp.gtfs.GtfsLocalizedSource;
import com.github.mob41.gtfssp.gtfs.GtfsTableSource;

public class HkTransitGtfsTableSource<T> extends GtfsTableSource<T> {

	public HkTransitGtfsTableSource(String tableName, GtfsLocalizedSource[] sources) {
		super(tableName, sources);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public AbstractGtfsBuilder<T> getCustomTableBuilder(String tableName) {
		if (tableName.equals("agency")) {
			return (AbstractGtfsBuilder<T>) new HkTransitGtfsAgencyBuilder();
		} else if (tableName.equals("trips")) {
			return (AbstractGtfsBuilder<T>) new HkTransitGtfsTripsBuilder();
		}
		return null;
	}

}
