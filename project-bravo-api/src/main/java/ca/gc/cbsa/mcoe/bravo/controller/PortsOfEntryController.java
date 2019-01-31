package ca.gc.cbsa.mcoe.bravo.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import ca.gc.cbsa.mcoe.bravo.domain.BorderStats;
import ca.gc.cbsa.mcoe.bravo.domain.PortOfEntry;
import ca.gc.cbsa.mcoe.bravo.domain.ProjectBravoApiConstants;
import ca.gc.cbsa.mcoe.bravo.repository.HourlyStatsRepository;
import ca.gc.cbsa.mcoe.bravo.util.StatsUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class PortsOfEntryController {

	@Autowired
	private HourlyStatsRepository hourlyStatsRepository;
	
	@RequestMapping(value = "/ports-of-entry", method = RequestMethod.GET)
	@ApiOperation("Returns a list of POEs.")
	public List<PortOfEntry> getPortsOfEntry() throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		File portListFile = new ClassPathResource("port-list-commercial.json").getFile();
		List<PortOfEntry> poeList = mapper.readValue(portListFile, new TypeReference<List<PortOfEntry>>(){});
		
		return poeList;
	}
	
	@RequestMapping(value = "/ports-of-entry/{workLocationCode}", method = RequestMethod.GET)
	@ApiOperation("Returns a specific POE by work location. Returns 404 if not found.")
	public PortOfEntry getPortOfEntryByWorkLocation(@ApiParam("Work location code.  Example: 0453") @PathVariable(value = "workLocationCode") String workLocationCode) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		File portListFile = new ClassPathResource("port-list-commercial.json").getFile();
		List<PortOfEntry> poeList = mapper.readValue(portListFile, new TypeReference<List<PortOfEntry>>(){});
		
		for (PortOfEntry poe : poeList) {
			if (poe.getPortWorkLocationCode().equals(workLocationCode)) {
				return poe;
			}
		}
		return null;
	}

	@RequestMapping(value = "/ports-of-entry/{workLocationCode}/stats", method = RequestMethod.GET)
	@ApiOperation("Returns stats for a specific POE by work location by a specific date range. Returns 404 if not found.")
	public List<BorderStats> getPortOfEntryStatsByWorkLocation(@ApiParam("Work location code.  Example: 0453") @PathVariable(value = "workLocationCode") String workLocationCode,
			@ApiParam("Mode.  1 = Commercial Hwy, 2 = Commercial Rail, 3 = Commercial Marine, 4 = Commercial Air, 5 = Commercial Multi, 6 = Travellers Hwy, 7 = Travellers Rail, 8 = Travellers Marine, 9 = Travellers Air, 10 = Travellers Multi") @RequestParam("mode") Integer mode,
			@ApiParam("Time delimiter.  Valid values: hour, day, month, year.") @RequestParam("timeDelimiter") String timeDelimiter,
			@ApiParam("Start Date in Eastern Standard Time.  Format: yyyy-MM-dd HH:mm") @RequestParam("startDate") String startDate,
			@ApiParam("End Date in Eastern Standard Time.  Format: yyyy-MM-dd HH:mm") @RequestParam("endDate") String endDate) throws ParseException {
		if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_HOURLY)) {
			return StatsUtil.buildMockStats(Calendar.HOUR, mode, startDate, endDate);
			
			/*List<HourlyStats> hourlyStatsList = hourlyStatsRepository.findHourlyStatsBetween(startDate, endDate);
			
			for (HourlyStats hourlyStats : hourlyStatsList) {
				for (PortStats portStats : hourlyStats.getPorts()) {
					if (portStats.getPort().equals(workLocationCode)) {
						for (PortStatsCounts counts : portStats.getCounts()) {
							if (counts.getMode().equals(mode.toString())) {
								BorderStats portOfEntryStats = new BorderStats();
								portOfEntryStats.setTotal(counts.getCount());
								portOfEntryStats.setTimestamp(hourlyStats.getId());
								portOfEntryStatsList.add(portOfEntryStats);
							}
						}
					}
				}
			}*/
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_DAILY)) {
			return StatsUtil.buildMockStats(Calendar.DAY_OF_MONTH, mode, startDate, endDate);
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_MONTHLY)) {
			return StatsUtil.buildMockStats(Calendar.MONTH, mode, startDate, endDate);
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_ANNUAL)) {
			return StatsUtil.buildMockStats(Calendar.YEAR, mode, startDate, endDate);
		}
		
		return null;
	}
	
}
