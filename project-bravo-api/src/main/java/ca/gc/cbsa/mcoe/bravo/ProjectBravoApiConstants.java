package ca.gc.cbsa.mcoe.bravo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProjectBravoApiConstants {

	public final static Integer DIVISION_CODE_TRAVELLERS = 1;
	public final static Integer DIVISION_CODE_COMMERCIAL = 2;
	
	public final static String DATE_FORMAT_WITH_HOUR_MIN = "yyyy-MM-dd HH:mm";
	public final static String DATE_FORMAT_WITHOUT_HOUR_MIN = "yyyy-MM-dd";
	public final static String DATE_FORMAT_WITH_HOUR_MIN_SECS = "yyyy-MM-dd HH:mm:ss";
	public final static String TIME_DELIMITER_HOURLY = "hour";
	public final static String TIME_DELIMITER_DAILY = "day";
	public final static String TIME_DELIMITER_MONTHLY = "month";
	public final static String TIME_DELIMITER_ANNUAL = "year";
	
	public final static Integer MODE_COMMERCIAL_HWY = 1;
	public final static Integer MODE_COMMERCIAL_RAIL = 2;
	public final static Integer MODE_COMMERCIAL_MARINE = 3;
	public final static Integer MODE_COMMERCIAL_AIR = 4;
	public final static Integer MODE_COMMERCIAL_MULTI = 5;
	public final static Integer MODE_TRAVELLERS_HWY = 6;
	public final static Integer MODE_TRAVELLERS_RAIL = 7;
	public final static Integer MODE_TRAVELLERS_MARINE = 8;
	public final static Integer MODE_TRAVELLERS_AIR = 9;
	public final static Integer MODE_TRAVELLERS_MULTI = 10;

	public final static String PORT_PREFIX_BC_COMMERCIAL = "08";
	public final static String PORT_PREFIX_BC_TRAVELLERS = "2";
	
	public static final Map<String, String> PORT_PREFIX_PROV_MAP_COMMERCIAL;
	static {
        Map<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("NS", "00");
        tempMap.put("PE", "01");
        tempMap.put("NB", "02");
        tempMap.put("QC", "03");
        tempMap.put("ON", "04");
        tempMap.put("MB", "05");
        tempMap.put("SK", "06");
        tempMap.put("AB", "07");
        tempMap.put("BC", "08");
        tempMap.put("NL", "09");
        
        PORT_PREFIX_PROV_MAP_COMMERCIAL = Collections.unmodifiableMap(tempMap);
    }
	
	public static final Map<String, String> PORT_PREFIX_PROV_MAP_TRAVELLERS;
    static {
    	Map<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("PE", "1");
        tempMap.put("NB", "2");
        tempMap.put("QC", "3");
        tempMap.put("ON", "4");
        tempMap.put("MB", "5");
        tempMap.put("SK", "6");
        tempMap.put("AB", "70");
        tempMap.put("NS", "75");
        tempMap.put("BC", "8");
        tempMap.put("NL", "9");
        PORT_PREFIX_PROV_MAP_TRAVELLERS = Collections.unmodifiableMap(tempMap);
    }
    
}
