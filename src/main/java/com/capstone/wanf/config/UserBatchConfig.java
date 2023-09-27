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

    /**
     * 사용자 삭제 작업을 위한 Spring Batch Job을 설정하고 생성합니다.
     *
     * @param jobRepository  JobRepository 빈
     * @param userDeleteStep 사용자 삭제 Step 빈
     * @return 사용자 삭제 작업(Job) 빈
     */
    @Bean
    public Job userDeleteJob(JobRepository jobRepository, Step userDeleteStep) {
        return new JobBuilder("userDeleteJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(userDeleteStep)
                .build();
    }

    /**
     * 사용자 삭제를 위한 Spring Batch Step을 설정하고 생성합니다.
     *
     * @param jobRepository      JobRepository 빈
     * @param transactionManager PlatformTransactionManager 빈
     * @return 사용자 삭제 Step 빈
     */
    @Bean
    public Step userDeleteStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("userDeleteStep", jobRepository)
                .<User, User>chunk(chunkSize, transactionManager)
                .reader(userItemReader())
                .processor(userItemProcessor())
                .writer(userItemWriter())
                .build();
    }

    /**
     * JPA 페이징 아이템 리더(JpaPagingItemReader)를 설정하고 생성하여 User 엔티티를 읽어옵니다.
     *
     * @return User 엔티티를 읽어오는 JpaPagingItemReader
     */
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

    /**
     * User 엔티티를 처리하는 아이템 프로세서(ItemProcessor)를 설정하고 생성합니다.
     *
     * @return User 엔티티를 처리하는 ItemProcessor
     */
    @Bean
    public ItemProcessor<User, User> userItemProcessor() {
        return user -> {
            if (user.getUserPassword() == null) {
                User persistedUser = entityManager.find(User.class, user.getId());

                if (persistedUser != null) {
                    entityManager.remove(persistedUser);

                    entityManager.flush();

                    return null;
                }
            }
            return user;
        };
    }

    /**
     * JPA 아이템 라이터(JpaItemWriter)를 설정하고 생성하여 User 엔티티를 작성합니다.
     *
     * @return User 엔티티를 작성하는 JpaItemWriter
     */
    @Bean
    public JpaItemWriter<User> userItemWriter() {
        JpaItemWriter<User> writer = new JpaItemWriter<>();

        writer.setEntityManagerFactory(entityManager.getEntityManagerFactory());

        return writer;
    }
}

