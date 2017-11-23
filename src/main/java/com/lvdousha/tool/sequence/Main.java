package com.lvdousha.tool.sequence;

public class Main {
//	861427063617527907
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Sequence sequence = new Sequence(4l, 5l);
		for(int i=0; i<10; i++){
			System.out.println(sequence.nextId());
		}
		
	}

}
