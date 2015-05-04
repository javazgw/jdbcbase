package com.base.myproject.server.timetask;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Testjob implements Job{

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyy.MMMMM.dd GGG hh:mm:ss aaa");
		
		Date d = new Date(System.currentTimeMillis());
		System.out.println(context.getJobDetail().getJobDataMap());
		System.out.println(context.getJobDetail().getJobDataMap().get("zgw"));
		System.out.println("date="+sdf.format(d));
	}

}
 