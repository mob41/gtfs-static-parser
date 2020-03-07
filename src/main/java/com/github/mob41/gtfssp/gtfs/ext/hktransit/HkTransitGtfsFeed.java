package com.github.mob41.gtfssp.gtfs.ext.hktransit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.GtfsFeed;
import com.github.mob41.gtfssp.gtfs.GtfsLocalizedSource;
import com.github.mob41.gtfssp.gtfs.GtfsTableSource;
import com.github.mob41.gtfssp.gtfs.ext.gtw.GtwGtfsFeed;
import com.github.mob41.gtfssp.gtfs.row.GtfsAgency;
import com.github.mob41.gtfssp.gtfs.row.GtfsFeedInfo;

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
