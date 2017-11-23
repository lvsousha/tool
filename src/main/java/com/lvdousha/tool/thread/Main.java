package com.lvdousha.tool.thread;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Person> list = new ArrayList<Person>();
		Person p = new Person("a");
		list.add(p);
		Person a = list.get(0);
		a.name = "c";
		System.out.println(list.get(0).name);
	}

}
