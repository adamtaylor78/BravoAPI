package ca.gc.cbsa.mcoe.bravo.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import ca.gc.cbsa.mcoe.bravo.ProjectBravoApiConstants;
import ca.gc.cbsa.mcoe.bravo.controller.response.AnnualComparisonStats;
import ca.gc.cbsa.mcoe.bravo.controller.response.BorderStats;
import ca.gc.cbsa.mcoe.bravo.controller.response.BorderStatsCounts;
import ca.gc.cbsa.mcoe.bravo.controller.response.CommercialCount;
import ca.gc.cbsa.mcoe.bravo.controller.response.Province;
import ca.gc.cbsa.mcoe.bravo.controller.response.ProvincialComparisonStats;
import ca.gc.cbsa.mcoe.bravo.controller.response.TravellersCount;

@Component
public class StatsUtil {

	public List<ProvincialComparisonStats> buildProvincialComparisonStatsCommercial(Map<String,Long> provincesCountMap) throws ParseException, IOException {
		List<ProvincialComparisonStats> provincialComparisonStatsList = new ArrayList<ProvincialComparisonStats>();
		
		for (String province : provincesCountMap.keySet()) {
			ProvincialComparisonStats provincialComparisonStats = new ProvincialComparisonStats();
			provincialComparisonStats.setProvinceCode(province);
			provincialComparisonStats.setConveyances(provincesCountMap.get(province));
			
			provincialComparisonStatsList.add(provincialComparisonStats);
		}
		
		return provincialComparisonStatsList;
	}
	
	public List<ProvincialComparisonStats> buildProvincialComparisonStatsTravellers(Map<String,Long> provincesCountMap) throws ParseException, IOException {
		List<ProvincialComparisonStats> provincialComparisonStatsList = new ArrayList<ProvincialComparisonStats>();
		
		for (String province : provincesCountMap.keySet()) {
			ProvincialComparisonStats provincialComparisonStats = new ProvincialComparisonStats();
			provincialComparisonStats.setProvinceCode(province);
			provincialComparisonStats.setTravellers(provincesCountMap.get(province));
			
			provincialComparisonStatsList.add(provincialComparisonStats);
		}
		
		return provincialComparisonStatsList;
	}
	
	public List<ProvincialComparisonStats> buildMockProvincialComparisonStats(Map<String,BorderStatsCounts> statsMap, int calendarUnit, int mode) throws ParseException, IOException {
		Faker faker = new Faker();
		
		List<ProvincialComparisonStats> provincialComparisonStatsList = new ArrayList<ProvincialComparisonStats>();
		
		for (Province province : getProvincesList()) {
			ProvincialComparisonStats provincialComparisonStats = new ProvincialComparisonStats();
			provincialComparisonStats.setProvinceCode(province.getProvinceCode());
			if (mode < 6) {
				Long conveyancesCount = 0L;
				for (Map.Entry<String,BorderStatsCounts> statsEntry : statsMap.entrySet()) {
					if (statsEntry.getValue().getConveyances() != null && statsEntry.getValue().getConveyances().getTotal() != null) {
						conveyancesCount = statsEntry.getValue().getConveyances().getTotal();
						break;
					}
				}
				Long totalConveyancesCount = conveyancesCount * statsMap.size();
				long lowRange = (long) (totalConveyancesCount - (totalConveyancesCount * 0.05));
				long highRange = (long) (totalConveyancesCount + (totalConveyancesCount * 0.05));
				
				provincialComparisonStats.setConveyances(Long.valueOf(faker.number().numberBetween(lowRange, highRange)));
			} else {
				Long travellersCount = 0L;
				for (Map.Entry<String,BorderStatsCounts> statsEntry : statsMap.entrySet()) {
					if (statsEntry.getValue().getTravellers() != null && statsEntry.getValue().getTravellers().getTotal() != null) {
						travellersCount = statsEntry.getValue().getTravellers().getTotal();
						break;
					}
				}
				Long totalTravellersCount = travellersCount * statsMap.size();
				long lowRange = (long) (totalTravellersCount - (totalTravellersCount * 0.05));
				long highRange = (long) (totalTravellersCount + (totalTravellersCount * 0.05));
				
				provincialComparisonStats.setTravellers(Long.valueOf(faker.number().numberBetween(lowRange, highRange)));
			}
			
			provincialComparisonStatsList.add(provincialComparisonStats);
		}
		
		return provincialComparisonStatsList;
	}
	
	public AnnualComparisonStats buildMockAnnualComparisonStats(Map<String,BorderStatsCounts> statsMap, int calendarUnit, int mode) {
		AnnualComparisonStats annualComparisonStats = new AnnualComparisonStats();
		if (mode < 6) {
			Long totalCountYear3 = 0L;
			for (Map.Entry<String,BorderStatsCounts> statsEntry : statsMap.entrySet()) {
				if (statsEntry.getValue().getConveyances() != null && statsEntry.getValue().getConveyances().getTotal() != null) {
					totalCountYear3 += statsEntry.getValue().getConveyances().getTotal();
				}
			}
			Long totalCountYear2 = (long) (totalCountYear3 - (totalCountYear3 * 0.02));
			Long totalCountYear1 = (long) (totalCountYear2 - (totalCountYear2 * 0.02));
			
			annualComparisonStats.getConveyances().add(totalCountYear3);
			annualComparisonStats.getConveyances().add(totalCountYear2);
			annualComparisonStats.getConveyances().add(totalCountYear1);
		} else {
			Long totalCountYear3 = 0L;
			for (Map.Entry<String,BorderStatsCounts> statsEntry : statsMap.entrySet()) {
				if (statsEntry.getValue().getTravellers() != null && statsEntry.getValue().getTravellers().getTotal() != null) {
					totalCountYear3 += statsEntry.getValue().getTravellers().getTotal();
				}
			}
			Long totalCountYear2 = (long) (totalCountYear3 - (totalCountYear3 * 0.02));
			Long totalCountYear1 = (long) (totalCountYear2 - (totalCountYear2 * 0.02));
			
			annualComparisonStats.getTravellers().add(totalCountYear3);
			annualComparisonStats.getTravellers().add(totalCountYear2);
			annualComparisonStats.getTravellers().add(totalCountYear1);
		}
		
		return annualComparisonStats;
	}
	
	public List<Province> getProvincesList() throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		ClassPathResource provincesListResource = new ClassPathResource("provinces-list.json");
		
		String provincesListJSONString;
		
		try (InputStream provincesListInputStream = provincesListResource.getInputStream()) {
			provincesListJSONString = IOUtils.toString(provincesListInputStream, "UTF-8"); 
		}
		
		List<Province> provincesList = mapper.readValue(provincesListJSONString, new TypeReference<List<Province>>(){});
		
		return provincesList;
		
	}
	
	public Map<String,BorderStatsCounts> buildEmptyStatsMap(String timeDelimiter, int mode, String startDate, String endDate) throws ParseException {
		Map<String,BorderStatsCounts> emptyStatsMap = new TreeMap<String, BorderStatsCounts>();
		int calendarUnit = 0;
		Calendar startCal = DateUtil.buildCalender(timeDelimiter, startDate);
		Calendar endCal = DateUtil.buildCalender(timeDelimiter, endDate);
		
		if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_HOURLY)) {
			calendarUnit = Calendar.HOUR;
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_DAILY)) {
			calendarUnit = Calendar.DAY_OF_MONTH;
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_MONTHLY)) {
			calendarUnit = Calendar.MONTH;
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_ANNUAL)) {
			calendarUnit = Calendar.YEAR;
		}
		
		SimpleDateFormat formatWithSecs = new SimpleDateFormat(ProjectBravoApiConstants.DATE_FORMAT_WITH_HOUR_MIN_SECS);
		
		while (startCal.before(endCal)) {
			String timestamp = formatWithSecs.format(startCal.getTime());
			
			if (calendarUnit == Calendar.MONTH) {
				timestamp = DateUtil.replaceDayInDateWith(timestamp, "00");
			}
			BorderStatsCounts stats = new BorderStatsCounts();
			stats.setTimestamp(timestamp);
			
			if (mode < 6) {
				stats.setConveyances(new CommercialCount());
			} else {
				stats.setTravellers(new TravellersCount());
			}
			
			emptyStatsMap.put(timestamp, stats);
			startCal.add(calendarUnit, 1);
		}
		
		return emptyStatsMap;
	}

	public BorderStats buildMockStats(int calendarUnit, int mode, String startDate, String endDate, Map<String,BorderStatsCounts> statsMap) throws ParseException {
		BorderStats borderStats = new BorderStats();
		
		Faker faker = new Faker();
		
		SimpleDateFormat format = new SimpleDateFormat(ProjectBravoApiConstants.DATE_FORMAT_WITH_HOUR_MIN_SECS);
		Date startDateObj = format.parse(startDate);
		Date endDateObj = format.parse(endDate); 
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDateObj);
		
		if (calendarUnit == Calendar.MONTH) {
			startCal.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDateObj);
		endCal.add(calendarUnit, 1);
		
		if (calendarUnit == Calendar.MONTH) {
			endCal.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		int divider = 1;
		
		if (calendarUnit == Calendar.HOUR) {
			divider = 8760;
		} else if (calendarUnit == Calendar.DAY_OF_MONTH) {
			divider = 365;
		} else if (calendarUnit == Calendar.MONTH) {
			divider = 12;
		}
		
		while (startCal.before(endCal)) {
			String currentStartDateFormatted = null;
			
			if (calendarUnit == Calendar.MONTH) {
				currentStartDateFormatted = DateUtil.replaceDayInDateWith(format.format(startCal.getTime()), "00");
			} else {
				currentStartDateFormatted = format.format(startCal.getTime());
			}
			System.out.println(currentStartDateFormatted);
			
			BorderStatsCounts stats = new BorderStatsCounts();
			stats.setTimestamp(currentStartDateFormatted);
						
			if (mode < 6) {
				CommercialCount commercialCount = new CommercialCount();
				commercialCount.setTotal(Long.valueOf(faker.number().numberBetween(100000, 250000) / divider));
				stats.setConveyances(commercialCount);
				statsMap.put(currentStartDateFormatted, stats);
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
				statsMap.put(currentStartDateFormatted, stats);
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
	
	public static String getProvinceFromPortCommercial(String port) {
		for (Entry<String, String> provinceEntry : ProjectBravoApiConstants.PORT_PREFIX_PROV_MAP_COMMERCIAL.entrySet()) {
			if (port.startsWith(provinceEntry.getValue())) {
				return provinceEntry.getKey();
			}
		}
		
		return null;
	}
	
	public static String getProvinceFromPortTravellers(String port) {
		for (Entry<String, String> provinceEntry : ProjectBravoApiConstants.PORT_PREFIX_PROV_MAP_TRAVELLERS.entrySet()) {
			if (port.startsWith(provinceEntry.getValue())) {
				return provinceEntry.getKey();
			}
		}
		
		return null;
	}
}
