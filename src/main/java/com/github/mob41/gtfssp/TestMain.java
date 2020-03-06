package com.github.mob41.gtfssp;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import com.github.mob41.gtfssp.gtfs.builders.GtfsAgencyBuilder;
import com.github.mob41.gtfssp.gtfs.builders.GtfsStopTimesBuilder;
import com.github.mob41.gtfssp.gtfs.row.GtfsAgency;
import com.github.mob41.gtfssp.gtfs.row.GtfsCalendar;
import com.github.mob41.gtfssp.gtfs.row.GtfsRoute;
import com.github.mob41.gtfssp.gtfs.row.GtfsStopTime;
import com.google.gson.Gson;

public class TestMain {

	public static void main(String[] args) throws Exception {
		trustAllSsl();
		
		System.out.println("Downloading stop_times...");
		GtfsStopTime[] stopTimes = new GtfsStopTimesBuilder().fromStream(new URL("https://static.data.gov.hk/td/pt-headway-tc/stop_times.txt").openConnection().getInputStream());
		System.out.println("Downloaded " + stopTimes.length + " rows");
		
		System.out.println("Pairing stop times with same trip IDs...");
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
		System.out.println("Done");

		System.out.println("Sorting trip stop times...");
		int count = 0;
		Iterator<String> sortIt = byTripId.keySet().iterator();
		while (sortIt.hasNext()) {
			count++;
			Collections.sort(byTripId.get(sortIt.next()), new Comparator<GtfsStopTime>() {
				
				@Override
				public int compare(GtfsStopTime o1, GtfsStopTime o2) {
					return o1.stop_sequence - o2.stop_sequence;
				}
				
			});
		}
		System.out.println("Done");

		long start = System.currentTimeMillis();
		System.out.println("Grouping trips with same path...");
		int groupCount = 0;
		
		List<List<GtfsStopTime>> groups = new ArrayList<List<GtfsStopTime>>();
		List<List<String>> groupsAssocIds = new ArrayList<List<String>>();
		
		Set<String> groupedTripIds = new HashSet<String>();
		
		List<GtfsStopTime> group;
		List<String> groupIds;
		Iterator<String> outIt = byTripId.keySet().iterator();
		int done = 0;
		int last = -1;
		while (outIt.hasNext()) {
			String outKey = outIt.next();

			if (groupedTripIds.contains(outKey)) {
				continue;
			}
			
			groupIds = new ArrayList<String>();
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
				System.out.println("Completed " + calc + "%. Grouped " + done + "/" + count + " rows into " + groups.size() + " trips.");
			}
		}
		
		System.out.println("Done used " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("Grouped " + count + " into " + groups.size() + " groups.");
		
		System.out.println("Evaluating how many rows will be created...");
		int rows = 0;
		for (int i = 0; i < groups.size(); i++) {
			rows += groups.get(i).size();
		}
		System.out.println(rows + " grouped trip stop times will be created. " + rows + "/" + stopTimes.length + " (" + ((int) Math.floor(rows / (float) stopTimes.length * 100)) + "%)");
	}
	
	private static boolean findKey(List<String> list, String key) {
		Collections.sort(list);
		int mid;
		int start = 0;
		int end = list.size() - 1;
		String midVal;
		int compare;
		
		while (start <= end) {
			mid = (int) Math.floor((start + end) / 2.0);
			midVal = list.get(mid);
			compare = key.compareTo(midVal);
			if (compare == 0) {
				return true;
			} else if (compare > 0) {
				start = mid + 1;
			} else {
				end = mid - 1;
			}
		}
		return false;
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
	
	private static void trustAllSsl() {
		try {

			/* Start of Fix */
	        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
	            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
	            public void checkServerTrusted(X509Certificate[] certs, String authType) { }
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {}
				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {}

	        } };

	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) { return true; }
	        };
	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	        /* End of the fix*/
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: Trust All SSL Failed!");
		}
	}

}
