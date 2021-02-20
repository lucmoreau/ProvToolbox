package org.openprovenance.prov.service.core.jobs;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.service.core.PostService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.openprovenance.prov.service.core.jobs.JobManagement.*;

public class CurlJob implements Job {
    static Logger logger = LogManager.getLogger(CurlJob.class);


    public CurlJob() {
    }

    @Override
    public void execute(JobExecutionContext arg0) {
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