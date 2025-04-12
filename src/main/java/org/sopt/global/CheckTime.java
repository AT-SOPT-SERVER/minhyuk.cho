package org.sopt.global;


import java.time.LocalDateTime;

public class CheckTime {
	private static LocalDateTime dateTime = null;


	public static Boolean checkTime(){
	if(dateTime == null || dateTime.plusMinutes(3).isBefore(LocalDateTime.now())) {
			return true;
		}
		return false;
	}

	public static void setTimestamp(){
		dateTime = LocalDateTime.now();
	}
}
