package ca.gc.cbsa.mcoe.bravo.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import ca.gc.cbsa.mcoe.bravo.controller.response.AnnualComparisonStats;
import ca.gc.cbsa.mcoe.bravo.controller.response.BorderStats;
import ca.gc.cbsa.mcoe.bravo.controller.response.BorderStatsCounts;
import ca.gc.cbsa.mcoe.bravo.controller.response.CommercialCount;
import ca.gc.cbsa.mcoe.bravo.controller.response.Province;
import ca.gc.cbsa.mcoe.bravo.controller.response.ProvincialComparisonStats;
import ca.gc.cbsa.mcoe.bravo.controller.response.TravellersCount;

public class StatsUtil {

	public static BorderStats buildMockStats(int calendarUnit, int mode, String startDate, String endDate) throws ParseException {
		BorderStats borderStats = new BorderStats();
		
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
			BorderStatsCounts stats = new BorderStatsCounts();
			stats.setTimestamp(format.format(startCal.getTime()));
						
			if (mode < 6) {
				CommercialCount commercialCount = new CommercialCount();
				commercialCount.setTotal(Long.valueOf(faker.number().numberBetween(100000, 250000) / divider));
				stats.setConveyances(commercialCount);
			} else {
				TravellersCount travellers = new TravellersCount();
				TravellersCount vehicles = new TravellersCount();
				travellers.setAirSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				travellers.setAirTotal(Long.valueOf(faker.number().numberBetween(25000, 75000) / divider));
				travellers.setLandSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				travellers.setLandTotal(Long.valueOf(faker.number().numberBetween(75000, 150000) / divider));
				travellers.setTotalSecondary(Long.valueOf(faker.number().numberBetween(15000, 25000) / divider));
				vehicles.setAirSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				vehicles.setAirTotal(Long.valueOf(faker.number().numberBetween(25000, 75000) / divider));
				vehicles.setLandSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				vehicles.setLandTotal(Long.valueOf(faker.number().numberBetween(75000, 150000) / divider));
				vehicles.setTotalSecondary(Long.valueOf(faker.number().numberBetween(15000, 25000) / divider));
				stats.setTravellers(travellers);
				stats.setVehicles(vehicles);
			}
			
			borderStats.getStats().add(stats);
			startCal.add(calendarUnit, 1);
		}
		
		AnnualComparisonStats annualComparisonStats = new AnnualComparisonStats();
		if (mode < 6) {
			for (int i=0; i < 3; i++) {
				annualComparisonStats.getConveyances().add(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
			}
		} else {
			for (int i=0; i < 3; i++) {
				annualComparisonStats.getTravellers().add(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				annualComparisonStats.getVehicles().add(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
			}
		}
		borderStats.setAnnualComparisonStats(annualComparisonStats);
		
		return borderStats;
		
	}
	
	public static BorderStats buildMockProvincialStats(int calendarUnit, int mode, String startDate, String endDate) throws ParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		ClassPathResource provincesListResource = new ClassPathResource("provinces-list.json");
		
		String provincesListJSONString;
		
		try (InputStream provincesListInputStream = provincesListResource.getInputStream()) {
			provincesListJSONString = IOUtils.toString(provincesListInputStream, "UTF-8"); 
		}
		
		List<Province> provincesList = mapper.readValue(provincesListJSONString, new TypeReference<List<Province>>(){});
		BorderStats borderStats = new BorderStats();
		
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
			BorderStatsCounts stats = new BorderStatsCounts();
			stats.setTimestamp(format.format(startCal.getTime()));
						
			if (mode < 6) {
				CommercialCount commercialCount = new CommercialCount();
				commercialCount.setTotal(Long.valueOf(faker.number().numberBetween(100000, 250000) / divider));
				stats.setConveyances(commercialCount);
			} else {
				TravellersCount travellers = new TravellersCount();
				TravellersCount vehicles = new TravellersCount();
				travellers.setAirSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				travellers.setAirTotal(Long.valueOf(faker.number().numberBetween(25000, 75000) / divider));
				travellers.setLandSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				travellers.setLandTotal(Long.valueOf(faker.number().numberBetween(75000, 150000) / divider));
				travellers.setTotalSecondary(Long.valueOf(faker.number().numberBetween(15000, 25000) / divider));
				vehicles.setAirSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				vehicles.setAirTotal(Long.valueOf(faker.number().numberBetween(25000, 75000) / divider));
				vehicles.setLandSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				vehicles.setLandTotal(Long.valueOf(faker.number().numberBetween(75000, 150000) / divider));
				vehicles.setTotalSecondary(Long.valueOf(faker.number().numberBetween(15000, 25000) / divider));
				stats.setTravellers(travellers);
				stats.setVehicles(vehicles);
			}
			
			borderStats.getStats().add(stats);
			startCal.add(calendarUnit, 1);
		}
		
		AnnualComparisonStats annualComparisonStats = new AnnualComparisonStats();
		if (mode < 6) {
			for (int i=0; i < 3; i++) {
				annualComparisonStats.getConveyances().add(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
			}
		} else {
			for (int i=0; i < 3; i++) {
				annualComparisonStats.getTravellers().add(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				annualComparisonStats.getVehicles().add(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
			}
		}
		borderStats.setAnnualComparisonStats(annualComparisonStats);
		
		for (Province province : provincesList) {
			ProvincialComparisonStats provincialComparisonStats = new ProvincialComparisonStats();
			provincialComparisonStats.setProvinceCode(province.getProvinceCode());
			provincialComparisonStats.setTotal((Long.valueOf(faker.number().numberBetween(100000, 250000) / divider)) * 25);
			borderStats.getProvincialComparisonStats().add(provincialComparisonStats);
		}
		
		return borderStats;
	}
}
