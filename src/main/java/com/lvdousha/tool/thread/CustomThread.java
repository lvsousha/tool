package com.lvdousha.tool.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class CustomThread implements Runnable{

//	private volatile AtomicInteger i = new AtomicInteger(1);
	private volatile int i = 1;
	
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread()+"=="+i);
		try {
			Thread.sleep(1000);
			System.out.println(Thread.currentThread()+"=="+i);
//			i.incrementAndGet();
			i = i+1;
			System.out.println(Thread.currentThread()+"=="+i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
