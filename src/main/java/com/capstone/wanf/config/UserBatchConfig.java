package com.capstone.wanf.config;

import com.capstone.wanf.user.domain.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class UserBatchConfig {
    private final EntityManager entityManager;

    private int chunkSize = 10;

    @Bean
    public Job userDeleteJob(JobRepository jobRepository, Step userDeleteStep) {
        return new JobBuilder("userDeleteJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(userDeleteStep)
                .build();
    }

    @Bean
    public Step userDeleteStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("userDeleteStep", jobRepository)
                .<User, User>chunk(chunkSize, transactionManager)
                .reader(userItemReader())
                .processor(userItemProcessor())
                .writer(userItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<User> userItemReader() {
        JpaPagingItemReader<User> reader = new JpaPagingItemReader<>();

        reader.setEntityManagerFactory(entityManager.getEntityManagerFactory());

        reader.setQueryString("SELECT u FROM User u WHERE u.createdDate < :time");

        Map<String, Object> parameterValues = new HashMap<>();

        parameterValues.put("time", LocalDateTime.now().minusHours(24));

        reader.setParameterValues(parameterValues);

        reader.setPageSize(chunkSize);

        return reader;
    }

    @Bean
    public ItemProcessor<User, User> userItemProcessor() {
        return user -> {
            if (user.getUserPassword() == null) {
                // EntityManager에서 User 엔티티를 조회하여 영속 상태로 만듦
                User persistedUser = entityManager.find(User.class, user.getId());

                if (persistedUser != null) {
                    // UserPassword가 null이면 삭제
                    entityManager.remove(persistedUser);

                    entityManager.flush();

                    return null;
                }
            }
            // UserPassword가 null이 아닌 데이터는 그대로 유지하고 삭제하지 않음
            return user;
        };
    }

    @Bean
    public JpaItemWriter<User> userItemWriter() {
        JpaItemWriter<User> writer = new JpaItemWriter<>();

        writer.setEntityManagerFactory(entityManager.getEntityManagerFactory());

        return writer;
    }
}

