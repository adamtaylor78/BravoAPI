package ca.gc.cbsa.mcoe.bravo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

import ca.gc.cbsa.mcoe.bravo.ProjectBravoApiConstants;

public class DateUtil {

	public static String replaceDayInDateWith(String date, String replacementDay) {
		return date.replaceAll("([0-9]{4}-[0-1][0-9]-)([0-3][0-9])(\\s[0-1][0-9]|[2][0-3]:[0-5][0-9]:[0-5][0-9])", "$1" + replacementDay + "$3");
	}
	
	public static Calendar buildCalender(String timeDelimiter, String dateString) throws ParseException {
		Calendar cal = null;
		
		if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_HOURLY)) {
			SimpleDateFormat format = new SimpleDateFormat(ProjectBravoApiConstants.DATE_FORMAT_WITH_HOUR_MIN);
			Date dateObj = format.parse(dateString);
			cal = Calendar.getInstance();
			cal.setTime(dateObj);
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_DAILY)) {
			SimpleDateFormat format = new SimpleDateFormat(ProjectBravoApiConstants.DATE_FORMAT_WITHOUT_HOUR_MIN);
			Date dateObj = format.parse(dateString);
			cal = Calendar.getInstance();
			cal.setTime(dateObj);
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_MONTHLY)) {
			YearMonth yearMonth = YearMonth.parse(dateString);
			
			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, yearMonth.getYear());
			cal.set(Calendar.MONTH, yearMonth.getMonth().getValue() - 1);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
		} else if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_ANNUAL)) {
			Year year = Year.parse(dateString);
			
			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, year.getValue());
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}
		
		return cal;
		
	}
	
	/**
	 * 
	 * @param timeDelimiter hour, day, month or year.
	 * @param simpleDateString Could be of format yyyy, yyyy-MM, yyyy-MM-dd or yyyy-MM-dd HH:mm (Depending on the timeDelimiter)
	 * @return Full date string in the format yyyy-MM-dd HH:mm:ss
	 * @throws ParseException
	 */
	public static String buildFullDateString(String timeDelimiter, String simpleDateString) throws ParseException {
		String fullDateString = null;
		SimpleDateFormat dateFormatWithMinSecs = new SimpleDateFormat(ProjectBravoApiConstants.DATE_FORMAT_WITH_HOUR_MIN_SECS);
		
		Calendar cal = buildCalender(timeDelimiter, simpleDateString);
		Date date = cal.getTime();
		
		fullDateString = dateFormatWithMinSecs.format(date);
			
		if (timeDelimiter.equals(ProjectBravoApiConstants.TIME_DELIMITER_MONTHLY)) {
			fullDateString = DateUtil.replaceDayInDateWith(fullDateString, "00");
		}
		
		return fullDateString;
	}
}
