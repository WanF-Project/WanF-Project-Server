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

    /**
     * 스케줄링된 작업을 관리하고, 주기적으로 사용자 삭제 작업을 수행합니다.
     *
     * @throws JobParametersInvalidException       잘못된 JobParameters 예외
     * @throws JobExecutionAlreadyRunningException 이미 실행 중인 Job 예외
     * @throws JobRestartException                 Job 재시작 예외
     * @throws JobInstanceAlreadyCompleteException 이미 완료된 JobInstance 예외
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void deleteUnverifiedUserJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("time", LocalDateTime.now().toString())
                .toJobParameters();

        jobLauncher.run(userDeleteJob, jobParameters);
    }
}
