package ca.gc.cbsa.mcoe.bravo.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import ca.gc.cbsa.mcoe.bravo.domain.ProjectBravoApiConstants;
import ca.gc.cbsa.mcoe.bravo.domain.Province;
import ca.gc.cbsa.mcoe.bravo.util.StatsUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class ProvincesController {

	@RequestMapping(value = "/provinces", method = RequestMethod.GET)
	@ApiOperation("Returns a list of provinces.")
	public List<Province> getProvinces() throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		File portListFile = new ClassPathResource("provinces-list.json").getFile();
		List<Province> provincesList = mapper.readValue(portListFile, new TypeReference<List<Province>>(){});
		
		return provincesList;
	}
	
	@RequestMapping(value = "/provinces/{provinceCode}/stats", method = RequestMethod.GET)
	@ApiOperation("Returns stats for a specific Province by province code for a specific date range. Returns 404 if not found.")
	public List<BorderStats> getProvincialStats(@ApiParam("Province code.  Example: ON") @PathVariable(value = "provinceCode") String provinceCode,
			@ApiParam("Mode.  1 = Commercial Hwy, 2 = Commercial Rail, 3 = Commercial Marine, 4 = Commercial Air, 5 = Commercial Multi, 6 = Travellers Hwy, 7 = Travellers Rail, 8 = Travellers Marine, 9 = Travellers Air, 10 = Travellers Multi") @RequestParam("mode") Integer mode,
			@ApiParam("Time delimiter.  Valid values: hour, day, month, year.") @RequestParam("timeDelimiter") String timeDelimiter,
			@ApiParam("Start Date in Eastern Standard Time.  Format: yyyy-MM-dd HH:mm") @RequestParam("startDate") String startDate,
			@ApiParam("End Date in Eastern Standard Time.  Format: yyyy-MM-dd HH:mm") @RequestParam("endDate") String endDate) throws ParseException {
		if (timeDelimiter.equals(ProjectBravoApiConstants.DATE_RANGE_HOURLY)) {
			return StatsUtil.buildMockStats(Calendar.HOUR, mode, startDate, endDate);
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
