package org.sopt.utils;

public class IDGenUtil {
	private static int Id =0 ;

	public static Integer generateId(){
		return Id++;
	}
}
