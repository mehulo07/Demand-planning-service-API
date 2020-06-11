package com.bns.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class DateUtility {
	
	 static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	public  String firstDatOfYear(int year) {
		
		Calendar calendarStart=Calendar.getInstance();
	    calendarStart.set(Calendar.YEAR,year);
	    calendarStart.set(Calendar.MONTH,0);
	    calendarStart.set(Calendar.DAY_OF_MONTH,1);
	    Date startDate=calendarStart.getTime();
	    String strDate= formatter.format(startDate);
	    
	    System.out.println("strDate is :"+strDate);
	    
		return strDate;
	}
	
	public  String lastDatOfYear(int year) {
		
		Calendar calendarStart=Calendar.getInstance();
	    calendarStart.set(Calendar.YEAR,year);
	    calendarStart.set(Calendar.MONTH,11);
	    calendarStart.set(Calendar.DAY_OF_MONTH,31);
	    Date endDate=calendarStart.getTime();
	    String endDatestr= formatter.format(endDate);
	    
	    System.out.println("endDatestr is :"+endDatestr);
	    
		return endDatestr;
	}
	
}
