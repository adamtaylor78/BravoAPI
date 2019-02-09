package ca.gc.cbsa.mcoe.bravo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.gc.cbsa.mcoe.bravo.domain.commercial.DailyStatsCommercial;
import ca.gc.cbsa.mcoe.bravo.domain.commercial.HourlyStatsCommercial;
import ca.gc.cbsa.mcoe.bravo.domain.commercial.MonthlyStatsCommercial;
import ca.gc.cbsa.mcoe.bravo.domain.travellers.DailyStatsTravellers;
import ca.gc.cbsa.mcoe.bravo.domain.travellers.HourlyStatsTravellers;
import ca.gc.cbsa.mcoe.bravo.domain.travellers.MonthlyStatsTravellers;
import ca.gc.cbsa.mcoe.bravo.repository.commercial.DailyStatsCommercialRepository;
import ca.gc.cbsa.mcoe.bravo.repository.commercial.HourlyStatsCommercialRepository;
import ca.gc.cbsa.mcoe.bravo.repository.commercial.MonthlyStatsCommercialRepository;
import ca.gc.cbsa.mcoe.bravo.repository.travellers.DailyStatsTravellersRepository;
import ca.gc.cbsa.mcoe.bravo.repository.travellers.HourlyStatsTravellersRepository;
import ca.gc.cbsa.mcoe.bravo.repository.travellers.MonthlyStatsTravellersRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectBravoApiApplicationTests {

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
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testLoadHourlyCommercialStats() {
		List<HourlyStatsCommercial> hourlyStatsList = hourlyStatsCommercialRepository.findHourlyStatsBetween("2018-12-01 00:00", "2018-12-01 23:59");
		assertNotNull(hourlyStatsList);
		assertTrue(hourlyStatsList.size() == 24);
	}
	
	@Test
	public void testLoadDailyCommercialStats() {
		List<DailyStatsCommercial> dailyStatsList = dailyStatsCommercialRepository.findDailyStatsBetween("2018-12-01 00:00", "2018-12-31 23:59");
		assertNotNull(dailyStatsList);
		assertTrue(dailyStatsList.size() == 31);
	}
	
	@Test
	public void testLoadMonthlyCommercialStats() {
		List<MonthlyStatsCommercial> monthlyStatsList = monthlyStatsCommercialRepository.findMonthlyStatsBetween("2018-01-00 00:00", "2018-12-31 23:59");
		assertNotNull(monthlyStatsList);
		assertTrue(monthlyStatsList.size() == 12);
	}
	
	@Test
	public void testLoadHourlyTravellersStats() {
		List<HourlyStatsTravellers> hourlyStatsList = hourlyStatsTravellersRepository.findHourlyStatsBetween("2018-12-31 00:00", "2018-12-31 23:59");
		assertNotNull(hourlyStatsList);
		assertTrue(hourlyStatsList.size() == 24);
	}
	
	@Test
	public void testLoadDailyTravellersStats() {
		List<DailyStatsTravellers> dailyStatsList = dailyStatsTravellersRepository.findDailyStatsBetween("2018-12-01 00:00", "2018-12-31 23:59");
		assertNotNull(dailyStatsList);
		assertTrue(dailyStatsList.size() == 31);
	}
	
	@Test
	public void testLoadMonthlyTravellersStats() {
		List<MonthlyStatsTravellers> monthlyStatsList = monthlyStatsTravellersRepository.findMonthlyStatsBetween("2018-01-00 00:00", "2018-12-31 23:59");
		assertNotNull(monthlyStatsList);
		assertTrue(monthlyStatsList.size() == 12);
	}

}

