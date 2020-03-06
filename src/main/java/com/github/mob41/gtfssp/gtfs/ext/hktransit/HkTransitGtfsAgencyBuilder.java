package com.github.mob41.gtfssp.gtfs.ext.hktransit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.mob41.gtfssp.gtfs.ext.gtw.Console;
import com.github.mob41.gtfssp.gtfs.ext.gtw.GtwGtfsAgency;
import com.github.mob41.gtfssp.gtfs.ext.gtw.GtwGtfsAgencyBuilder;
import com.github.mob41.gtfssp.gtfs.row.GtfsAgency;

public class HkTransitGtfsAgencyBuilder extends GtwGtfsAgencyBuilder {
	
	public static boolean CONSOLE_LOGGING = true;
	
	@Override
	protected boolean isLocaleManaged(String locale) {
		return locale.equals("en") ||
				locale.equals("zh-HK") ||
				locale.equals("zh-CN");
	}
	
	@Override
	protected GtfsAgency[] handle(String locale, GtfsAgency[] objs) {
		return toInstances(modifyAgencies(locale, toList(objs)));
	}
	
	@Override
	protected List<Map<String, String>> handle(String locale, List<Map<String, String>> objs) {
		return modifyAgencies(locale, objs);
	}
	
	private static GtwGtfsAgency[] toInstances(List<Map<String, String>> list) {
		GtwGtfsAgency[] out = new GtwGtfsAgency[list.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = new GtwGtfsAgency(list.get(i));
		}
		return out;
	}
	
	private static List<Map<String, String>> toList(GtfsAgency[] objs) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < objs.length; i++) {
			list.add(objs[i].getStringMap());
		}
		return list;
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
	
	private List<Map<String, String>> modifyAgencies(String localeValue, List<Map<String, String>> maps) {
		List<Map<String, String>> out = new ArrayList<Map<String, String>>();
		
		String agencyId;
		Map<String, String> map;
		for (int i = 0; i < maps.size(); i++) {
			map = maps.get(i);
			
			agencyId = map.get("agency_id");
			if (agencyId.equals("CTB")) {
				if (localeValue.equals("en")) {
					map.put("agency_short_name", "CTB");
					map.put("agency_long_name", "Citybus");
				} else if (localeValue.equals("zh-HK")) {
					map.put("agency_short_name", "\u57CE\u5DF4");
					map.put("agency_long_name", "\u57CE\u5DF4");
				} else if (localeValue.equals("zh-CN")) {
					map.put("agency_short_name", "\u57CE\u5DF4");
					map.put("agency_long_name", "\u57CE\u5DF4");
				}
			} else if (agencyId.equals("KMB")) {
				if (localeValue.equals("en")) {
					map.put("agency_short_name", "KMB");
					map.put("agency_long_name", "Kowloon Motor Bus");
				} else if (localeValue.equals("zh-HK")) {
					map.put("agency_short_name", "\u4E5D\u5DF4");
					map.put("agency_long_name", "\u4E5D\u9F8D\u5DF4\u58EB");
				} else if (localeValue.equals("zh-CN")) {
					map.put("agency_short_name", "\u4E5D\u5DF4");
					map.put("agency_long_name", "\u4E5D\u9F99\u5DF4\u58EB");
				}
			} else if (agencyId.equals("KMB+CTB")) {
				if (localeValue.equals("en")) {
					map.put("agency_short_name", "KMB/CTB");
					map.put("agency_long_name", "Kowloon Motor Bus/Citybus");
				} else if (localeValue.equals("zh-HK")) {
					map.put("agency_short_name", "\u4E5D\u5DF4/\u57CE\u5DF4");
					map.put("agency_long_name", "\u4E5D\u9F8D\u5DF4\u58EB/\u57CE\u5DF4");
				} else if (localeValue.equals("zh-CN")) {
					map.put("agency_short_name", "\u4E5D\u5DF4/\u57CE\u5DF4");
					map.put("agency_long_name", "\u4E5D\u9F99\u5DF4\u58EB/\u57CE\u5DF4");
				}
			} else if (agencyId.equals("KMB+NWFB")) {
				if (localeValue.equals("en")) {
					map.put("agency_short_name", "KMB/NWFB");
					map.put("agency_long_name", "Kowloon Motor Bus/New World First Bus");
				} else if (localeValue.equals("zh-HK")) {
					map.put("agency_short_name", "\u4E5D\u5DF4/\u65B0\u5DF4");
					map.put("agency_long_name", "\u4E5D\u9F8D\u5DF4\u58EB/\u65B0\u4E16\u754C\u7B2C\u4E00\u5DF4\u58EB");
				} else if (localeValue.equals("zh-CN")) {
					map.put("agency_short_name", "\u4E5D\u5DF4/\u65B0\u5DF4");
					map.put("agency_long_name", "\u4E5D\u9F99\u5DF4\u58EB/\u65B0\u4E16\u754C\u7B2C\u4E00\u5DF4\u58EB");
				}
			} else if (agencyId.equals("LWB+CTB")) {
				if (localeValue.equals("en")) {
					map.put("agency_short_name", "LWB/CTB");
					map.put("agency_long_name", "Long Win Bus/Citybus");
				} else if (localeValue.equals("zh-HK")) {
					map.put("agency_short_name", "\u9F8D\u904B/\u57CE\u5DF4");
					map.put("agency_long_name", "\u9F8D\u904B\u5DF4\u58EB/\u57CE\u5DF4");
				} else if (localeValue.equals("zh-CN")) {
					map.put("agency_short_name", "\u9F99\u8FD0/\u57CE\u5DF4");
					map.put("agency_long_name", "\u9F99\u8FD0\u5DF4\u58EB/\u57CE\u5DF4");
				}
			} else if (agencyId.equals("NWFB")) {
				if (localeValue.equals("en")) {
					map.put("agency_short_name", "NWFB");
					map.put("agency_long_name", "New World First Bus");
				} else if (localeValue.equals("zh-HK")) {
					map.put("agency_short_name", "\u65B0\u5DF4");
					map.put("agency_long_name", "\u65B0\u4E16\u754C\u7B2C\u4E00\u5DF4\u58EB");
				} else if (localeValue.equals("zh-CN")) {
					map.put("agency_short_name", "\u65B0\u5DF4");
					map.put("agency_long_name", "\u65B0\u4E16\u754C\u7B2C\u4E00\u5DF4\u58EB");
				}
			} else if (agencyId.equals("PI")) {
				if (localeValue.equals("en")) {
					map.put("agency_short_name", "Park Island Bus");
					map.put("agency_long_name", "Park Island Transport (Bus Services)");
				} else if (localeValue.equals("zh-HK")) {
					map.put("agency_short_name", "\u73C0\u9E97\u7063\u5DF4\u58EB");
					map.put("agency_long_name", "\u73C0\u9E97\u7063\u5BA2\u904B\uFF08\u5DF4\u58EB\u670D\u52D9\uFF09");
				} else if (localeValue.equals("zh-CN")) {
					map.put("agency_short_name", "\u73C0\u4E3D\u6E7E\u5DF4\u58EB");
					map.put("agency_long_name", "\u73C0\u4E3D\u6E7E\u5BA2\u8FD0\uFF08\u5DF4\u58EB\u670D\u52A1\uFF09");
				}
			} else if (agencyId.equals("LWB")) {
				if (localeValue.equals("en")) {
					map.put("agency_short_name", "LWB");
					map.put("agency_long_name", "Long Win Bus");
				} else if (localeValue.equals("zh-HK")) {
					map.put("agency_short_name", "\u9F8D\u904B");
					map.put("agency_long_name", "\u9F8D\u904B\u5DF4\u58EB");
				} else if (localeValue.equals("zh-CN")) {
					map.put("agency_short_name", "\u9F99\u8FD0");
					map.put("agency_long_name", "\u9F99\u8FD0\u5DF4\u58EB");
				}
			} else if (agencyId.equals("DB")) {
				if (localeValue.equals("en")) {
					map.put("agency_short_name", "Discovery Bay Bus");
					map.put("agency_long_name", "Discovery Bay Transit Services (Bus Services)");
				} else if (localeValue.equals("zh-HK")) {
					map.put("agency_short_name", "\u6109\u666F\u7063\u5DF4\u58EB");
					map.put("agency_long_name", "\u6109\u666F\u7063\u4EA4\u901A\u670D\u52D9\uFF08\u5DF4\u58EB\u670D\u52D9\uFF09");
				} else if (localeValue.equals("zh-CN")) {
					map.put("agency_short_name", "\u6109\u666F\u6E7E\u5DF4\u58EB");
					map.put("agency_long_name", "\u6109\u666F\u6E7E\\u4EA4\u901A\u670D\u52A1\uFF08\u5DF4\u58EB\u670D\u52A1\uFF09");
				}
			} else if (agencyId.equals("XB")) {
				String name = map.get("agency_name");
				if (localeValue.equals("en")) {
					map.put("agency_short_name", "Lok Ma Chau Cross-boundary");
					map.put("agency_long_name", name);
				} else if (localeValue.equals("zh-HK")) {
					map.put("agency_short_name", "\u843D\u99AC\u6D32\u904E\u5883\u5DF4\u58EB");
					map.put("agency_long_name", name);
				} else if (localeValue.equals("zh-CN")) {
					map.put("agency_short_name", "\u843D\u9A6C\u6D32\u8FC7\u5883\u5DF4\u58EB");
					map.put("agency_long_name", name);
				}
			} else if (agencyId.equals("GMB")) {
				String name = map.get("agency_name");
				if (localeValue.equals("en")) {
					map.put("agency_short_name", "Green Minibus");
					map.put("agency_long_name", name);
				} else if (localeValue.equals("zh-HK")) {
					map.put("agency_short_name", "\u5C08\u7DDA\u5C0F\u5DF4");
					map.put("agency_long_name", name);
				} else if (localeValue.equals("zh-CN")) {
					map.put("agency_short_name", "\u4E13\u7EBF\u5C0F\u5DF4");
					map.put("agency_long_name", name);
				}
			} else if (agencyId.equals("FERRY")) {
				String name = map.get("agency_name");
				if (localeValue.equals("en")) {
					map.put("agency_short_name", "Ferry");
					map.put("agency_long_name", name);
				} else if (localeValue.equals("zh-HK")) {
					map.put("agency_short_name", "\u6E21\u8F2A");
					map.put("agency_long_name", name);
				} else if (localeValue.equals("zh-CN")) {
					map.put("agency_short_name", "\u6E21\u8F6E");
					map.put("agency_long_name", name);
				}
			} else {
				String name = map.get("agency_name");
				map.put("agency_short_name", name);
				map.put("agency_long_name", name);
			}
			
			out.add(map);
			int calc = (int) Math.floor((i + 1) / (float) maps.size() * 100);
			reportMessage("Modified Agencies " + calc + "%: " + (i + 1) + "/" + maps.size());
		}
		finishLine();
		
		return out;
	}

}
