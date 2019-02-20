package ca.gc.cbsa.mcoe.bravo.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
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
import ca.gc.cbsa.mcoe.bravo.controller.response.CommercialCount;
import ca.gc.cbsa.mcoe.bravo.controller.response.Province;
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
public class ProvincesController {
	
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
			if (mode < 6) {
				Map<String, Long> provinceToTotalMap = new HashMap<String, Long>();
				List<HourlyStatsCommercial> hourlyStatsList = hourlyStatsCommercialRepository.findHourlyStatsBetween(fullStartDate, fullEndDate);
				
				for (HourlyStatsCommercial hourlyStats : hourlyStatsList) {
					BorderStatsCounts borderStatsCounts = new BorderStatsCounts();
					borderStatsCounts.setTimestamp(hourlyStats.getId());
					
					Long totalConveyances = null;
					CommercialCount conveyances = new CommercialCount();
					
					for (PortStatsCommercial portStats : hourlyStats.getPorts()) {
						if (portStats.getPort().startsWith(ProjectBravoApiConstants.PORT_PREFIX_PROV_MAP_COMMERCIAL.get(provinceCode))) {
							for (PortStatsCountsCommercial counts : portStats.getCounts()) {
								if (counts.getMode().equals(mode.toString())) {
									totalConveyances = (totalConveyances != null) ? totalConveyances + counts.getCount() : counts.getCount();
								}
							}
						}
					}
					
					for (PortStatsCommercial portStats : hourlyStats.getPorts()) {
						String province = StatsUtil.getProvinceFromPortCommercial(portStats.getPort());
						
						if (province != null) {
							for (PortStatsCountsCommercial counts : portStats.getCounts()) {
								if (counts.getMode().equals(mode.toString())) {
									if (provinceToTotalMap.get(province) != null) {
										Long total = provinceToTotalMap.get(province);
										Long newTotal = total + counts.getCount();
										provinceToTotalMap.put(province, newTotal);
									} else {
										provinceToTotalMap.put(province, counts.getCount());
									}
								}
							}
						}
					}
					
					conveyances.setTotal(totalConveyances);
					borderStatsCounts.setConveyances(conveyances);
					statsMap.put(hourlyStats.getId(), borderStatsCounts);
				}
				borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(statsMap, Calendar.HOUR, mode));
				borderStats.setProvincialComparisonStats(bravoStatsUtil.buildProvincialComparisonStatsCommercial(provinceToTotalMap));
			} else {
				Map<String, Long> provinceToTotalMap = new HashMap<String, Long>();
				List<HourlyStatsTravellers> hourlyStatsList = hourlyStatsTravellersRepository.findHourlyStatsBetween(fullStartDate, fullEndDate);
				
				for (HourlyStatsTravellers hourlyStats : hourlyStatsList) {
					BorderStatsCounts borderStatsCounts = new BorderStatsCounts();
					borderStatsCounts.setTimestamp(hourlyStats.getId());
					TravellersCount travellersCount = new TravellersCount();
					Long totalTravellers = null;
					Long totalSecondary = null;
					
					for (PortStatsTravellers portStats : hourlyStats.getPorts()) {
						if (portStats.getPort().startsWith(ProjectBravoApiConstants.PORT_PREFIX_PROV_MAP_TRAVELLERS.get(provinceCode))) {
							//TODO: Filter by mode
							for (PassageCounts counts : portStats.getPassageCounts()) {
								totalTravellers = (totalTravellers != null) ? totalTravellers + counts.getCount() : counts.getCount();
							}
							for (ReferralCounts counts : portStats.getReferralCounts()) {
								totalSecondary = (totalSecondary != null) ? totalSecondary + counts.getCount() : counts.getCount();
							}
						}
					}
					
					for (PortStatsTravellers portStats : hourlyStats.getPorts()) {
						String province = StatsUtil.getProvinceFromPortTravellers(portStats.getPort());
						
						if (province != null) {
							for (PassageCounts counts : portStats.getPassageCounts()) {
								//if (counts.getMode().equals(mode.toString())) {
									if (provinceToTotalMap.get(province) != null) {
										Long total = provinceToTotalMap.get(province);
										Long newTotal = total + counts.getCount();
										provinceToTotalMap.put(province, newTotal);
									} else {
										provinceToTotalMap.put(province, counts.getCount());
									}
								//}
							}
						}
					}
					
					travellersCount.setTotal(totalTravellers);
					travellersCount.setTotalSecondary(totalSecondary);
					borderStatsCounts.setTravellers(travellersCount);
					statsMap.put(hourlyStats.getId(), borderStatsCounts);
				}
				borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(statsMap, Calendar.HOUR, mode));
				borderStats.setProvincialComparisonStats(bravoStatsUtil.buildProvincialComparisonStatsTravellers(provinceToTotalMap));
			}
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_DAILY)) {
			if (mode < 6) {
				Map<String, Long> provinceToTotalMap = new HashMap<String, Long>();
				List<DailyStatsCommercial> dailyStatsList = dailyStatsCommercialRepository.findDailyStatsBetween(fullStartDate, fullEndDate);
				
				for (DailyStatsCommercial dailyStats : dailyStatsList) {
					BorderStatsCounts borderStatsCounts = new BorderStatsCounts();
					borderStatsCounts.setTimestamp(dailyStats.getId());
					
					Long totalConveyances = null;
					CommercialCount conveyances = new CommercialCount();
					
					for (PortStatsCommercial portStats : dailyStats.getPorts()) {
						if (portStats.getPort().startsWith(ProjectBravoApiConstants.PORT_PREFIX_PROV_MAP_COMMERCIAL.get(provinceCode))) {
							for (PortStatsCountsCommercial counts : portStats.getCounts()) {
								if (counts.getMode().equals(mode.toString())) {
									totalConveyances = (totalConveyances != null) ? totalConveyances + counts.getCount() : counts.getCount();
								}
							}
						}
					}
					
					for (PortStatsCommercial portStats : dailyStats.getPorts()) {
						String province = StatsUtil.getProvinceFromPortCommercial(portStats.getPort());
						
						if (province != null) {
							for (PortStatsCountsCommercial counts : portStats.getCounts()) {
								if (counts.getMode().equals(mode.toString())) {
									if (provinceToTotalMap.get(province) != null) {
										Long total = provinceToTotalMap.get(province);
										Long newTotal = total + counts.getCount();
										provinceToTotalMap.put(province, newTotal);
									} else {
										provinceToTotalMap.put(province, counts.getCount());
									}
								}
							}
						}
					}
					
					conveyances.setTotal(totalConveyances);
					borderStatsCounts.setConveyances(conveyances);
					statsMap.put(dailyStats.getId(), borderStatsCounts);
				}
				borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(statsMap, Calendar.DAY_OF_MONTH, mode));
				borderStats.setProvincialComparisonStats(bravoStatsUtil.buildProvincialComparisonStatsCommercial(provinceToTotalMap));
			} else {
				Map<String, Long> provinceToTotalMap = new HashMap<String, Long>();
				List<DailyStatsTravellers> dailyStatsList = dailyStatsTravellersRepository.findDailyStatsBetween(fullStartDate, fullEndDate);
				
				for (DailyStatsTravellers dailyStats : dailyStatsList) {
					BorderStatsCounts borderStatsCounts = new BorderStatsCounts();
					borderStatsCounts.setTimestamp(dailyStats.getId());
					TravellersCount travellersCount = new TravellersCount();
					Long totalTravellers = null;
					Long totalSecondary = null;
					
					for (PortStatsTravellers portStats : dailyStats.getPorts()) {
						if (portStats.getPort().startsWith(ProjectBravoApiConstants.PORT_PREFIX_PROV_MAP_TRAVELLERS.get(provinceCode))) {
							//TODO: Filter by mode
							for (PassageCounts counts : portStats.getPassageCounts()) {
								totalTravellers = (totalTravellers != null) ? totalTravellers + counts.getCount() : counts.getCount();
							}
							for (ReferralCounts counts : portStats.getReferralCounts()) {
								totalSecondary = (totalSecondary != null) ? totalSecondary + counts.getCount() : counts.getCount();
							}
						}
					}
					
					for (PortStatsTravellers portStats : dailyStats.getPorts()) {
						String province = StatsUtil.getProvinceFromPortTravellers(portStats.getPort());
						
						if (province != null) {
							for (PassageCounts counts : portStats.getPassageCounts()) {
								//if (counts.getMode().equals(mode.toString())) {
									if (provinceToTotalMap.get(province) != null) {
										Long total = provinceToTotalMap.get(province);
										Long newTotal = total + counts.getCount();
										provinceToTotalMap.put(province, newTotal);
									} else {
										provinceToTotalMap.put(province, counts.getCount());
									}
								//}
							}
						}
					}
					
					travellersCount.setTotal(totalTravellers);
					travellersCount.setTotalSecondary(totalSecondary);
					borderStatsCounts.setTravellers(travellersCount);
					statsMap.put(dailyStats.getId(), borderStatsCounts);
				}
				borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(statsMap, Calendar.DAY_OF_MONTH, mode));
				borderStats.setProvincialComparisonStats(bravoStatsUtil.buildProvincialComparisonStatsTravellers(provinceToTotalMap));
			}
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_MONTHLY)) {
			if (mode < 6) {
				Map<String, Long> provinceToTotalMap = new HashMap<String, Long>();
				List<MonthlyStatsCommercial> monthlyStatsList = monthlyStatsCommercialRepository.findMonthlyStatsBetween(fullStartDate, fullEndDate);
				
				for (MonthlyStatsCommercial monthlyStats : monthlyStatsList) {
					BorderStatsCounts borderStatsCounts = new BorderStatsCounts();
					borderStatsCounts.setTimestamp(monthlyStats.getId());
					
					Long totalConveyances = null;
					CommercialCount conveyances = new CommercialCount();
					
					for (PortStatsCommercial portStats : monthlyStats.getPorts()) {
						if (portStats.getPort().startsWith(ProjectBravoApiConstants.PORT_PREFIX_PROV_MAP_COMMERCIAL.get(provinceCode))) {
							for (PortStatsCountsCommercial counts : portStats.getCounts()) {
								if (counts.getMode().equals(mode.toString())) {
									totalConveyances = (totalConveyances != null) ? totalConveyances + counts.getCount() : counts.getCount();
								}
							}
						}
					}
					
					for (PortStatsCommercial portStats : monthlyStats.getPorts()) {
						String province = StatsUtil.getProvinceFromPortCommercial(portStats.getPort());
						
						if (province != null) {
							for (PortStatsCountsCommercial counts : portStats.getCounts()) {
								if (counts.getMode().equals(mode.toString())) {
									if (provinceToTotalMap.get(province) != null) {
										Long total = provinceToTotalMap.get(province);
										Long newTotal = total + counts.getCount();
										provinceToTotalMap.put(province, newTotal);
									} else {
										provinceToTotalMap.put(province, counts.getCount());
									}
								}
							}
						}
					}
					
					conveyances.setTotal(totalConveyances);
					borderStatsCounts.setConveyances(conveyances);
					statsMap.put(monthlyStats.getId(), borderStatsCounts);
				}
				borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(statsMap, Calendar.MONTH, mode));
				borderStats.setProvincialComparisonStats(bravoStatsUtil.buildProvincialComparisonStatsCommercial(provinceToTotalMap));
			} else {
				Map<String, Long> provinceToTotalMap = new HashMap<String, Long>();
				List<MonthlyStatsTravellers> monthlyStatsList = monthlyStatsTravellersRepository.findMonthlyStatsBetween(fullStartDate, fullEndDate);
				
				for (MonthlyStatsTravellers monthlyStats : monthlyStatsList) {
					BorderStatsCounts borderStatsCounts = new BorderStatsCounts();
					borderStatsCounts.setTimestamp(monthlyStats.getId());
					TravellersCount travellersCount = new TravellersCount();
					Long totalTravellers = null;
					Long totalSecondary = null;
					
					for (PortStatsTravellers portStats : monthlyStats.getPorts()) {
						if (portStats.getPort().startsWith(ProjectBravoApiConstants.PORT_PREFIX_PROV_MAP_TRAVELLERS.get(provinceCode))) {
							//TODO: Filter by mode
							for (PassageCounts counts : portStats.getPassageCounts()) {
								totalTravellers = (totalTravellers != null) ? totalTravellers + counts.getCount() : counts.getCount();
							}
							for (ReferralCounts counts : portStats.getReferralCounts()) {
								totalSecondary = (totalSecondary != null) ? totalSecondary + counts.getCount() : counts.getCount();
							}
						}
					}
					
					for (PortStatsTravellers portStats : monthlyStats.getPorts()) {
						String province = StatsUtil.getProvinceFromPortTravellers(portStats.getPort());
						
						if (province != null) {
							for (PassageCounts counts : portStats.getPassageCounts()) {
								//if (counts.getMode().equals(mode.toString())) {
									if (provinceToTotalMap.get(province) != null) {
										Long total = provinceToTotalMap.get(province);
										Long newTotal = total + counts.getCount();
										provinceToTotalMap.put(province, newTotal);
									} else {
										provinceToTotalMap.put(province, counts.getCount());
									}
								//}
							}
						}
					}
					
					travellersCount.setTotal(totalTravellers);
					travellersCount.setTotalSecondary(totalSecondary);
					borderStatsCounts.setTravellers(travellersCount);
					statsMap.put(monthlyStats.getId(), borderStatsCounts);
				}
				borderStats.setAnnualComparisonStats(bravoStatsUtil.buildMockAnnualComparisonStats(statsMap, Calendar.MONTH, mode));
				borderStats.setProvincialComparisonStats(bravoStatsUtil.buildProvincialComparisonStatsTravellers(provinceToTotalMap));
			}
		}

		for (Map.Entry<String, BorderStatsCounts> entry : statsMap.entrySet()) {
			borderStats.getStats().add(entry.getValue());
	    }
		
		return borderStats;
	}
	
}
