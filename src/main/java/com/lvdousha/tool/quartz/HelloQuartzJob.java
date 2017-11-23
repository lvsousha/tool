package com.lvdousha.tool.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloQuartzJob implements Job{
	public void execute(JobExecutionContext context) 
            throws JobExecutionException {
        System.out.println("Hello, Quartz! - executing its JOB at "+ 
            new Date() + " by " + context.getTrigger().getCalendarName());
    }
}
