package ca.gc.cbsa.mcoe.bravo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
import ca.gc.cbsa.mcoe.bravo.controller.response.PortOfEntry;
import ca.gc.cbsa.mcoe.bravo.util.DateUtil;
import ca.gc.cbsa.mcoe.bravo.util.StatsUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class PortsOfEntryController {

	@Autowired
	private StatsUtil bravoStatsUtil;
	
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
			@ApiParam("Time delimiter.  Valid values: hour, day, month.") @RequestParam("timeDelimiter") String timeDelimiter,
			@ApiParam("Start Date in Eastern Standard Time.  Format:  for hourly queries use 'yyyy-MM-dd HH:mm', for daily queries use 'yyyy-MM-dd', for monthly queries use 'yyyy-MM', for yearly queries use 'yyyy'") @RequestParam("startDate") String startDate,
			@ApiParam("End Date in Eastern Standard Time.  Format:  for hourly queries use 'yyyy-MM-dd HH:mm', for daily queries use 'yyyy-MM-dd', for monthly queries use 'yyyy-MM', for yearly queries use 'yyyy'") @RequestParam("endDate") String endDate)
			throws ParseException {

		String fullStartDate = DateUtil.buildFullDateString(timeDelimiter, startDate);
		String fullEndDate = DateUtil.buildFullDateString(timeDelimiter, endDate);
		
		Map<String,BorderStatsCounts> statsMap = bravoStatsUtil.buildEmptyStatsMap(timeDelimiter, mode, startDate, endDate);
		
		BorderStats borderStats = null;
		
		if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_HOURLY)) {
			borderStats = bravoStatsUtil.buildMockStats(Calendar.HOUR, mode, fullStartDate, fullEndDate, statsMap);
			borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(statsMap, Calendar.HOUR, mode));
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_DAILY)) {
			borderStats = bravoStatsUtil.buildMockStats(Calendar.DAY_OF_MONTH, mode, fullStartDate, fullEndDate, statsMap);
			borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(statsMap, Calendar.DAY_OF_MONTH, mode));
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_MONTHLY)) {
			borderStats = bravoStatsUtil.buildMockStats(Calendar.MONTH, mode, fullStartDate, fullEndDate, statsMap);
			borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(statsMap, Calendar.MONTH, mode));
		}
		
		return borderStats;
	}

}
