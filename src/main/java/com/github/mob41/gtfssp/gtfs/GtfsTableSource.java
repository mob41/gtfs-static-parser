package com.github.mob41.gtfssp.gtfs;

import java.io.IOException;

import com.github.mob41.gtfssp.gtfs.builders.GtfsAgencyBuilder;
import com.github.mob41.gtfssp.gtfs.builders.GtfsCalendarBuilder;
import com.github.mob41.gtfssp.gtfs.builders.GtfsCalendarDatesBuilder;
import com.github.mob41.gtfssp.gtfs.builders.GtfsFareAttributesBuilder;
import com.github.mob41.gtfssp.gtfs.builders.GtfsFareRulesBuilder;
import com.github.mob41.gtfssp.gtfs.builders.GtfsFrequenciesBuilder;
import com.github.mob41.gtfssp.gtfs.builders.GtfsRoutesBuilder;
import com.github.mob41.gtfssp.gtfs.builders.GtfsStopTimesBuilder;
import com.github.mob41.gtfssp.gtfs.builders.GtfsStopsBuilder;
import com.github.mob41.gtfssp.gtfs.builders.GtfsTripsBuilder;
import com.github.mob41.gtfssp.gtfs.row.GtfsTranslation;

public class GtfsTableSource<T> {
	
	private final String tableName;
	
	private final GtfsLocalizedSource[] sources;
	
	private final String defaultLocale;
	
	private GtfsTranslation[] translations = null;
	
	public GtfsTableSource(String tableName, GtfsLocalizedSource[] sources) {
		this(tableName, sources, "en");
	}

	public GtfsTableSource(String tableName, GtfsLocalizedSource[] sources, String defaultLocale) {
		this.tableName = tableName;
		this.sources = sources;
		this.defaultLocale = defaultLocale;
	}

	public String getTableName() {
		return tableName;
	}

	public GtfsLocalizedSource[] getSources() {
		return sources;
	}
	
	public T[] fetchData() throws IOException {
		AbstractGtfsBuilder<T> builder = getTableBuilder(tableName);
		builder.setDefaultLocale(defaultLocale);
		int i;
		
		T[] out = null;
		GtfsLocalizedSource source;
		for (i = 0; i < sources.length; i++) {
			source = sources[i];
			System.out.println("Fetching localized source (" + (i + 1) + "/" + sources.length + ") " + source.getLanguage());
			if (defaultLocale.equals(source.getLanguage())) {
				out = builder.setDefaultLocaleFromStream(source.getInputStream());
			} else {
				builder.setLocaleFromStream(source.getLanguage(), source.getInputStream());
			}
		}
		translations = builder.getTranslations();
		return out;
	}
	
	public GtfsTranslation[] getTranslations() {
		return translations;
	}
	
	public AbstractGtfsBuilder<T> getCustomTableBuilder(String tableName){
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private final AbstractGtfsBuilder<T> getTableBuilder(String tableName) {
		AbstractGtfsBuilder<T> spec = this.getCustomTableBuilder(tableName);
		if (spec != null) {
			return spec;
		}
		
		if (tableName.equals("agency")) {
			return (AbstractGtfsBuilder<T>) new GtfsAgencyBuilder();
		} else if (tableName.equals("calendar")) {
			return (AbstractGtfsBuilder<T>) new GtfsCalendarBuilder();
		} else if (tableName.equals("calendar_dates")) {
			return (AbstractGtfsBuilder<T>) new GtfsCalendarDatesBuilder();
		} else if (tableName.equals("fare_attributes")) {
			return (AbstractGtfsBuilder<T>) new GtfsFareAttributesBuilder();
		} else if (tableName.equals("fare_rules")) {
			return (AbstractGtfsBuilder<T>) new GtfsFareRulesBuilder();
		} else if (tableName.equals("frequencies")) {
			return (AbstractGtfsBuilder<T>) new GtfsFrequenciesBuilder();
		} else if (tableName.equals("routes")) {
			return (AbstractGtfsBuilder<T>) new GtfsRoutesBuilder();
		} else if (tableName.equals("stops")) {
			return (AbstractGtfsBuilder<T>) new GtfsStopsBuilder();
		} else if (tableName.equals("stop_times")) {
			return (AbstractGtfsBuilder<T>) new GtfsStopTimesBuilder();
		} else if (tableName.equals("trips")) {
			return (AbstractGtfsBuilder<T>) new GtfsTripsBuilder();
		} else {
			return null;
		}
	}

}
