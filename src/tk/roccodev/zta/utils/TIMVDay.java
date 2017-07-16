package tk.roccodev.zta.utils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import tk.roccodev.zta.ZTAMain;

public class TIMVDay {

	

	public static Long parseCalendar(Calendar c){
		return Long.parseLong(c.get(Calendar.YEAR) + "" + (c.get(Calendar.MONTH) + 1) + c.get(Calendar.DAY_OF_MONTH));
	}
	
	public static String fromCalendar(Calendar c){
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH); 
	}
	
	
	public static boolean containsDayfile(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String fileName = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
		File path = new File(ZTAMain.mcFile + "/timv/dailykarma/" + fileName);
		
		return path.exists();
	}
	

}
