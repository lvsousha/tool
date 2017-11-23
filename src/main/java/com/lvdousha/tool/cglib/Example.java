package com.lvdousha.tool.cglib;

import java.util.Date;

public class Example {

	private String global1 = "g_string";
	private int i = 2;
	public Date date;
	private final String global2 = "g_final";
	private static String global3 = "g_static";
	private final int j = 3;
	
	public void test(){
		String partial1 = "p_string";
		final String partial2 = "p_final";
	}
	
	public String test2(){
		test3();
		test4();
		return "return_test2";
	}
	
	private String test3(){
		return "return_test3";
	}
	
	private static void test4(){
		int k = 9;
	}
	
}
