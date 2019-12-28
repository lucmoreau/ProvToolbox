package org.openprovenance.prov.service.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.impl.StdSchedulerFactory;

public class JobManagement {
	public static final String CURL_TOKEN = "CURL_TOKEN";
    public static final String LOG_JOB = "logJob";
    public static final String LOG_URL = "logUrl";
    public static final String LOG_SOURCE = "logSource";
	public static final String UTILS_KEY = "utils";

	static Logger logger = Logger.getLogger(JobManagement.class);

	static public class DeleteJob implements Job {

		public DeleteJob() {
		}

		@Override
		public void execute(JobExecutionContext context){
			System.out.println("==========> delete job called " + context.getTrigger());

			ServiceUtils utils=new ServiceUtils();
			try {
				SchedulerContext schedulerContext = context.getScheduler().getContext();
				utils=(ServiceUtils)schedulerContext.getOrDefault(UTILS_KEY,utils);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}


			String visibleId = context.getJobDetail().getKey().getName();
			if (visibleId == null) {
				logger.error("no visibleId " + context);
			} else {
				DocumentResource vr = utils.getResourceIndex().get(visibleId);

				if (vr == null) {
					logger.error("no validation resource for " + visibleId);
				} else {

					logger.debug("deleting ... " + visibleId);
					utils.getResourceIndex().remove(visibleId);

					if (vr.storageId != null) {
						logger.info("TODO: need to update storage backend: deleting ... " + vr.storageId);
						new File(vr.storageId).delete();
						vr.storageId = null;
					}
					/*
					if (vr.reportFile != null) {
						logger.debug("deleting ... " + vr.reportFile);
						new File(vr.reportFile).delete();
						vr.reportFile = null;
					}

					 */
					/*
					if (vr.complete != null) {
						logger.debug("deleting ... " + vr.complete);
						new File(vr.complete).delete();
						vr.complete = null;
					}

					 */

				}
			}
		}

	}
	
    public static final String LOGGED_MESSAGE = "loggedMessage";
    private static final String SAVE_MESSAGE = null;

    static public class CurlJob implements Job {


        public CurlJob() {
        }

        @Override
        public void execute(JobExecutionContext arg0)
                throws JobExecutionException {
        	String message=arg0.getJobDetail().getJobDataMap().getString(LOGGED_MESSAGE);
            logger.debug("==========> curl job called " + message);
            String [] components=message.split(",");
            Runtime runtime = Runtime.getRuntime();
            try {
                String action=components[0];
                String graphId=components[1];
                String filename=components[2];
                String source=components[3];
                
                if (PostService.EXPANSION.equals(action)) {
                    graphId=graphId+"_expanded";
                }
                

                String [] curl_cmd=new String[] {
                        "curl",  "--fail", 
                        "-H", "Authorization: Token " + token,
                        "-F", "source=" + logSource,
                        "-F", "uri=https://openprovenance.org/services/provapi/documents/" + graphId,
                        "-F", "prov_format=" + source.toLowerCase(),
                        "-F", "content=@" + filename,
                        logUrl}; 
                
                System.out.println(">curl cmd:\n" );
                for (String str: curl_cmd) {
                    System.out.println(str);
                }

                Process process = runtime.exec(curl_cmd);
                InputStream is=process.getInputStream();
                int resultCode = process.waitFor();
        
                System.out.println(">curl cmd: " + resultCode);

                if (resultCode == 0) {
                    // all is good
                } else {
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader buff = new BufferedReader (isr);
                    String line;
                    while((line = buff.readLine()) != null)
                        System.out.print(line);
                    buff.close();
                }

            } catch (Throwable cause) {
                // process cause
            }
        }
    }
    
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

	public Date scheduleJob(String visibleId) {

		JobDetail job = JobBuilder.newJob(DeleteJob.class)
				.withIdentity(visibleId, "graph").build();

		// Trigger the job to run on the next 10 round minute
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(visibleId + "-trigger", "graph")
				.startAt(DateBuilder.futureDate(10, IntervalUnit.MINUTE))
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

	public static boolean logJobp="true".equals(System.getProperty(LOG_JOB));
	static String  logUrl=System.getProperty(LOG_URL);
    static String  logSource=System.getProperty(LOG_SOURCE,"provapi");
    static String  token=System.getProperty(CURL_TOKEN);

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
