package ca.gc.cbsa.mcoe.bravo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.gc.cbsa.mcoe.bravo.domain.DailyStats;
import ca.gc.cbsa.mcoe.bravo.domain.HourlyStats;
import ca.gc.cbsa.mcoe.bravo.domain.MonthlyStats;
import ca.gc.cbsa.mcoe.bravo.repository.DailyStatsRepository;
import ca.gc.cbsa.mcoe.bravo.repository.HourlyStatsRepository;
import ca.gc.cbsa.mcoe.bravo.repository.MonthlyStatsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectBravoApiApplicationTests {

	@Autowired
	private HourlyStatsRepository hourlyStatsRepository;
	
	@Autowired
	private DailyStatsRepository dailyStatsRepository;
	
	@Autowired
	private MonthlyStatsRepository monthlyStatsRepository;
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testLoadHourlyStats() {
		List<HourlyStats> hourlyStatsList = hourlyStatsRepository.findHourlyStatsBetween("2018-12-01 00:00", "2018-12-01 23:59");
		assertNotNull(hourlyStatsList);
		assertTrue(hourlyStatsList.size() == 24);
	}
	
	@Test
	public void testLoadDailyStats() {
		List<DailyStats> dailyStatsList = dailyStatsRepository.findDailyStatsBetween("2018-12-01 00:00", "2018-12-31 23:59");
		assertNotNull(dailyStatsList);
		assertTrue(dailyStatsList.size() == 31);
	}
	
	@Test
	public void testLoadMonthlyStats() {
		List<MonthlyStats> monthlyStatsList = monthlyStatsRepository.findMonthlyStatsBetween("2018-01-00 00:00", "2018-12-31 23:59");
		assertNotNull(monthlyStatsList);
		assertTrue(monthlyStatsList.size() == 12);
	}

}

