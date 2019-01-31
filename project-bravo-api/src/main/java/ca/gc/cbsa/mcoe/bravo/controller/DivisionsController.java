package ca.gc.cbsa.mcoe.bravo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.gc.cbsa.mcoe.bravo.domain.Division;
import ca.gc.cbsa.mcoe.bravo.domain.HourlyStats;
import ca.gc.cbsa.mcoe.bravo.domain.PortOfEntry;
import ca.gc.cbsa.mcoe.bravo.domain.PortOfEntryStats;
import ca.gc.cbsa.mcoe.bravo.domain.PortStats;
import ca.gc.cbsa.mcoe.bravo.domain.PortStatsCounts;
import ca.gc.cbsa.mcoe.bravo.domain.ProjectBravoApiConstants;
import ca.gc.cbsa.mcoe.bravo.repository.DivisionsRepository;
import ca.gc.cbsa.mcoe.bravo.repository.HourlyStatsRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

@RestController
public class DivisionsController {

	@Autowired
	private DivisionsRepository divisionsRepository;

	@Autowired
	private HourlyStatsRepository hourlyStatsRepository;
	
	@RequestMapping(value = "/divisions", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Get Divisions", notes = "Get a list of Divisions", response = List.class)
	@ApiResponse(code = 200, message = "Success")
	public List<Division> getDivision() {
		return divisionsRepository.findAll();
	}

	@RequestMapping(value = "/divisions/{divisionCode}", method = RequestMethod.GET)
	@ApiOperation("Returns a division by its specific code. 404 if does not exist.")
	public Division getDivisionByCode(@ApiParam("Division ID.  1 = Travellers, 2 = Commercial") @PathVariable(value = "divisionCode") Integer divisionCode) {
		return divisionsRepository.findByCode(divisionCode);
	}

	@RequestMapping(value = "/divisions/{divisionCode}/ports-of-entry", method = RequestMethod.GET)
	@ApiOperation("Returns a list of POEs for a given division.")
	public List<PortOfEntry> getPortsOfEntry(@ApiParam("Division ID.  1 = Travellers, 2 = Commercial") @PathVariable(value = "divisionCode") Integer divisionCode,
			@ApiParam("Work location code") String workLocationCode) {
		PortOfEntry portOfEntry = new PortOfEntry("0453", "AMBASSADOR BRIDGE", "1100", "SOUTHERN ONTARIO", "1201",
				"ON DISTRICT", "45 AMBASSADOR LANE", "TEST", "WINDSOR", "ON", "K1K1K1");
		List<PortOfEntry> portList = new ArrayList<PortOfEntry>();
		portList.add(portOfEntry);
		return portList;
	}

	@RequestMapping(value = "/divisions/{divisionCode}/ports-of-entry/{workLocationCode}", method = RequestMethod.GET)
	@ApiOperation("Returns a specific POE by work location. Returns 404 if not found.")
	public PortOfEntry getPortOfEntryByWorkLocation(@ApiParam("Division ID.  1 = Travellers, 2 = Commercial") @PathVariable(value = "divisionCode") Integer divisionCode,
			@ApiParam("Work location code.  Example: 0453") @PathVariable(value = "workLocationCode") String workLocationCode) {
		PortOfEntry portOfEntry = new PortOfEntry("0453", "AMBASSADOR BRIDGE", "1100", "SOUTHERN ONTARIO", "1201",
				"ON DISTRICT", "45 AMBASSADOR LANE", "TEST", "WINDSOR", "ON", "K1K1K1");
		return portOfEntry;
	}

	@RequestMapping(value = "/divisions/{divisionCode}/ports-of-entry/{workLocationCode}/stats", method = RequestMethod.GET)
	@ApiOperation("Returns stats for a specific POE by work location by a specific date range. Returns 404 if not found.")
	public List<PortOfEntryStats> getPortOfEntryStatsByWorkLocation(@ApiParam("Division ID.  1 = Travellers, 2 = Commercial") @PathVariable(value = "divisionCode") Integer divisionCode,
			@ApiParam("Work location code.  Example: 0453") @PathVariable(value = "workLocationCode") String workLocationCode,
			@ApiParam("Mode.  1 = HIGHWAY, 2 = RAIL, 3 = MARINE, 4 = AIR, 5 = MULTI") @RequestParam("mode") Integer mode,
			@ApiParam("Time delimiter.  Valid values: hour, day, month year.") @RequestParam("timeDelimiter") String timeDelimiter,
			@ApiParam("Start Date in Eastern Standard Time.  Format: yyyy-MM-dd HH:MM") @RequestParam("startDate") String startDate,
			@ApiParam("End Date in Eastern Standard Time.  Format: yyyy-MM-dd HH:MM") @RequestParam("endDate") String endDate) {
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
			
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_MONTHLY)) {
			
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_ANNUAL)) {
			
		}
		
		return portOfEntryStatsList;
	}

}
