package ca.gc.cbsa.mcoe.bravo.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import ca.gc.cbsa.mcoe.bravo.controller.response.BorderStats;
import ca.gc.cbsa.mcoe.bravo.controller.response.Province;
import ca.gc.cbsa.mcoe.bravo.controller.response.ProvincialComparisonStats;
import ca.gc.cbsa.mcoe.bravo.controller.response.ProvincialStats;

public class StatsUtil {

	public static List<BorderStats> buildMockStats(int calendarUnit, int mode, String startDate, String endDate) throws ParseException {
		List<BorderStats> borderStatsList = new ArrayList<BorderStats>();
		
		Faker faker = new Faker();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date startDateObj = format.parse(startDate);
		Date endDateObj = format.parse(endDate); 
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDateObj);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDateObj);
		
		int divider = 1;
		
		if (calendarUnit == Calendar.HOUR) {
			divider = 8760;
		} else if (calendarUnit == Calendar.DAY_OF_MONTH) {
			divider = 365;
		} else if (calendarUnit == Calendar.MONTH) {
			divider = 12;
		}
		
		while (startCal.before(endCal)) {
			BorderStats borderStats = new BorderStats();
			borderStats.setTotal(Long.valueOf(faker.number().numberBetween(100000, 250000) / divider));
			if (mode >= 6) {
				borderStats.setAirSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				borderStats.setAirTotal(Long.valueOf(faker.number().numberBetween(25000, 75000) / divider));
				borderStats.setLandSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				borderStats.setLandTotal(Long.valueOf(faker.number().numberBetween(75000, 150000) / divider));
				borderStats.setTotalSecondary(Long.valueOf(faker.number().numberBetween(15000, 25000) / divider));
			}
			borderStats.setTimestamp(format.format(startCal.getTime()));
			borderStatsList.add(borderStats);
			startCal.add(calendarUnit, 1);
		}
		
		return borderStatsList;
		
	}
	
	public static ProvincialStats buildMockProvincialComparisonStats(int calendarUnit, int mode, String startDate, String endDate) throws ParseException, IOException {
		ProvincialStats provincialStats = new ProvincialStats();
		
		ObjectMapper mapper = new ObjectMapper();
		ClassPathResource provincesListResource = new ClassPathResource("provinces-list.json");
		String provincesListJSONString;
		try (InputStream provincesListInputStream = provincesListResource.getInputStream()) {
			provincesListJSONString = IOUtils.toString(provincesListInputStream, "UTF-8"); 
		}
		List<Province> provincesList = mapper.readValue(provincesListJSONString, new TypeReference<List<Province>>(){});
		
		Faker faker = new Faker();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date startDateObj = format.parse(startDate);
		Date endDateObj = format.parse(endDate); 
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDateObj);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDateObj);
		
		int divider = 1;
		
		if (calendarUnit == Calendar.HOUR) {
			divider = 8760;
		} else if (calendarUnit == Calendar.DAY_OF_MONTH) {
			divider = 365;
		} else if (calendarUnit == Calendar.MONTH) {
			divider = 12;
		}
		
		while (startCal.before(endCal)) {
			BorderStats borderStats = new BorderStats();
			
			borderStats.setTotal(Long.valueOf(faker.number().numberBetween(100000, 250000) / divider));
			if (mode >= 6) {
				borderStats.setAirSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				borderStats.setAirTotal(Long.valueOf(faker.number().numberBetween(25000, 75000) / divider));
				borderStats.setLandSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				borderStats.setLandTotal(Long.valueOf(faker.number().numberBetween(75000, 150000) / divider));
				borderStats.setTotalSecondary(Long.valueOf(faker.number().numberBetween(15000, 25000) / divider));
			}
			borderStats.setTimestamp(format.format(startCal.getTime()));
			provincialStats.getStats().add(borderStats);
			startCal.add(calendarUnit, 1);
		}
		
		for (Province province : provincesList) {
			ProvincialComparisonStats provincialComparisonStats = new ProvincialComparisonStats();
			provincialComparisonStats.setProvinceCode(province.getProvinceCode());
			provincialComparisonStats.setTotal((Long.valueOf(faker.number().numberBetween(100000, 250000) / divider)) * 25);
			provincialStats.getProvincialComparisonStats().add(provincialComparisonStats);
		}
		
		return provincialStats;
	}
}
