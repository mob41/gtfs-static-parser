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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.mob41.gtfssp.gtfs.GtfsData;
import com.github.mob41.gtfssp.gtfs.row.GtfsTranslation;

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
	public T[] setDefaultLocaleFromStream(InputStream in) throws IOException {
		return setDefaultLocaleFromStream(in, true);
	}
	
	/***
	 * Sets locale for localized GtfsData from stream
	 * @param locale Lower-case language code in standards ISO 639-1 and ISO 639-2 (e.g. en, zh, zh-hk, zh-cn)
	 * @param in InputStream with GTFS static data
	 * @param skipHeader Skip the first row containing header names
	 * @throws IOException I/O Errors
	 */
	public T[] setDefaultLocaleFromStream(InputStream in, boolean skipHeader) throws IOException {
		T[] out;
		if ((out = fromStream(in, skipHeader)).length > 0 && maps.size() > 0) {
			setDefaultLocale(maps);
		}
		return out;
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
			setLocale(locales, maps);
		}
	}
	
	public void setDefaultLocale(List<Map<String, String>> maps) {
		defaultLocaleMaps = maps;
		localizedMaps.put(defaultLocale, maps);
	}
	
	public void setLocale(String locale, List<Map<String, String>> maps) {
		setLocale(new String[] { locale }, maps);
	}
	
	public void setLocale(String[] locales, List<Map<String, String>> maps) {
		for (String locale : locales) {
			localizedMaps.put(locale, maps);
		}
	}
	
	/***
	 * Finds the deltas in each localized GTFS data and combines them into an array of <code>GtfsTranslation</code> instances.
	 * If default locale maps are not built, an <code>IllegalStateException</code> will be thrown.
	 * @return <code>GtfsTranslations</code> array
	 */
	public GtfsTranslation[] getTranslations() {
		if (defaultLocaleMaps == null) {
			//return null;
			throw new IllegalStateException("Default localized maps are not built and set.");
		}
		
		Set<String> deltas = new HashSet<String>();
		
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
						if (!deltas.contains(mapKey)) {
							deltas.add(mapKey);
						}
					}
				}
			}
		}

		//List<Map<String, String>> out = new ArrayList<Map<String, String>>();
		//Map<String, String> valMap;
		List<GtfsTranslation> outList = new ArrayList<GtfsTranslation>();
		List<Map<String, String>> list;
		Map<String, String> map;
		Map<String, String> newMap;
		for (i = 0; i < defaultLocaleMaps.size(); i++) {
			//valMap = new HashMap<String, String>();
			//valMap.putAll(defaultLocaleMaps.get(i));
			
			for (String deltaKey : deltas) {
				localeIt = localizedMaps.keySet().iterator();
				while (localeIt.hasNext()) {
					localeKey = localeIt.next();
					list = localizedMaps.get(localeKey);
					map = list.get(i);
					
					newMap = new HashMap<String, String>();
					newMap.put("table_name", dataType);
					newMap.put("field_name", deltaKey);
					newMap.put("language", localeKey);
					newMap.put("translation", map.get(deltaKey));
					outList.add(new GtfsTranslation(newMap));
				}
			}
		}
		
		GtfsTranslation[] out = new GtfsTranslation[outList.size()];
		for (i = 0; i < out.length; i++) {
			out[i] = outList.get(i);
		}
		return out;
	}
	
	public int calculatePages(int rowLimit) {
		return (int) Math.ceil(maps.size() / (float) rowLimit);
	}
	
	public String[] getHeaders(){
		return headers;
	}
	
	public abstract String getHeaderType(String header);
	
	public String[] getHeaderTypes() {
		String[] headers = getHeaders();
		String[] headerTypes = new String[headers.length];
		for (int i = 0; i < headerTypes.length; i++) {
			headerTypes[i] = getHeaderType(headers[i]);
		}
		return headerTypes;
	}
	
	public void setMaps(List<Map<String, String>> maps) {
		this.maps = maps;
	}
	
	public List<Map<String, String>> getMaps(){
		return maps;
	}
	
	/***
	 * Rebuilds the previously built GtfsData into a CSV-formatted stream
	 * @param maps GtfsData HashMaps
	 */
	public void rebuild(OutputStream out) throws IOException {
		rebuild(maps, out);
	}
	
	public void rebuild(List<Map<String, String>> maps, OutputStream out) throws IOException {
		rebuild(maps, out, true, 0, maps.size());
	}
	
	public void rebuildWithPaging(List<Map<String, String>> maps, OutputStream out, int rowLimit, int pageIndex) throws IOException {
		rebuildWithPaging(maps, out, true, rowLimit, pageIndex);
	}
	
	public void rebuildWithPaging(List<Map<String, String>> maps, OutputStream out, boolean putHeaders, int rowLimit, int pageIndex) throws IOException {
		int pages = calculatePages(rowLimit);
		if (pageIndex < 0 || pageIndex >= pages) {
			throw new IOException("Invalid page index: " + pageIndex);
		}
		int nextStart = (pageIndex + 1) * rowLimit;
		int end = nextStart >= maps.size() ? maps.size() : nextStart;
		rebuild(maps, out, putHeaders, rowLimit * pageIndex, end);
	}
	
	/***
	 * Rebuilds the provided HashMap data into a CSV-formatted stream
	 * @param maps GtfsData HashMaps
	 * @param putHeaders Put headers to the first row
	 * @param start Start index
	 * @param end End index
	 */
	public void rebuild(List<Map<String, String>> maps, OutputStream out, boolean putHeaders, int start, int end) throws IOException{
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
		
		if (putHeaders) {
			for (int i = 0; i < headers.size(); i++) {
				val = headers.get(i);
				row += val;
				if (i != headers.size() - 1) {
					row += ",";
				}
			}
			rows.add(row);
		}
		
		for (int i = start; i < end; i++) {
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
