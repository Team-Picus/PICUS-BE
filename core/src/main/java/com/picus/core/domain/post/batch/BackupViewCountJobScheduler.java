package com.picus.core.domain.post.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BackupViewCountJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job backupJob;

    public BackupViewCountJobScheduler(JobLauncher jobLauncher, Job backupJob) {
        this.jobLauncher = jobLauncher;
        this.backupJob = backupJob;
    }

    @Scheduled(cron = "0 0 0,12 * * ?")
    public void runBackupJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(backupJob, params);
    }
}
