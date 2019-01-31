package ca.gc.cbsa.mcoe.bravo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.github.javafaker.Faker;

import ca.gc.cbsa.mcoe.bravo.domain.BorderStats;

public class StatsUtil {

	public static List<BorderStats> buildMockStats(int calendarUnit, int mode, String startDate, String endDate) throws ParseException {
		List<BorderStats> borderStatsList = new ArrayList<>();
		
		Faker faker = new Faker();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date startDateObj = format.parse(startDate);
		Date endDateObj = format.parse(endDate); 
		
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDateObj);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDateObj);
		
		int divider = 1;
		
		if (calendarUnit == Calendar.HOUR) {
			divider = 8760;
		} else if (calendarUnit == Calendar.DAY_OF_MONTH) {
			divider = 365;
		} else if (calendarUnit == Calendar.MONTH) {
			divider = 12;
		}
		
		while (startCal.before(endCal)) {
			BorderStats borderStats = new BorderStats();
			borderStats.setTotal(Long.valueOf(faker.number().numberBetween(100000, 250000) / divider));
			if (mode >= 6) {
				borderStats.setAirSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				borderStats.setAirTotal(Long.valueOf(faker.number().numberBetween(25000, 75000) / divider));
				borderStats.setLandSecondaryTotal(Long.valueOf(faker.number().numberBetween(9000, 18000) / divider));
				borderStats.setLandTotal(Long.valueOf(faker.number().numberBetween(75000, 150000) / divider));
				borderStats.setTotalSecondary(Long.valueOf(faker.number().numberBetween(15000, 25000) / divider));
			}
			borderStats.setTimestamp(format.format(startCal.getTime()));
			borderStatsList.add(borderStats);
			startCal.add(calendarUnit, 1);
		}
		
		return borderStatsList;
		
	}
}
