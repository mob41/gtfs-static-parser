package com.github.mob41.gtfssp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.GtfsFeed;
import com.github.mob41.gtfssp.gtfs.GtfsLocalizedSource;
import com.github.mob41.gtfssp.gtfs.GtfsTableSource;
import com.github.mob41.gtfssp.gtfs.builders.GtfsStopTimesBuilder;
import com.github.mob41.gtfssp.gtfs.ext.gtw.GtwGtfsFeed;
import com.github.mob41.gtfssp.gtfs.ext.hktransit.HkTransitGtfsFeed;
import com.github.mob41.gtfssp.gtfs.row.GtfsFeedInfo;
import com.github.mob41.gtfssp.gtfs.row.GtfsRoute;
import com.github.mob41.gtfssp.gtfs.row.GtfsStop;
import com.github.mob41.gtfssp.gtfs.row.GtfsStopTime;
import com.github.mob41.gtfssp.gtfs.row.GtfsTranslation;

public class GroupStopTimesMain {

	public static void main(String[] args) throws Exception {
		System.out.println("Trusting ssl");
		//GtwGtfsFeed.CONSOLE_LOGGING = true;
		trustAllSsl();
		System.out.println("preapring sources");
		GtfsFeed feed = new HkTransitGtfsFeed();
		System.out.println("Fething feed");
		Map<String, GtfsData[]> map = feed.fetchFeed();
		Iterator<String> it = map.keySet().iterator();
		String key;
		GtfsData[] arr;
		System.out.println("Printing out");
		while (it.hasNext()) {
			key = it.next();
			System.out.println("KEY: " + key);
			arr = map.get(key);
			System.out.println("len: " + arr.length);
		}
		System.out.println("Translations");
		GtfsTranslation[] trans = feed.getTranslations();
		System.out.println("len: " + trans.length);
		
		System.out.println("Making zip");
		GtfsFeed.makeZip("test-" + System.currentTimeMillis() + ".zip", map);
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
