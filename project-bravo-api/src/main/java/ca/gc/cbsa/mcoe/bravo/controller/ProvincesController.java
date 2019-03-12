package ca.gc.cbsa.mcoe.bravo.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.gc.cbsa.mcoe.bravo.ProjectBravoApiConstants;
import ca.gc.cbsa.mcoe.bravo.controller.response.BorderStats;
import ca.gc.cbsa.mcoe.bravo.controller.response.BorderStatsCounts;
import ca.gc.cbsa.mcoe.bravo.controller.response.Province;
import ca.gc.cbsa.mcoe.bravo.util.DateUtil;
import ca.gc.cbsa.mcoe.bravo.util.StatsUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class ProvincesController {
	
	@Autowired
	private StatsUtil bravoStatsUtil;

	@RequestMapping(value = "/provinces", method = RequestMethod.GET)
	@ApiOperation("Returns a list of provinces.")
	public List<Province> getProvinces() throws IOException {
		return bravoStatsUtil.getProvincesList();
	}
	
	@RequestMapping(value = "/provinces/{provinceCode}/stats", method = RequestMethod.GET)
	@ApiOperation("Returns stats for a specific Province by province code for a specific date range. Returns 404 if not found.")
	public BorderStats getProvincialStats(@ApiParam("Province code.  Example: ON") @PathVariable(value = "provinceCode") String provinceCode,
			@ApiParam("Mode.  1 = Commercial Hwy, 2 = Commercial Rail, 3 = Commercial Marine, 4 = Commercial Air, 5 = Commercial Multi, 6 = Travellers Hwy, 7 = Travellers Rail, 8 = Travellers Marine, 9 = Travellers Air, 10 = Travellers Multi") @RequestParam(value="mode") Integer mode,
			@ApiParam("Time delimiter.  Valid values: hour, day, month.") @RequestParam("timeDelimiter") String timeDelimiter,
			@ApiParam("Start Date in Eastern Standard Time.  Format:  for hourly queries use 'yyyy-MM-dd HH:mm', for daily queries use 'yyyy-MM-dd', for monthly queries use 'yyyy-MM', for yearly queries use 'yyyy'") @RequestParam("startDate") String startDate,
			@ApiParam("End Date in Eastern Standard Time.  Format:  for hourly queries use 'yyyy-MM-dd HH:mm', for daily queries use 'yyyy-MM-dd', for monthly queries use 'yyyy-MM', for yearly queries use 'yyyy'") @RequestParam("endDate") String endDate) throws ParseException, IOException {
		BorderStats borderStats = new BorderStats();
		
		String fullStartDate = DateUtil.buildFullDateString(timeDelimiter, startDate);
		String fullEndDate = DateUtil.buildFullDateString(timeDelimiter, endDate);
		
		Map<String,BorderStatsCounts> statsMap = bravoStatsUtil.buildEmptyStatsMap(timeDelimiter, mode, startDate, endDate);
		
		if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_HOURLY)) {
			borderStats = bravoStatsUtil.buildMockStats(Calendar.HOUR, mode, fullStartDate, fullEndDate, statsMap);
			borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(statsMap, Calendar.HOUR, mode));
			borderStats.setProvincialComparisonStats(bravoStatsUtil.buildMockProvincialComparisonStats(statsMap, Calendar.HOUR, mode));
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_DAILY)) {
			borderStats = bravoStatsUtil.buildMockStats(Calendar.DAY_OF_MONTH, mode, fullStartDate, fullEndDate, statsMap);
			borderStats.setProvincialComparisonStats(bravoStatsUtil.buildMockProvincialComparisonStats(statsMap, Calendar.DAY_OF_MONTH, mode));
			borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(statsMap, Calendar.DAY_OF_MONTH, mode));
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_MONTHLY)) {
			borderStats = bravoStatsUtil.buildMockStats(Calendar.MONTH, mode, fullStartDate, fullEndDate, statsMap);
			borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(statsMap, Calendar.MONTH, mode));
			borderStats.setProvincialComparisonStats(bravoStatsUtil.buildMockProvincialComparisonStats(statsMap, Calendar.MONTH, mode));
		}
		
		return borderStats;
	}
	
}
