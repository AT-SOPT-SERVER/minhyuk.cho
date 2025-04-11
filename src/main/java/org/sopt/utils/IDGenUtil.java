package org.sopt.utils;

public class IDGenUtil {
	private static long Id =0 ;

	public static Long generateId(){
		return Id++;
	}
}
