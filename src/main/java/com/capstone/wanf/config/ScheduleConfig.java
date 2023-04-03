package com.capstone.wanf.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Configuration
@EnableScheduling
public class ScheduleConfig {
    private final JobLauncher jobLauncher;
    private final Job userDeleteJob;

    // 매번 같은 JobParameters를 사용해서 Job이 중복 실행되지 않도록 막고 있어 정상적으로 동작하지 않음.
    // JobParameters를 동적으로 생성하여 매번 다른 값을 사용하도록 변경 -> 현재 시간을 JobParameters로 넣어 매번 새로운 JobInstance가 생성됨
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") // 매일 자정에 실행
    public void deleteUnverifiedUserJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("time", LocalDateTime.now().toString())
                .toJobParameters();
        jobLauncher.run(userDeleteJob, jobParameters);
    }
}
