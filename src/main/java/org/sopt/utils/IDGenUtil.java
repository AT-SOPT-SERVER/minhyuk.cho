package org.sopt.utils;

public class IDGenUtil {
	private static int Id =0 ;
	public static Integer getId(){
		return Id;
	}

	public static void setId(Integer newId){
		Id = newId;
	}

	public static Integer generateId(){
		return Id++;
	}
}
