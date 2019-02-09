package ca.gc.cbsa.mcoe.bravo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

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
import ca.gc.cbsa.mcoe.bravo.domain.DailyStats;
import ca.gc.cbsa.mcoe.bravo.domain.HourlyStats;
import ca.gc.cbsa.mcoe.bravo.domain.MonthlyStats;
import ca.gc.cbsa.mcoe.bravo.domain.PortStats;
import ca.gc.cbsa.mcoe.bravo.domain.PortStatsCounts;
import ca.gc.cbsa.mcoe.bravo.repository.commercial.DailyStatsRepository;
import ca.gc.cbsa.mcoe.bravo.repository.commercial.HourlyStatsRepository;
import ca.gc.cbsa.mcoe.bravo.repository.commercial.MonthlyStatsRepository;
import ca.gc.cbsa.mcoe.bravo.util.StatsUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class PortsOfEntryController {

	@Autowired
	private StatsUtil bravoStatsUtil;
	
	@Autowired
	private HourlyStatsRepository hourlyStatsRepository;
	
	@Autowired
	private DailyStatsRepository dailyStatsRepository;
	
	@Autowired
	private MonthlyStatsRepository monthlyStatsRepository;

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
			@ApiParam("Start Date in Eastern Standard Time.  Format: yyyy-MM-dd HH:mm") @RequestParam("startDate") String startDate,
			@ApiParam("End Date in Eastern Standard Time.  Format: yyyy-MM-dd HH:mm") @RequestParam("endDate") String endDate)
			throws ParseException {
		BorderStats borderStats = new BorderStats();

		if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_HOURLY)) {
			if (mode < 6) {
				List<HourlyStats> hourlyStatsList = hourlyStatsRepository.findHourlyStatsBetween(startDate, endDate);
				
				for (HourlyStats hourlyStats : hourlyStatsList) {
					BorderStatsCounts stats = new BorderStatsCounts();
					stats.setTimestamp(hourlyStats.getId());
					
					for (PortStats portStats : hourlyStats.getPorts()) {
						if (portStats.getPort().equals(workLocationCode)) {
							for (PortStatsCounts counts : portStats.getCounts()) {
								if (counts.getMode().equals(mode.toString())) {
									CommercialCount conveyances = new CommercialCount();
									conveyances.setTotal(counts.getCount());
									stats.setConveyances(conveyances);
									borderStats.getStats().add(stats);
								}
							}
						}
					}
				}
				if (!borderStats.getStats().isEmpty()) {
					borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(Calendar.HOUR, mode));
				}
			} else {
				return bravoStatsUtil.buildMockStats(Calendar.HOUR, mode, startDate, endDate);
			}
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_DAILY)) {
			if (mode < 6) {
				List<DailyStats> dailyStatsList = dailyStatsRepository.findDailyStatsBetween(startDate, endDate);
				
				for (DailyStats dailyStats : dailyStatsList) {
					BorderStatsCounts stats = new BorderStatsCounts();
					stats.setTimestamp(dailyStats.getId());
					
					for (PortStats portStats : dailyStats.getPorts()) {
						if (portStats.getPort().equals(workLocationCode)) {
							for (PortStatsCounts counts : portStats.getCounts()) {
								if (counts.getMode().equals(mode.toString())) {
									CommercialCount conveyances = new CommercialCount();
									conveyances.setTotal(counts.getCount());
									stats.setConveyances(conveyances);
									borderStats.getStats().add(stats);
								}
							}
						}
					}
				}
				if (!borderStats.getStats().isEmpty()) {
					borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(Calendar.DAY_OF_MONTH, mode));
				}
			} else {
				return bravoStatsUtil.buildMockStats(Calendar.DAY_OF_MONTH, mode, startDate, endDate);
			}
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_MONTHLY)) {
			if (mode < 6) {
				List<MonthlyStats> monthlyStatsList = monthlyStatsRepository.findMonthlyStatsBetween(startDate, endDate);
				
				for (MonthlyStats monthlyStats : monthlyStatsList) {
					BorderStatsCounts stats = new BorderStatsCounts();
					stats.setTimestamp(monthlyStats.getId());
					
					for (PortStats portStats : monthlyStats.getPorts()) {
						if (portStats.getPort().equals(workLocationCode)) {
							for (PortStatsCounts counts : portStats.getCounts()) {
								if (counts.getMode().equals(mode.toString())) {
									CommercialCount conveyances = new CommercialCount();
									conveyances.setTotal(counts.getCount());
									stats.setConveyances(conveyances);
									borderStats.getStats().add(stats);
								}
							}
						}
					}
				}
				if (!borderStats.getStats().isEmpty()) {
					borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(Calendar.MONTH, mode));
				}
			} else {
				return bravoStatsUtil.buildMockStats(Calendar.MONTH, mode, startDate, endDate);
			}
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_ANNUAL)) {
			return bravoStatsUtil.buildMockStats(Calendar.YEAR, mode, startDate, endDate);
		}

		return borderStats;
	}

}
