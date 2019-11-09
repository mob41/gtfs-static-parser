package com.github.mob41.gtfssp.gtfs.builders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.GtfsData;

public abstract class AbstractGtfsBuilder<T> {
	
	private static final String DEFAULT_LOCALE = "en";
	
	private final String dataType;
	
	private String[] headers;
	
	private List<GtfsData> parsedList;
	
	private List<Map<String, String>> maps;
	
	private List<Map<String, String>> defaultLocaleMaps;
	
	private Map<String, List<Map<String, String>>> localizedMaps;

	private String defaultLocale;
	
	public AbstractGtfsBuilder(String dataType) {
		this.dataType = dataType;
		defaultLocaleMaps = null;
		localizedMaps = new HashMap<String, List<Map<String, String>>>();
		defaultLocale = DEFAULT_LOCALE;
	}
	
	/***
	 * Sets the default locale for generating localized maps. Default is <code>en</code>.
	 * @param defaultLocale Lower-case default language code in standards ISO 639-1 and ISO 639-2 (e.g. en, zh, zh-hk, zh-cn)
	 */
	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}
	
	/***
	 * Gets the current default locale for generating localized maps. Default is <code>en</code>.
	 * @return Lower-case default language code in standards ISO 639-1 and ISO 639-2 (e.g. en, zh, zh-hk, zh-cn)
	 */
	public String getDefaultLocale() {
		return defaultLocale;
	}
	
	/***
	 * Returns the data type/originated file without extension (e.g. agency, routes, stops)
	 * @return Data type in String
	 */
	public String getDataType() {
		return dataType;
	}
	
	/***
	 * Parses provided Map into a GtfsData.
	 * @param map Map to be parsed
	 * @return GtfsData instance
	 */
	public abstract GtfsData parseMap(Map<String, String> map);
	
	/***
	 * Clears existing localized maps.
	 */
	public void clearLocalizedMaps() {
		defaultLocaleMaps = null;
		localizedMaps.clear();
	}

	/***
	 * Sets default locale for localized GtfsData from stream
	 * @param locale Lower-case language code in standards ISO 639-1 and ISO 639-2 (e.g. en, zh, zh-hk, zh-cn)
	 * @param in InputStream with GTFS static data
	 * @throws IOException I/O Errors
	 */
	public void setDefaultLocaleFromStream(InputStream in) throws IOException {
		setDefaultLocaleFromStream(in, true);
	}
	
	/***
	 * Sets locale for localized GtfsData from stream
	 * @param locale Lower-case language code in standards ISO 639-1 and ISO 639-2 (e.g. en, zh, zh-hk, zh-cn)
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @throws IOException I/O Errors
	 */
	public void setDefaultLocaleFromStream(InputStream in, boolean skipHeader) throws IOException {
		if (fromStream(in, skipHeader).length > 0 && maps.size() > 0) {
			defaultLocaleMaps = maps;
			localizedMaps.put(defaultLocale, maps);
		}
	}
	
	/***
	 * Sets locale for localized GtfsData from stream
	 * @param locale Lower-case language code in standards ISO 639-1 and ISO 639-2 (e.g. en, zh, zh-hk, zh-cn)
	 * @param in InputStream with GTFS static data
	 * @throws IOException I/O Errors
	 */
	public void setLocaleFromStream(String locale, InputStream in) throws IOException {
		setLocaleFromStream(locale, in, true);
	}
	
	/***
	 * Sets locale for localized GtfsData from stream
	 * @param locales Lower-case language codes in standards ISO 639-1 and ISO 639-2 (e.g. en, zh, zh-hk, zh-cn)
	 * @param in InputStream with GTFS static data
	 * @throws IOException I/O Errors
	 */
	public void setLocaleFromStream(String[] locales, InputStream in) throws IOException {
		setLocaleFromStream(locales, in, true);
	}
	
	/***
	 * Sets locale for localized GtfsData from stream
	 * @param locale Lower-case language code in standards ISO 639-1 and ISO 639-2 (e.g. en, zh, zh-hk, zh-cn)
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @throws IOException I/O Errors
	 */
	public void setLocaleFromStream(String locale, InputStream in, boolean skipHeader) throws IOException {
		setLocaleFromStream(new String[] { locale }, in, skipHeader);
	}
	
	/***
	 * Sets locale for localized GtfsData from stream
	 * @param locales Lower-case language codes in standards ISO 639-1 and ISO 639-2 (e.g. en, zh, zh-hk, zh-cn)
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @throws IOException I/O Errors
	 */
	public void setLocaleFromStream(String[] locales, InputStream in, boolean skipHeader) throws IOException {
		if (fromStream(in, skipHeader).length > 0 && maps.size() > 0) {
			for (String locale : locales) {
				localizedMaps.put(locale, maps);
			}
		}
	}
	
	/***
	 * Finds the deltas in each localized GTFS data and combines them into one <code>HashMap</code> array.
	 * If default locale maps are not built, previously built maps will be returned instead.
	 * @return HashMap array
	 */
	public List<Map<String, String>> getLocalizedMaps() {
		if (defaultLocaleMaps == null) {
			return maps;
			//throw new IllegalStateException("Default localized maps are not built and set.");
		}
		
		List<String> deltas = new ArrayList<String>();
		
		String localeKey;
		String mapKey;
		Map<String, String> defMap;
		Map<String, String> forMap;
		List<Map<String, String>> current;
		Iterator<String> localeIt = localizedMaps.keySet().iterator();
		Iterator<String> objIt;
		int i;
		while (localeIt.hasNext()) {
			localeKey = localeIt.next();
			current = localizedMaps.get(localeKey);
			for (i = 0; i < defaultLocaleMaps.size(); i++) {
				defMap = defaultLocaleMaps.get(i);
				forMap = current.get(i);
				objIt = defMap.keySet().iterator();
				while (objIt.hasNext()) {
					mapKey = objIt.next();
					if (!defMap.get(mapKey).equals(forMap.get(mapKey))) {
						deltas.add(mapKey);
					}
				}
			}
		}

		List<Map<String, String>> out = new ArrayList<Map<String, String>>();
		Map<String, String> valMap;
		
		for (i = 0; i < defaultLocaleMaps.size(); i++) {
			valMap = new HashMap<String, String>();
			valMap.putAll(defaultLocaleMaps.get(i));
			
			for (String deltaKey : deltas) {
				localeIt = localizedMaps.keySet().iterator();
				while (localeIt.hasNext()) {
					localeKey = localeIt.next();
					valMap.put(deltaKey + "_" + localeKey, localizedMaps.get(localeKey).get(i).get(deltaKey));
				}
			}
			
			out.add(valMap);
		}
		return out;
	}
	
	/***
	 * Rebuilds the previously built GtfsData into a CSV-formatted stream
	 * @param maps GtfsData HashMaps
	 */
	public void rebuild(OutputStream out) throws IOException {
		rebuild(maps, out);
	}
	
	/***
	 * Immediately build localized GtfsData from previously built GtfsData and rebuilds it into a CSV-formatted stream
	 * @param maps GtfsData HashMaps
	 */
	public void rebuildLocalized(OutputStream out) throws IOException {
		List<Map<String, String>> lm = getLocalizedMaps();
		rebuild(lm, out);
	}
	
	/***
	 * Rebuilds the provided HashMap data into a CSV-formatted stream
	 * @param maps GtfsData HashMaps
	 */
	public void rebuild(List<Map<String, String>> maps, OutputStream out) throws IOException{
		List<String> headers = new ArrayList<String>();
		
		Iterator<String> it;
		String key;
		for (Map<String, String> map : maps) {
			it = map.keySet().iterator();
			while (it.hasNext()) {
				key = it.next();
				if (!headers.contains(key)) {
					headers.add(key);
				}
			}
		}
		Collections.sort(headers);
		
		List<String> rows = new ArrayList<String>();
		
		String val;
		String row = "";
		for (int i = 0; i < headers.size(); i++) {
			val = headers.get(i);
			row += val;
			if (i != headers.size() - 1) {
				row += ",";
			}
		}
		rows.add(row);
		
		for (int i = 0; i < maps.size(); i++) {
			row = "";
			for (int j = 0; j < headers.size(); j++) {
				val = maps.get(i).get(headers.get(j));
				row += val;
				if (j != headers.size() - 1) {
					row += ",";
				}
			}
			rows.add(row);
		}
		
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), true);
		for (String str : rows) {
			writer.println(str);
		}
		//writer.flush();
		//writer.close();
	}

	/***
	 * Get an array of GTFS-static-formatted data from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @return An array of data
	 * @throws IOException I/O Errors
	 */
	public T[] fromStream(InputStream in) throws IOException {
		return fromStream(in, true);
	}

	/***
	 * Get an array of GTFS-static-formatted data from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @return An array of data
	 * @throws IOException I/O Errors
	 */
	public abstract T[] fromStream(InputStream in, boolean skipHeader) throws IOException;

	/***
	 * Get an array of GTFS-static-formatted data from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @return An array of data
	 * @throws IOException I/O Errors
	 */
	public GtfsData[] dataFromStream(InputStream in) throws IOException {
		return dataFromStream(in, true);
	}
	
	/***
	 * Get an array of GTFS-static-formatted data from an InputStream
	 * 
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @return An array of data
	 * @throws IOException I/O Errors
	 */
	public GtfsData[] dataFromStream(InputStream in, boolean skipHeader) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		
		String headerStr = reader.readLine();
		
		if (headerStr == null) {
			throw new IOException("EOL could not read CSV headers.");
		}
		
		headers = csvSplit(headerStr);
		
		maps = new ArrayList<Map<String, String>>();
		parsedList = new ArrayList<GtfsData>();
		String[] splits;
		String line;
		Map<String, String> map;
		while ((line = reader.readLine()) != null) {
			splits = csvSplit(line);
			if (splits.length < headers.length) {
			    throw new IOException("Invalid " + dataType + " structure with column length less than headers' length " + headers.length + ": " + splits.length);
			}
			
			map = new HashMap<String, String>();
			for (int i = 0; i < headers.length; i++) {
				map.put(headers[i], splits[i]);
			}
			
			maps.add(map);
			parsedList.add(parseMap(map));
		}
		
		reader.close();
		
		GtfsData[] out = new GtfsData[parsedList.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = parsedList.get(i);
		}
		return out;
	}
	
	public static String[] csvSplit(String row) {
		return row.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	}

}
