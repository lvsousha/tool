package com.lvdousha.tool.quartz;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class Main {

	public static void main(String[] args) throws SchedulerException {
		// TODO Auto-generated method stub
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		JobDetail jd = JobBuilder.newJob(HelloQuartzJob.class).build();
//		Trigger
		
		
		
		
		
		

	}

}
