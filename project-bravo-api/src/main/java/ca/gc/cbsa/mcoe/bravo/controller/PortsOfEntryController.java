package ca.gc.cbsa.mcoe.bravo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gc.cbsa.mcoe.bravo.ProjectBravoApiConstants;
import ca.gc.cbsa.mcoe.bravo.controller.response.BorderStats;
import ca.gc.cbsa.mcoe.bravo.controller.response.BorderStatsCounts;
import ca.gc.cbsa.mcoe.bravo.controller.response.CommercialCount;
import ca.gc.cbsa.mcoe.bravo.controller.response.PortOfEntry;
import ca.gc.cbsa.mcoe.bravo.controller.response.TravellersCount;
import ca.gc.cbsa.mcoe.bravo.domain.commercial.DailyStatsCommercial;
import ca.gc.cbsa.mcoe.bravo.domain.commercial.HourlyStatsCommercial;
import ca.gc.cbsa.mcoe.bravo.domain.commercial.MonthlyStatsCommercial;
import ca.gc.cbsa.mcoe.bravo.domain.commercial.PortStatsCommercial;
import ca.gc.cbsa.mcoe.bravo.domain.commercial.PortStatsCountsCommercial;
import ca.gc.cbsa.mcoe.bravo.domain.travellers.DailyStatsTravellers;
import ca.gc.cbsa.mcoe.bravo.domain.travellers.HourlyStatsTravellers;
import ca.gc.cbsa.mcoe.bravo.domain.travellers.MonthlyStatsTravellers;
import ca.gc.cbsa.mcoe.bravo.domain.travellers.PassageCounts;
import ca.gc.cbsa.mcoe.bravo.domain.travellers.PortStatsTravellers;
import ca.gc.cbsa.mcoe.bravo.domain.travellers.ReferralCounts;
import ca.gc.cbsa.mcoe.bravo.repository.commercial.DailyStatsCommercialRepository;
import ca.gc.cbsa.mcoe.bravo.repository.commercial.HourlyStatsCommercialRepository;
import ca.gc.cbsa.mcoe.bravo.repository.commercial.MonthlyStatsCommercialRepository;
import ca.gc.cbsa.mcoe.bravo.repository.travellers.DailyStatsTravellersRepository;
import ca.gc.cbsa.mcoe.bravo.repository.travellers.HourlyStatsTravellersRepository;
import ca.gc.cbsa.mcoe.bravo.repository.travellers.MonthlyStatsTravellersRepository;
import ca.gc.cbsa.mcoe.bravo.util.DateUtil;
import ca.gc.cbsa.mcoe.bravo.util.StatsUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class PortsOfEntryController {

	@Autowired
	private StatsUtil bravoStatsUtil;
	
	@Autowired
	private HourlyStatsCommercialRepository hourlyStatsCommercialRepository;
	
	@Autowired
	private DailyStatsCommercialRepository dailyStatsCommercialRepository;
	
	@Autowired
	private MonthlyStatsCommercialRepository monthlyStatsCommercialRepository;

	@Autowired
	private HourlyStatsTravellersRepository hourlyStatsTravellersRepository;
	
	@Autowired
	private DailyStatsTravellersRepository dailyStatsTravellersRepository;
	
	@Autowired
	private MonthlyStatsTravellersRepository monthlyStatsTravellersRepository;
	
	@RequestMapping(value = "/ports-of-entry", method = RequestMethod.GET)
	@ApiOperation("Returns a list of POEs.")
	public List<PortOfEntry> getPortsOfEntry() throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		ClassPathResource portListResource = new ClassPathResource("port-list-commercial.json");

		String portListJSONString;

		try (InputStream portListInputStream = portListResource.getInputStream()) {
			portListJSONString = IOUtils.toString(portListInputStream, "UTF-8");
		}

		List<PortOfEntry> poeList = mapper.readValue(portListJSONString, new TypeReference<List<PortOfEntry>>() {
		});

		return poeList;
	}

	@RequestMapping(value = "/ports-of-entry/{workLocationCode}", method = RequestMethod.GET)
	@ApiOperation("Returns a specific POE by work location. Returns 404 if not found.")
	public PortOfEntry getPortOfEntryByWorkLocation(
			@ApiParam("Work location code.  Example: 0453") @PathVariable(value = "workLocationCode") String workLocationCode)
			throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		ClassPathResource portListResource = new ClassPathResource("port-list-commercial.json");

		String portListJSONString;

		try (InputStream portListInputStream = portListResource.getInputStream()) {
			portListJSONString = IOUtils.toString(portListInputStream, "UTF-8");
		}

		List<PortOfEntry> poeList = mapper.readValue(portListJSONString, new TypeReference<List<PortOfEntry>>() {
		});

		for (PortOfEntry poe : poeList) {
			if (poe.getPortWorkLocationCode().equals(workLocationCode)) {
				return poe;
			}
		}
		return null;
	}

	@RequestMapping(value = "/ports-of-entry/{workLocationCode}/stats", method = RequestMethod.GET)
	@ApiOperation("Returns stats for a specific POE by work location by a specific date range. Returns 404 if not found.")
	public BorderStats getPortOfEntryStatsByWorkLocation(
			@ApiParam("Work location code.  Example: 0453") @PathVariable(value = "workLocationCode") String workLocationCode,
			@ApiParam("Mode.  1 = Commercial Hwy, 2 = Commercial Rail, 3 = Commercial Marine, 4 = Commercial Air, 5 = Commercial Multi, 6 = Travellers Hwy, 7 = Travellers Rail, 8 = Travellers Marine, 9 = Travellers Air, 10 = Travellers Multi") @RequestParam(value="mode") Integer mode,
			@ApiParam("Time delimiter.  Valid values: hour, day, month, year.") @RequestParam("timeDelimiter") String timeDelimiter,
			@ApiParam("Start Date in Eastern Standard Time.  Format:  for hourly queries use 'yyyy-MM-dd HH:mm', for daily queries use 'yyyy-MM-dd', for monthly queries use 'yyyy-MM', for yearly queries use 'yyyy'") @RequestParam("startDate") String startDate,
			@ApiParam("End Date in Eastern Standard Time.  Format:  for hourly queries use 'yyyy-MM-dd HH:mm', for daily queries use 'yyyy-MM-dd', for monthly queries use 'yyyy-MM', for yearly queries use 'yyyy'") @RequestParam("endDate") String endDate)
			throws ParseException {
		BorderStats borderStats = new BorderStats();

		String fullStartDate = DateUtil.buildFullDateString(timeDelimiter, startDate);
		String fullEndDate = DateUtil.buildFullDateString(timeDelimiter, endDate);
		
		Map<String,BorderStatsCounts> statsMap = buildEmptyStatsMap(timeDelimiter, mode, startDate, endDate);
		
		if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_HOURLY)) {
			if (mode < 6) {
				List<HourlyStatsCommercial> hourlyStatsList = hourlyStatsCommercialRepository.findHourlyStatsBetween(fullStartDate, fullEndDate);
				
				for (HourlyStatsCommercial hourlyStats : hourlyStatsList) {
					BorderStatsCounts borderStatsCounts = new BorderStatsCounts();
					borderStatsCounts.setTimestamp(hourlyStats.getId());
					
					for (PortStatsCommercial portStats : hourlyStats.getPorts()) {
						if (portStats.getPort().equals(workLocationCode)) {
							for (PortStatsCountsCommercial counts : portStats.getCounts()) {
								if (counts.getMode().equals(mode.toString())) {
									CommercialCount conveyances = new CommercialCount();
									conveyances.setTotal(counts.getCount());
									borderStatsCounts.setConveyances(conveyances);
									statsMap.put(hourlyStats.getId(), borderStatsCounts);
								}
							}
						}
					}
				}
				borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(Calendar.HOUR, mode));
			} else {
				List<HourlyStatsTravellers> hourlyStatsList = hourlyStatsTravellersRepository.findHourlyStatsBetween(fullStartDate, fullEndDate);
				
				for (HourlyStatsTravellers hourlyStats : hourlyStatsList) {
					BorderStatsCounts borderStatsCounts = new BorderStatsCounts();
					borderStatsCounts.setTimestamp(hourlyStats.getId());
					
					for (PortStatsTravellers portStats : hourlyStats.getPorts()) {
						if (portStats.getPort().equals(workLocationCode)) {
							TravellersCount travellersCount = new TravellersCount();
							
							for (PassageCounts counts : portStats.getPassageCounts()) {
								travellersCount.setTotal(counts.getCount());
							}
							for (ReferralCounts counts : portStats.getReferralCounts()) {
								travellersCount.setTotalSecondary(counts.getCount());
							}
							borderStatsCounts.setTravellers(travellersCount);
							statsMap.put(hourlyStats.getId(), borderStatsCounts);
						}
					}
				}
				borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(Calendar.HOUR, mode));
			}
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_DAILY)) {
			if (mode < 6) {
				List<DailyStatsCommercial> dailyStatsList = dailyStatsCommercialRepository.findDailyStatsBetween(fullStartDate, fullEndDate);
				
				for (DailyStatsCommercial dailyStats : dailyStatsList) {
					BorderStatsCounts borderStatsCounts = new BorderStatsCounts();
					borderStatsCounts.setTimestamp(dailyStats.getId());
					
					for (PortStatsCommercial portStats : dailyStats.getPorts()) {
						if (portStats.getPort().equals(workLocationCode)) {
							for (PortStatsCountsCommercial counts : portStats.getCounts()) {
								if (counts.getMode().equals(mode.toString())) {
									CommercialCount conveyances = new CommercialCount();
									conveyances.setTotal(counts.getCount());
									borderStatsCounts.setConveyances(conveyances);
									statsMap.put(dailyStats.getId(), borderStatsCounts);
								}
							}
						}
					}
				}
				borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(Calendar.DAY_OF_MONTH, mode));
			} else {
				List<DailyStatsTravellers> dailyStatsList = dailyStatsTravellersRepository.findDailyStatsBetween(fullStartDate, fullEndDate);
				
				for (DailyStatsTravellers dailyStats : dailyStatsList) {
					BorderStatsCounts borderStatsCounts = new BorderStatsCounts();
					borderStatsCounts.setTimestamp(dailyStats.getId());
					
					for (PortStatsTravellers portStats : dailyStats.getPorts()) {
						if (portStats.getPort().equals(workLocationCode)) {
							TravellersCount travellersCount = new TravellersCount();
							
							for (PassageCounts counts : portStats.getPassageCounts()) {
								travellersCount.setTotal(counts.getCount());
							}
							for (ReferralCounts counts : portStats.getReferralCounts()) {
								travellersCount.setTotalSecondary(counts.getCount());
							}
							borderStatsCounts.setTravellers(travellersCount);
							statsMap.put(dailyStats.getId(), borderStatsCounts);
						}
					}
				}
				borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(Calendar.DAY_OF_MONTH, mode));
			}
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_MONTHLY)) {
			if (mode < 6) {
				List<MonthlyStatsCommercial> monthlyStatsList = monthlyStatsCommercialRepository.findMonthlyStatsBetween(fullStartDate, fullEndDate);
				
				for (MonthlyStatsCommercial monthlyStats : monthlyStatsList) {
					BorderStatsCounts borderStatsCounts = new BorderStatsCounts();
					borderStatsCounts.setTimestamp(monthlyStats.getId());
					
					for (PortStatsCommercial portStats : monthlyStats.getPorts()) {
						if (portStats.getPort().equals(workLocationCode)) {
							for (PortStatsCountsCommercial counts : portStats.getCounts()) {
								if (counts.getMode().equals(mode.toString())) {
									CommercialCount conveyances = new CommercialCount();
									conveyances.setTotal(counts.getCount());
									borderStatsCounts.setConveyances(conveyances);
									statsMap.put(monthlyStats.getId(), borderStatsCounts);
								}
							}
						}
					}
				}
				borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(Calendar.MONTH, mode));
			} else {
				List<MonthlyStatsTravellers> monthlyStatsList = monthlyStatsTravellersRepository.findMonthlyStatsBetween(fullStartDate, fullEndDate);
				
				for (MonthlyStatsTravellers monthlyStats : monthlyStatsList) {
					BorderStatsCounts borderStatsCounts = new BorderStatsCounts();
					borderStatsCounts.setTimestamp(monthlyStats.getId());
					
					for (PortStatsTravellers portStats : monthlyStats.getPorts()) {
						if (portStats.getPort().equals(workLocationCode)) {
							TravellersCount travellersCount = new TravellersCount();
							
							for (PassageCounts counts : portStats.getPassageCounts()) {
								travellersCount.setTotal(counts.getCount());
							}
							for (ReferralCounts counts : portStats.getReferralCounts()) {
								travellersCount.setTotalSecondary(counts.getCount());
							}
							borderStatsCounts.setTravellers(travellersCount);
							statsMap.put(monthlyStats.getId(), borderStatsCounts);
						}
					}
				}
				borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(Calendar.MONTH, mode));
			}
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_ANNUAL)) {
			return bravoStatsUtil.buildMockStats(Calendar.YEAR, mode, fullStartDate, fullEndDate);
		}

		for (Map.Entry<String, BorderStatsCounts> entry : statsMap.entrySet()) {
			borderStats.getStats().add(entry.getValue());
	    }
		
		return borderStats;
	}

	private Map<String,BorderStatsCounts> buildEmptyStatsMap(String timeDelimiter, int mode, String startDate, String endDate) throws ParseException {
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
