package com.github.mob41.gtfssp;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import com.github.mob41.gtfssp.gtfs.GtfsAgency;
import com.github.mob41.gtfssp.gtfs.GtfsCalendar;
import com.github.mob41.gtfssp.gtfs.GtfsRoute;
import com.github.mob41.gtfssp.gtfs.builders.GtfsAgencyBuilder;
import com.google.gson.Gson;

public class TestMain {

	public static void main(String[] args) throws Exception {
		trustAllSsl();
		
		URL url;
		URLConnection conn;
		
		
		GtfsAgencyBuilder b = new GtfsAgencyBuilder();

		url = new URL("https://static.data.gov.hk/td/pt-headway-en/agency.txt");
		conn = url.openConnection();
		b.setDefaultLocaleFromStream(conn.getInputStream());
		
		url = new URL("https://static.data.gov.hk/td/pt-headway-tc/agency.txt");
		conn = url.openConnection();
		b.setLocaleFromStream(new String[] { "zh", "zh-hk" }, conn.getInputStream());
		
		url = new URL("https://static.data.gov.hk/td/pt-headway-sc/agency.txt");
		conn = url.openConnection();
		b.setLocaleFromStream("zh-cn", conn.getInputStream());
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		b.rebuildLocalized(out);
		System.out.println(new String(out.toByteArray(), StandardCharsets.UTF_8));
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
