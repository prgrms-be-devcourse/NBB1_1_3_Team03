package com.sscanner.team.points.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class TaskExecutorConfig {

    @Bean(name = ["pointTaskExecutor"])
    fun taskExecutor(): ThreadPoolTaskExecutor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 10
            maxPoolSize = 50
            queueCapacity = 100
            threadNamePrefix = "Async-Task-"
            initialize()
        }
    }
}
