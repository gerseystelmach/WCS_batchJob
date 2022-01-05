package org.wcs.batchjobworkshop.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    JobLauncher jobLauncher;

    /*Preciso ter o mesmo nome do job que quero usar */
    @Autowired
    Job heartBeatJob;

    @ResponseBody
    @GetMapping("")
    Map<String, String> index() {
        Map<String, String> msg = new HashMap<>();
        msg.put("message", "Welcome to batch job workshop");

        return msg;
    }

    @ResponseBody
    @GetMapping("/heartbeat")
    Map<String, String>  heartBeat() {

        Map<String, JobParameter> confMap = new HashMap<String, JobParameter>();
        JobParameters jobParameters = new JobParameters(confMap);

        Map<String, String> msg = new HashMap<>();
        msg.put("message", "Welcome to heartbeat page");

        try {
            jobLauncher.run(heartBeatJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobParametersInvalidException | JobRestartException | JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
