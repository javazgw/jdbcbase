package com.base.myproject.server.timetask;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
/**
 * 定时程序的入口，封装quartz，需要class 是继承了job的接口
 * @author Administrator
 *
 */
public class QuartZJob {
	static QuartZJob qzj = null;
	SchedulerFactory sf = null;

	private QuartZJob() throws SchedulerException {
		init();
	}

	public static QuartZJob getInstance() throws SchedulerException {
		if (qzj == null) {
			qzj = new QuartZJob();
		}
		return qzj;
	}
/**
 *
 * @param c 必须是实现了job接口
 * @param cronexpression  "* 0/10 * * * ?" 触发的表达式，
 * @throws SchedulerException
 */
	public void addjob(Class<? extends Job> c, Map<? extends String, ? extends Object> t, String cronexpression) throws SchedulerException {
		Scheduler sched = sf.getScheduler();
		sched.start();
		JobDetail job = newJob(c).withIdentity(new Random().nextInt()+"", "group1")
		// .storeDurably()
				.build();
		if(t!=null)
			job.getJobDataMap().putAll(t);
		Trigger trigger = newTrigger().withIdentity(new Random().nextInt()+"", "group1")
				.withSchedule(cronSchedule(cronexpression))
				// .startNow()
				// .startAt(runTime)
				.build();
		sched.scheduleJob(job, trigger);
	}

	void init() throws SchedulerException {
		sf = new StdSchedulerFactory();

	}
	
	public static void main(String[] argv) throws SchedulerException
	{
		HashMap<String, Object> hm = new HashMap<String, Object> ();
		hm.put("zgw", "zgw2");
		hm.put("zgw1", "zgw3");
		QuartZJob.getInstance().addjob(Testjob.class, hm,"*/10 * * * * ?");
		
		HashMap<String, Object> hm2 = new HashMap<String, Object> ();
		hm2.put("zgw", "zgw22222222");
		hm2.put("zgw1", "zgw3");
		QuartZJob.getInstance().addjob(Testjob.class, hm2,"*/8 * * * * ?");
	}
}
