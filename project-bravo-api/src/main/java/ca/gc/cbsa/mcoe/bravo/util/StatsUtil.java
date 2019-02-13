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

	public BorderStats buildMockStats(int calendarUnit, int mode, String startDate, String endDate) throws ParseException {
		BorderStats borderStats = new BorderStats();
		
		Faker faker = new Faker();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date startDateObj = format.parse(startDate);
		Date endDateObj = format.parse(endDate); 
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDateObj);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDateObj);
		
		int divider = getMockStatsDivider(calendarUnit);
		
		while (startCal.before(endCal)) {
			BorderStatsCounts stats = new BorderStatsCounts();
			stats.setTimestamp(format.format(startCal.getTime()));
						
			if (mode < 6) {
				CommercialCount commercialCount = new CommercialCount();
				commercialCount.setTotal(Long.valueOf(faker.number().numberBetween(100000, 250000) / divider));
				stats.setConveyances(commercialCount);
			} else {
				TravellersCount travellers = new TravellersCount();
				travellers.setAirSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				travellers.setAirTotal(Long.valueOf(faker.number().numberBetween(25000, 75000) / divider));
				travellers.setLandSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				travellers.setLandTotal(Long.valueOf(faker.number().numberBetween(75000, 150000) / divider));
				travellers.setTotalSecondary(Long.valueOf(faker.number().numberBetween(15000, 25000) / divider));
				travellers.setTotal(Long.valueOf(faker.number().numberBetween(100000, 250000) / divider));
				stats.setTravellers(travellers);
			}
			
			borderStats.getStats().add(stats);
			startCal.add(calendarUnit, 1);
		}
		
		borderStats.setAnnualComparisonStats(buildMockAnnualComparisonStats(calendarUnit, mode));
		
		return borderStats;
		
	}
	
	public List<ProvincialComparisonStats> buildMockProvincialComparisonStats(int calendarUnit, int mode) throws ParseException, IOException {
		int divider = getMockStatsDivider(calendarUnit);
		Faker faker = new Faker();
		
		List<ProvincialComparisonStats> provincialComparisonStatsList = new ArrayList<ProvincialComparisonStats>();
		
		for (Province province : getProvincesList()) {
			ProvincialComparisonStats provincialComparisonStats = new ProvincialComparisonStats();
			provincialComparisonStats.setProvinceCode(province.getProvinceCode());
			if (mode < 6) {
				provincialComparisonStats.setConveyances(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
			} else {
				provincialComparisonStats.setTravellers(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
			}
			
			provincialComparisonStatsList.add(provincialComparisonStats);
		}
		
		return provincialComparisonStatsList;
	}
	
	public AnnualComparisonStats buildMockAnnualComparisonStats(int calendarUnit, int mode) {
		Faker faker = new Faker();
		int divider = getMockStatsDivider(calendarUnit);
		
		AnnualComparisonStats annualComparisonStats = new AnnualComparisonStats();
		if (mode < 6) {
			for (int i=0; i < 3; i++) {
				annualComparisonStats.getConveyances().add(Long.valueOf(faker.number().numberBetween(100000, 250000) / divider));
			}
		} else {
			for (int i=0; i < 3; i++) {
				annualComparisonStats.getTravellers().add(Long.valueOf(faker.number().numberBetween(100000, 250000) / divider));
			}
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
	
	private int getMockStatsDivider(int calendarUnit) {
		int divider = 1;
		
		if (calendarUnit == Calendar.HOUR) {
			divider = 8760;
		} else if (calendarUnit == Calendar.DAY_OF_MONTH) {
			divider = 365;
		} else if (calendarUnit == Calendar.MONTH) {
			divider = 12;
		}
		
		return divider;
	}
}
