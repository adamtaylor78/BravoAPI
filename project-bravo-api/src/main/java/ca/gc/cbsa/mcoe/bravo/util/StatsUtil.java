package ca.gc.cbsa.mcoe.bravo.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import ca.gc.cbsa.mcoe.bravo.controller.response.BorderStatsCounts;
import ca.gc.cbsa.mcoe.bravo.controller.response.CommercialCount;
import ca.gc.cbsa.mcoe.bravo.controller.response.Province;
import ca.gc.cbsa.mcoe.bravo.controller.response.ProvincialComparisonStats;
import ca.gc.cbsa.mcoe.bravo.controller.response.TravellersCount;

@Component
public class StatsUtil {

	public List<ProvincialComparisonStats> buildMockProvincialComparisonStats(Map<String,BorderStatsCounts> statsMap, int calendarUnit, int mode) throws ParseException, IOException {
		Faker faker = new Faker();
		
		List<ProvincialComparisonStats> provincialComparisonStatsList = new ArrayList<ProvincialComparisonStats>();
		
		for (Province province : getProvincesList()) {
			ProvincialComparisonStats provincialComparisonStats = new ProvincialComparisonStats();
			provincialComparisonStats.setProvinceCode(province.getProvinceCode());
			if (mode < 6) {
				Long conveyancesCount = 0L;
				for (Map.Entry<String,BorderStatsCounts> statsEntry : statsMap.entrySet()) {
					if (statsEntry.getValue().getConveyances() != null) {
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
					if (statsEntry.getValue().getTravellers() != null) {
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
			Long conveyancesCount = 0L;
			for (Map.Entry<String,BorderStatsCounts> statsEntry : statsMap.entrySet()) {
				if (statsEntry.getValue().getConveyances() != null) {
					conveyancesCount = statsEntry.getValue().getConveyances().getTotal();
					break;
				}
			}
			Long totalCountYear1 = conveyancesCount * statsMap.size();
			Long totalCountYear2 = (long) (totalCountYear1 + (totalCountYear1 * 0.02));
			Long totalCountYear3 = (long) (totalCountYear2 + (totalCountYear2 * 0.02));
			
			annualComparisonStats.getConveyances().add(totalCountYear3);
			annualComparisonStats.getConveyances().add(totalCountYear2);
			annualComparisonStats.getConveyances().add(totalCountYear1);
		} else {
			Long travellersCount = 0L;
			for (Map.Entry<String,BorderStatsCounts> statsEntry : statsMap.entrySet()) {
				if (statsEntry.getValue().getTravellers() != null) {
					travellersCount = statsEntry.getValue().getTravellers().getTotal();
					break;
				}
			}
			Long totalCountYear1 = travellersCount * statsMap.size();
			Long totalCountYear2 = (long) (totalCountYear1 + (totalCountYear1 * 0.02));
			Long totalCountYear3 = (long) (totalCountYear2 + (totalCountYear2 * 0.02));
			
			annualComparisonStats.getConveyances().add(totalCountYear3);
			annualComparisonStats.getConveyances().add(totalCountYear2);
			annualComparisonStats.getConveyances().add(totalCountYear1);
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
	
}
