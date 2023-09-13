package org.openprovenance.prov.service.core.jobs;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.*;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

public class JobManagement {
	public static final String CURL_TOKEN = "CURL_TOKEN";
    public static final String LOG_JOB = "logJob";
    public static final String LOG_URL = "logUrl";
    public static final String LOG_SOURCE = "logSource";
	public static final String UTILS_KEY = "utils";
    public static final String DURATION_KEY = "duration";

    static Logger logger = LogManager.getLogger(JobManagement.class);
	
    public static final String LOGGED_MESSAGE = "loggedMessage";


    
    private static Scheduler the_scheduler;
    public static Scheduler getScheduler() {
        return the_scheduler;
    }
    
	Scheduler scheduler;

	public boolean setupScheduler() {
		SchedulerFactory sf = new StdSchedulerFactory();
		try {
			scheduler = sf.getScheduler();
			scheduler.start();
			the_scheduler=scheduler;
			return true;
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	public Date scheduleJobOld(String visibleId) {

		JobDetail job = JobBuilder.newJob(JobDeleteDocumentResource.class).withIdentity(visibleId, "graph").build();

		// Trigger the job to run on the next 10 round minute
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(visibleId + "-trigger", "graph")
				.startAt(DateBuilder.futureDate(1, IntervalUnit.MINUTE))
				.build();
		try {
			Date date=scheduler.scheduleJob(job, trigger);
			logger.info("schedule date " + date);

			return date;
		} catch (SchedulerException e) {
			e.printStackTrace();
			return null;
		}
	}

	 */

	public Date scheduleJob(String visibleId) {
		return scheduleJob(JobDeleteDocumentResource.class,visibleId,"-trigger", "graph");
	}

	public Date scheduleJob(Class<? extends Job> jobClazz, String visibleId, String idSuffix, String group) {

		int duration=600; //seconds
		try {
			duration=(Integer)the_scheduler.getContext().get(JobManagement.DURATION_KEY);
		} catch (SchedulerException e) {
			logger.throwing(e);
		}
		JobDetail job = JobBuilder.newJob(jobClazz).withIdentity(visibleId, group).build();

		// Trigger the job to run on the next 10 round minute
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(visibleId + idSuffix, group)
				.startAt(DateBuilder.futureDate(duration, IntervalUnit.SECOND))
				.build();
		try {
			Date date=scheduler.scheduleJob(job, trigger);
			logger.debug("schedule date " + date);
			return date;
		} catch (SchedulerException e) {
			logger.throwing(e);
			return null;
		}
	}


	public static boolean logJobp="true".equals(System.getProperty(LOG_JOB));
	public static String  logUrl=System.getProperty(LOG_URL);
    public static String  logSource=System.getProperty(LOG_SOURCE,"provapi");
    public static String  token=System.getProperty(CURL_TOKEN);

	static public void scheduleCurlJob(String id, String loggedMessage) {
	    logger.debug("========================> " + logJobp);
	    if (!logJobp) return;
	    JobDetail job = JobBuilder.newJob(CurlJob.class)
                .withIdentity(id, "curl")
                .usingJobData(LOGGED_MESSAGE,loggedMessage).build();

        // Trigger the job to run on the next 1 round minute
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(id, "graph")
                .startAt(DateBuilder.futureDate(1, IntervalUnit.MINUTE))
                .build();
        try {
            JobManagement.getScheduler().scheduleJob(job, trigger);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        
	}

}
