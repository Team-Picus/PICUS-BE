package com.picus.core.domain.post.infra.batch;

import com.picus.core.domain.post.domain.entity.view.BackupViewCount;
import com.picus.core.domain.post.domain.entity.view.ViewCount;
import com.picus.core.domain.post.domain.repository.BackupViewCountRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BackupViewCountJobConfig {

    private final RedisTemplate<String, Integer> redisTemplate;
    private final BackupViewCountRepository backupViewCountRepository;

    public BackupViewCountJobConfig(RedisTemplate<String, Integer> redisTemplate,
                                    BackupViewCountRepository backupViewCountRepository) {
        this.redisTemplate = redisTemplate;
        this.backupViewCountRepository = backupViewCountRepository;
    }

    @Bean
    public Job backupJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("backupJob", jobRepository)
                .start(backupStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step backupStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("backupStep", jobRepository)
                .<ViewCount, BackupViewCount>chunk(500, transactionManager)
                .reader(redisViewCountItemReader())
                .processor(viewCountProcessor())
                .writer(backupViewCountItemWriter())
                .build();
    }

    @Bean
    public RedisViewCountItemReader redisViewCountItemReader() {
        return new RedisViewCountItemReader(redisTemplate);
    }

    @Bean
    public ViewCountProcessor viewCountProcessor() {
        return new ViewCountProcessor();
    }

    @Bean
    public BackupViewCountItemWriter backupViewCountItemWriter() {
        return new BackupViewCountItemWriter(backupViewCountRepository);
    }
}
