package com.ns.main.entity;

import java.util.Calendar;

public class DateTime {

	private long base;
	
	private int year;
	private String year_s;
	private int month;
	private String month_s;
	private int date;
	private String date_s;
	private int hour;
	private String hour_s;
	private int minute;
	private String minute_s;
	private int second;
	private String second_s;
	
	public DateTime() {
		base = System.currentTimeMillis();
	}
	
	public DateTime(long datetime) {
		base = datetime;
	}
	
	public DateTime(String datetime) {
		if(datetime.equals("") || !datetime.contains(" ") || !datetime.contains("-") || !datetime.contains(":")) {
			base = System.currentTimeMillis();
		}
		try {
			String[] date_sa = datetime.split(" ")[0].split("-");
			String[] time = datetime.split(" ")[1].split(":");
			Calendar calendar = Calendar.getInstance();
			int sec = 0;
			if(time.length == 2) {
				sec = 0;
			}
			else {
				sec = Integer.parseInt(time[2]);
			}
			calendar.set(Integer.parseInt(date_sa[0]), Integer.parseInt(date_sa[1]) - 1, Integer.parseInt(date_sa[2]), 
					Integer.parseInt(time[0]), Integer.parseInt(time[1]), sec);
			base = calendar.getTimeInMillis();
		}catch (Exception e) {
			base = System.currentTimeMillis();
		}
		update();
	}

	public boolean updateBase() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, date, hour, minute, second);
		base = calendar.getTimeInMillis();
		return update();
	}
	
	public boolean update() {
		if(base > 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(base);
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			date = calendar.get(Calendar.DATE);
			hour = calendar.get(Calendar.HOUR);
			minute = calendar.get(Calendar.MINUTE);
			second = calendar.get(Calendar.SECOND);
			if((year_s = "" + year).length() != 4)return false;
			if((month_s = "" + (month + 1)).length() !=2) {
				if((month_s = "0" + month_s).length() != 2)return false;
			}
			if((date_s = "" + date).length() !=2) {
				if((date_s = "0" + date_s).length() != 2)return false;
			}
			if((hour_s = "" + hour).length() !=2) {
				if((hour_s = "0" + hour_s).length() != 2)return false;
			}
			if((minute_s = "" + minute).length() !=2) {
				if((minute_s = "0" + minute_s).length() != 2)return false;
			}
			if((second_s = "" + second).length() !=2) {
				if((second_s = "0" + second_s).length() != 2)return false;
			}
			return true;
		}
		return false;
	}
	
	public long getBase() {
		return base;
	}

	public void setBase(long base) {
		this.base = base;
		update();
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
		updateBase();
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
		updateBase();
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
		updateBase();
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
		updateBase();
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
		updateBase();
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
		updateBase();
	}
	
	@Override
	public String toString() {
		return year_s + "-" + month_s + "-" + date_s + " " + hour_s + ":" + minute_s + ":" + second_s;
	}

	public String getYear_s() {
		return year_s;
	}

	public void setYear_s(String year_s) {
		this.year_s = year_s;
	}

	public String getMonth_s() {
		return month_s;
	}

	public void setMonth_s(String month_s) {
		this.month_s = month_s;
	}

	public String getDate_s() {
		return date_s;
	}

	public void setDate_s(String date_s) {
		this.date_s = date_s;
	}

	public String getHour_s() {
		return hour_s;
	}

	public void setHour_s(String hour_s) {
		this.hour_s = hour_s;
	}

	public String getMinute_s() {
		return minute_s;
	}

	public void setMinute_s(String minute_s) {
		this.minute_s = minute_s;
	}

	public String getSecond_s() {
		return second_s;
	}

	public void setSecond_s(String second_s) {
		this.second_s = second_s;
	}
	
}
