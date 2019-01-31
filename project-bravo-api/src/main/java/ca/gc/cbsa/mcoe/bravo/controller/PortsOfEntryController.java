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

import ca.gc.cbsa.mcoe.bravo.domain.HourlyStats;
import ca.gc.cbsa.mcoe.bravo.domain.PortOfEntry;
import ca.gc.cbsa.mcoe.bravo.domain.PortOfEntryStats;
import ca.gc.cbsa.mcoe.bravo.domain.PortStats;
import ca.gc.cbsa.mcoe.bravo.domain.PortStatsCounts;
import ca.gc.cbsa.mcoe.bravo.domain.ProjectBravoApiConstants;
import ca.gc.cbsa.mcoe.bravo.domain.Province;
import ca.gc.cbsa.mcoe.bravo.repository.HourlyStatsRepository;
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
	public PortOfEntry getPortOfEntryByWorkLocation(@ApiParam("Division ID.  1 = Travellers, 2 = Commercial") @PathVariable(value = "divisionCode") Integer divisionCode,
			@ApiParam("Work location code.  Example: 0453") @PathVariable(value = "workLocationCode") String workLocationCode) {
		PortOfEntry portOfEntry = new PortOfEntry("0453", "AMBASSADOR BRIDGE", "1100", "SOUTHERN ONTARIO", "1201",
				"ON DISTRICT", "45 AMBASSADOR LANE", "TEST", "WINDSOR", "ON", "K1K1K1");
		return portOfEntry;
	}

	@RequestMapping(value = "/ports-of-entry/{workLocationCode}/stats", method = RequestMethod.GET)
	@ApiOperation("Returns stats for a specific POE by work location by a specific date range. Returns 404 if not found.")
	public List<PortOfEntryStats> getPortOfEntryStatsByWorkLocation(@ApiParam("Division ID.  1 = Travellers, 2 = Commercial") @PathVariable(value = "divisionCode") Integer divisionCode,
			@ApiParam("Work location code.  Example: 0453") @PathVariable(value = "workLocationCode") String workLocationCode,
			@ApiParam("Mode.  1 = Commercial Hwy, 2 = Commercial Rail, 3 = Commercial Marine, 4 = Commercial Air, 5 = Commercial Multi, 6 = Travellers Hwy, 7 = Travellers Rail, 8 = Travellers Marine, 9 = Travellers Air, 10 = Travellers Multi") @RequestParam("mode") Integer mode,
			@ApiParam("Time delimiter.  Valid values: hour, day, month, year.") @RequestParam("timeDelimiter") String timeDelimiter,
			@ApiParam("Start Date in Eastern Standard Time.  Format: yyyy-MM-dd HH:MM") @RequestParam("startDate") String startDate,
			@ApiParam("End Date in Eastern Standard Time.  Format: yyyy-MM-dd HH:MM") @RequestParam("endDate") String endDate) throws ParseException {
		List<PortOfEntryStats> portOfEntryStatsList = new ArrayList<>();
		
		if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_HOURLY)) {
			List<HourlyStats> hourlyStatsList = hourlyStatsRepository.findHourlyStatsBetween(startDate, endDate);
			
			for (HourlyStats hourlyStats : hourlyStatsList) {
				for (PortStats portStats : hourlyStats.getPorts()) {
					if (portStats.getPort().equals(workLocationCode)) {
						for (PortStatsCounts counts : portStats.getCounts()) {
							if (counts.getMode().equals(mode.toString())) {
								PortOfEntryStats portOfEntryStats = new PortOfEntryStats();
								portOfEntryStats.setTotal(counts.getCount());
								portOfEntryStats.setTimestamp(hourlyStats.getId());
								portOfEntryStatsList.add(portOfEntryStats);
							}
						}
					}
				}
			}
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_DAILY)) {
			Faker faker = new Faker();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM");
			Date startDateObj = format.parse(startDate);
			Date endDateObj = format.parse(endDate); 
			
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDateObj);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDateObj);
			
			while (startCal.before(endCal)) {
				PortOfEntryStats portOfEntryStats = new PortOfEntryStats();
				portOfEntryStats.setTotal(Long.valueOf(faker.number().numberBetween(60, 250)));
				portOfEntryStats.setTimestamp(format.format(startCal));
				portOfEntryStatsList.add(portOfEntryStats);
				startCal.add(Calendar.DAY_OF_MONTH, 1);
			}
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_MONTHLY)) {
			Faker faker = new Faker();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM");
			Date startDateObj = format.parse(startDate);
			Date endDateObj = format.parse(endDate); 
			
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDateObj);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDateObj);
			
			while (startCal.before(endCal)) {
				PortOfEntryStats portOfEntryStats = new PortOfEntryStats();
				portOfEntryStats.setTotal(Long.valueOf(faker.number().numberBetween(1800, 7000)));
				portOfEntryStats.setTimestamp(format.format(startCal));
				portOfEntryStatsList.add(portOfEntryStats);
				startCal.add(Calendar.MONTH, 1);
			}
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_ANNUAL)) {
			Faker faker = new Faker();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM");
			Date startDateObj = format.parse(startDate);
			Date endDateObj = format.parse(endDate); 
			
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDateObj);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDateObj);
			
			while (startCal.before(endCal)) {
				PortOfEntryStats portOfEntryStats = new PortOfEntryStats();
				portOfEntryStats.setTotal(Long.valueOf(faker.number().numberBetween(45000, 70000)));
				portOfEntryStats.setTimestamp(format.format(startCal));
				portOfEntryStatsList.add(portOfEntryStats);
				startCal.add(Calendar.YEAR, 1);
			}
		}
		
		return portOfEntryStatsList;
	}
	
	@RequestMapping(value = "/provinces/{provinceCode}/stats", method = RequestMethod.GET)
	@ApiOperation("Returns a list of provinces.")
	public List<Province> getProvinces() throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		File portListFile = new ClassPathResource("provinces-list.json").getFile();
		List<Province> provincesList = mapper.readValue(portListFile, new TypeReference<List<Province>>(){});
		
		return provincesList;
	}
	
	@RequestMapping(value = "/provinces/{provinceCode}/stats", method = RequestMethod.GET)
	@ApiOperation("Returns stats for a specific province.")
	public List<PortOfEntry> getProvincialStats() throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		File portListFile = new ClassPathResource("port-list-commercial.json").getFile();
		List<PortOfEntry> poeList = mapper.readValue(portListFile, new TypeReference<List<PortOfEntry>>(){});
		
		return poeList;
	}

}
